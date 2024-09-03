package info.ggamt.gest.service.baram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.ggamt.gest.domain.baram.DayHistory;
import info.ggamt.gest.domain.baram.DayTargetUser;
import info.ggamt.gest.domain.baram.TimeHistory;
import info.ggamt.gest.dto.baram.BaramHistoryDTO;
import info.ggamt.gest.dto.baram.BaramUserBasicInfoDTO;
import info.ggamt.gest.repository.baram.DayHistoryRepository;
import info.ggamt.gest.repository.baram.DayTargetUserRepository;
import info.ggamt.gest.repository.baram.TimeHistoryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.net.http.HttpClient;
@Service
@Transactional
public class BaramService {

    @Autowired
    DayTargetUserRepository dayTargetUserRepository;

    @Autowired
    DayHistoryRepository dayHistoryRepository;
    
    @Autowired
    TimeHistoryRepository timeHistoryRepository;

    @Autowired
    NexonOpenApiService nexonOpenApiService;
    
    @Transactional(readOnly = true)
    public BaramHistoryDTO getAllBaramHistory () {
        BaramHistoryDTO bh = new BaramHistoryDTO();
        List<TimeHistory> timeHistory = timeHistoryRepository.findAll();
        List<DayHistory> dayHistory = dayHistoryRepository.findAll();
        bh.setDayHistoryList(dayHistory);
        bh.setTimeHistoryList(timeHistory);
        return bh;
    }

    @Transactional(readOnly = true)
    public List<String> getAllOcids () {
        return dayTargetUserRepository.findAll().stream().map((r) -> r.getOcid()).collect(Collectors.toList());
    }

    /**
     * gthrTodayOcids
     * @description 당일 수집 대상자들의 ocid를 수집
     * @apiNote 각 직업별 랭킹 10,000위 (총 100,000명 조회)
     */
    public long gthrTodayOcids () {
        nexonOpenApiService.keyLoading();
        List<List<String>> nameSlots = nexonOpenApiService.parseGthrUserName(); // 홈페이지 html 파싱 > 유저명 조회
        String hsDt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int max = nameSlots.size(); // 보통 10
        List<DayTargetUser> dtus = new ArrayList<DayTargetUser>();
        for (int jobIndex=0; jobIndex < max; jobIndex++) {
            HttpClient client = HttpClient.newHttpClient();
            List<String> nameSlot = nameSlots.get(jobIndex);
            int frame = 20;
            int listMax = nameSlot.size();
            for(int i=0; i<listMax; i+=frame) {
                List<String> subSlot = nameSlot.subList(i, i + frame);
                List<String> resultOcids = nexonOpenApiService.retvOcidOpenApi(client, subSlot);
                Long jobTp = Long.valueOf(jobIndex);
                for(String resultOcid : resultOcids) {
                    // 테이블 insert 규격에 맞게 세팅
                    DayTargetUser dtu = new DayTargetUser();
                    dtu.setHsDt(hsDt);
                    dtu.setJobTp(jobTp);
                    dtu.setOcid(resultOcid);
                    dtus.add(dtu);
                }
                System.out.println("dtus size: " + dtus.size());
            }
        }
        try {
            // 이전 결과물 삭제
            dayTargetUserRepository.deleteAllInBatch();
            System.out.println("all delelte");
            int frame = 100;
            int dtuMax = dtus.size();
            for(int i=0; i<dtuMax; i+=frame) {
                List<DayTargetUser> slot = dtus.subList(i, i+frame);
                // 결과를 모두 저장
                List<DayTargetUser> dayTargetUsers = dayTargetUserRepository.saveAll(slot);
                System.out.println("index: "+ i);
                System.out.println("update dayTargetUsers:" + dayTargetUsers.size());
            }
        } catch (Exception e) {

        } finally {
            System.out.println("nameSlot size:" + nameSlots.stream().map((r)-> r.size()).reduce((r,w) -> r+w).get());
        }
        return dtus.size();
    }

    /**
     * [open api]
     * 동시접속자 조회
     * @return long
     */
    public long gthrCurrentUser () {
        nexonOpenApiService.keyLoading();
        List<Long> jobCnt = new ArrayList<Long>();
        ZonedDateTime koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        String hsDttm = koreaTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시"));
        // 직업별 조회 0~9
        for (long j=0; j<10; j++) {
            System.out.println("job start:"+j);
            HttpClient client = HttpClient.newHttpClient();
            // 수집대상목록 db select
            List<String> ocids = dayTargetUserRepository.findByJobTp(j).stream().map((r)->r.getOcid()).collect(Collectors.toList());
            System.out.println("ocids cnt:" + ocids.size());
            if (ocids.size() == 0) ocids = new ArrayList<>();

            long cnt = 0L; // 접속자수
            int frame = 20; // 100개씩 open api 조회
            int max = ocids.size();
            List<BaramUserBasicInfoDTO> allBasicInfos = new ArrayList<BaramUserBasicInfoDTO>();
            // open api 반복 조회
            for(int i=0; i<=max-1; i+=frame) {
                List<String> targetOcids = ocids.subList(i, max-1 < i+frame ? max-1 : i+frame);
                // frame 크기 만큼 리스트 분할 조회
                List<BaramUserBasicInfoDTO> basicInfos = nexonOpenApiService.retvBasicInfoOpenApi(client, targetOcids);
                System.out.println("basicInfos cnt:"+ basicInfos.size());
                allBasicInfos.addAll(basicInfos);
            }
            // 로그인 여부 판별
            for (BaramUserBasicInfoDTO basicInfo : allBasicInfos) {
                String lastLoginStr = basicInfo.getCharacter_date_last_login();
                String lastLogoutStr = basicInfo.getCharacter_date_last_logout();
                if (lastLoginStr != null && lastLogoutStr != null) {
                    // 로그인 여부 판별
                    if(lastLoginStr.compareTo(lastLogoutStr) > 0) {
                        cnt++;
                    }
                }
            }
            jobCnt.add(cnt);
        }
        
        if (jobCnt.size() > 0 && hsDttm != null) {
            try {
                for (int i=0; i<jobCnt.size(); i++) {
                    TimeHistory timeHistory = new TimeHistory();
                    timeHistory.setAccCnt(jobCnt.get(i));
                    timeHistory.setHsDttm(hsDttm);
                    timeHistory.setJobTp(Long.valueOf(i));
                    timeHistoryRepository.save(timeHistory);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jobCnt.stream().reduce((r,w) -> r+w).get();
    } 
}
