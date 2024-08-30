package info.ggamt.gest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import info.ggamt.gest.dto.baram.BaramHistoryDTO;
import info.ggamt.gest.service.baram.BaramService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@Controller
@RequestMapping("/api/baram")
public class BaramRetvController {

    @Autowired
    private BaramService BaramService;

    @GetMapping
    public BaramHistoryDTO getAllUsers() {
        return BaramService.getAllBaramHistory();
    }

    
    @GetMapping("/ocid")
    public ResponseEntity<String> getAllOcid() {
        List<String> result = BaramService.getAllOcids();
        System.out.println("result:"+ result.size());
        return ResponseEntity.ok().body("result:"+ result.size());
    }

    @PostMapping("/ocid/update")
    public String gthrTodayOcids() {
        long cnt = BaramService.gthrTodayOcids();
        System.out.println("result cnt:" + cnt);
        return String.valueOf(cnt);
    }
    
}
