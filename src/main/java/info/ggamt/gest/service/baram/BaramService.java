package info.ggamt.gest.service.baram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.ggamt.gest.domain.baram.DayTargetUser;
import info.ggamt.gest.dto.baram.BaramHistoryDTO;
import info.ggamt.gest.repository.baram.DayTargetUserRepository;

import java.time.LocalDate;
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
    NexonOpenApiService nexonOpenApiService;
    
    @Transactional(readOnly = true)
    public BaramHistoryDTO getAllBaramHistory () {
        BaramHistoryDTO bh = new BaramHistoryDTO();
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
            int frame = 100;
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
}
