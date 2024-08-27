package info.ggamt.gest.service.baram;

import org.springframework.beans.factory.annotation.Autowired;

import info.ggamt.gest.domain.baram.DayTargetUser;
import info.ggamt.gest.dto.baram.BaramHistoryDTO;
import info.ggamt.gest.repository.baram.DayTargetUserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class BaramService {
    @Autowired
    DayTargetUserRepository dayTargetUserRepository;

    @Autowired
    NexonOpenApiService nexonOpenApiService;

    public BaramHistoryDTO getAllBaramHistory () {
        BaramHistoryDTO bh = new BaramHistoryDTO();
        return bh;
    }

    /**
     * gthrTodayOcids
     * @description 당일 수집 대상자들의 ocid를 수집
     * @apiNote 각 직업별 랭킹 10,000위 (총 100,000명 조회)
     */
    public long gthrTodayOcids () {
        long result = 0L;
        List<List<String>> nameSlots = nexonOpenApiService.parseGthrUserName(); // 홈페이지 html 파싱 > 유저명 조회
        String hsDt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int max = nameSlots.size(); // 보통 10
        List<DayTargetUser> dtus = new ArrayList<DayTargetUser>();
        for (int jobIndex=0; jobIndex < max; jobIndex++) {
            List<String> nameSlot = nameSlots.get(jobIndex);
            List<String> resultOcids = nexonOpenApiService.retvOcidOpenApi(nameSlot);
            Long jobTp = Long.valueOf(jobIndex);
            for(String resultOcid : resultOcids) {
                // 테이블 insert 규격에 맞게 세팅
                DayTargetUser dtu = new DayTargetUser();
                dtu.setHsDt(hsDt);
                dtu.setJobTp(jobTp);
                dtu.setOcid(resultOcid);
            }
        }
        // 결과를 모두 저장
        dayTargetUserRepository.saveAll(dtus);

        return result;
    }
}
