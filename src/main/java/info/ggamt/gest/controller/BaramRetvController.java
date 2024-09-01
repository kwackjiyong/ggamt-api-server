package info.ggamt.gest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import info.ggamt.gest.dto.baram.BaramHistoryDTO;
import info.ggamt.gest.service.baram.BaramService;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;


@Controller
@RequestMapping("/api/baram")
public class BaramRetvController {

    @Autowired
    private BaramService BaramService;


    /**
     * 유저 접속자수 실시간 정보 조회
     * @return dayHistoryList, timeHistoryList
     */
    @GetMapping
    public ResponseEntity<BaramHistoryDTO> getAllBaramHistory() {
        BaramHistoryDTO result = BaramService.getAllBaramHistory();
        return ResponseEntity.ok().body(result);
    }
    
    @GetMapping("/ocid")
    public ResponseEntity<String> getAllOcid() {
        List<String> result = BaramService.getAllOcids();
        System.out.println("result:"+ result.size());
        return ResponseEntity.ok().body("result:"+ result.size());
    }

    @PostMapping("/ocid/update")
    public ResponseEntity<String> gthrTodayOcids() {
        long cnt = BaramService.gthrTodayOcids();
        System.out.println("result cnt:" + cnt);
        return ResponseEntity.ok().body("result:"+ cnt);
    }

    @PostMapping("/history/update")
    public ResponseEntity<String> gthrCurrentUser() {
        long cnt = BaramService.gthrCurrentUser();
        System.out.println("result cnt:" + cnt);
        return ResponseEntity.ok().body("result:"+ cnt);
    }
    
}
