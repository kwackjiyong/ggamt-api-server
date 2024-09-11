package info.ggamt.gest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import info.ggamt.gest.domain.baram.macro.MacroMLive;
import info.ggamt.gest.domain.baram.macro.MacroUser;
import info.ggamt.gest.dto.baram.BaramHistoryDTO;
import info.ggamt.gest.dto.baram.macro.BaramMacroCurrentDTO;
import info.ggamt.gest.service.baram.BaramMacroService;
import info.ggamt.gest.service.baram.BaramService;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/api/baram")
public class BaramRetvController {

    private final String ocidKey = "fy45h6b0-skf23m0b54hj76";
    
    @Autowired
    private BaramService baramService;
    
    @Autowired
    private BaramMacroService baramMacroService;


    /**
     * 유저 접속자수 실시간 정보 조회
     * @return dayHistoryList, timeHistoryList
     */
    @GetMapping
    public ResponseEntity<BaramHistoryDTO> getAllBaramHistory() {
        BaramHistoryDTO result = baramService.getAllBaramHistory();
        return ResponseEntity.ok().body(result);
    }
    
    /**
     * 매크로 유저 현황 실시간 정보 조회
     * @return
     */
    @GetMapping("/macro")
    public ResponseEntity<BaramMacroCurrentDTO> getBaramMacroInfos() {
        BaramMacroCurrentDTO result = new BaramMacroCurrentDTO();
        // List<MacroUser> macroUsers = baramMacroService.getMacroUsers();
        List<MacroMLive> macroUsers = baramMacroService.getMacroMLive();
        String gthrDttm = baramMacroService.getMacroBatchHistory();
        result.setGthrDttm(gthrDttm);
        if (macroUsers.size() > 0) {
            result.setMacroInfos(macroUsers);
        }
        return ResponseEntity.ok().body(result);
    }
    // @GetMapping("/ocid")
    // public ResponseEntity<String> getAllOcid() {
    //     List<String> result = BaramService.getAllOcids();
    //     System.out.println("result:"+ result.size());
    //     return ResponseEntity.ok().body("result:"+ result.size());
    // }

    // @PostMapping("/ocid/update")
    // public ResponseEntity<String> gthrTodayOcids() {
    //     long cnt = BaramService.gthrTodayOcids();
    //     System.out.println("result cnt:" + cnt);
    //     return ResponseEntity.ok().body("result:"+ cnt);
    // }

    // @PostMapping("/history/update")
    // public ResponseEntity<String> gthrCurrentUser() {
    //     long cnt = BaramService.gthrCurrentUser();
    //     System.out.println("result cnt:" + cnt);
    //     return ResponseEntity.ok().body("result:"+ cnt);
    // }
    

    @PostMapping("/macro/add")
    public ResponseEntity<String> addMacroUser(@RequestBody MacroUser macroUser) {
        if (macroUser != null) {
            if (macroUser.getDescription() != null && macroUser.getUserName() != null && ocidKey.equals(macroUser.getOcid())) {
                baramMacroService.insertMacroUser(macroUser.getUserName(), macroUser.getDescription());
            } else {
                System.out.println("param error");
            }
        }
        return ResponseEntity.ok().body("macro user add");
    }

    @PostMapping("/macro/update")
    public ResponseEntity<String> gthrBaramMacroInfos(@RequestBody MacroUser macroUser) {
        if (ocidKey.equals(macroUser.getOcid())) {
            baramMacroService.gthrMacroMLive();
        }
        return ResponseEntity.ok().body("updateMacroLive");
    }
}
