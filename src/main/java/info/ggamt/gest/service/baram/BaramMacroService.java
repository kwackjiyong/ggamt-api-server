package info.ggamt.gest.service.baram;

import java.net.http.HttpClient;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.ggamt.gest.domain.baram.macro.MacroBatchHistory;
import info.ggamt.gest.domain.baram.macro.MacroMLive;
import info.ggamt.gest.domain.baram.macro.MacroUser;
import info.ggamt.gest.dto.baram.BaramUserBasicInfoDTO;
import info.ggamt.gest.repository.baram.macro.MacroBatchHistoryRepository;
import info.ggamt.gest.repository.baram.macro.MacroMLiveRepository;
import info.ggamt.gest.repository.baram.macro.MacroUserRepository;
import info.ggamt.gest.util.util;


@Service
@Transactional
public class BaramMacroService {

    @Autowired
    private MacroBatchHistoryRepository macroBatchRepository;

    @Autowired
    private MacroMLiveRepository macroMLiveRepository;

    @Autowired
    private MacroUserRepository macroUserRepository;

    @Autowired
    private NexonOpenApiService nexonOpenApiService;
    
    @Transactional(readOnly = true)
    /** 매크로 유저 실시간현황 최근 배치시간 조회 */
    public String getMacroBatchHistory () {
        Optional<MacroBatchHistory> result = macroBatchRepository.findFirstByOrderByIdDesc();
        if (result.isPresent()) {
            return result.get().getBatchDttm();
        }
        return "";
    }

    @Transactional(readOnly = true)
    /** 매크로 실시간현황 가져오기 */
    public List<MacroMLive> getMacroMLive () {
       return macroMLiveRepository.findAll();
    }

    @Transactional(readOnly = true)
    /** 매크로 유저 목록 가져오기 */
    public List<MacroUser> getMacroUsers () {
        return macroUserRepository.findAll();
    }

    /** 신규 매크로 유저 추가 */
    public MacroUser insertMacroUser (String userName, String desc) {
        System.out.println("userName:"+ userName);
        System.out.println("desc:"+ desc);
        long cnt = macroUserRepository.countByUserName(userName);
        System.out.println("cnt:"+ cnt);
        // 이미 존재하면 이탈처리
        if (cnt > 0) {
            return null;
        }
        try {
            nexonOpenApiService.keyLoading();
            MacroUser macroUser = new MacroUser();
            macroUser.setIsDelete(false);
            macroUser.setIsHalt(false);
            macroUser.setUserName(userName);
            macroUser.setDescription(desc);
            HttpClient client = HttpClient.newHttpClient();
            List<String> userNames = new ArrayList<String>();
            userNames.add(util.buildQueryString(userName));
            List<String> ocids = nexonOpenApiService.retvOcidOpenApi(client, userNames);

            if(ocids.size() > 0) {
                System.out.println("ocids:" + ocids.size());
                macroUser.setOcid(ocids.get(0));
                System.out.println("ocid:" + macroUser.getOcid());
                return macroUserRepository.save(macroUser);
            } else {
                System.out.println("ocid error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 매크로 실시간현황 업데이트 */
    public void gthrMacroMLive () {
        // 모든 매크로 유저 조회
        List<MacroUser> macroUsers = macroUserRepository.findAll();
        if (macroUsers.size() > 0) {
            List<String> ocids = macroUsers.stream().map((r) -> r.getOcid()).collect(Collectors.toList());
            nexonOpenApiService.keyLoading();
            List<BaramUserBasicInfoDTO> allBasicInfos = nexonOpenApiService.retvBasicInfoOpenApi(HttpClient.newHttpClient(), ocids);
            
            List<MacroMLive> macroLives = new ArrayList<>();
            for (BaramUserBasicInfoDTO basicInfo : allBasicInfos) {
                String lastLoginStr = basicInfo.getCharacter_date_last_login();
                String lastLogoutStr = basicInfo.getCharacter_date_last_logout();
                MacroMLive macroLive = new MacroMLive();
                macroLive.setUserName(basicInfo.getCharacter_name());
                MacroUser macroUser = macroUsers.stream().filter((r)-> r.getUserName().equals(basicInfo.getCharacter_name())).findFirst().get();
                if(macroUser != null) {
                    macroLive.setDescription(macroUser.getDescription());
                }
                macroLive.setIsLive(false);
                if (lastLoginStr != null && lastLogoutStr != null) {
                    // 로그인 여부 판별
                    if(lastLoginStr.compareTo(lastLogoutStr) > 0) {
                        macroLive.setIsLive(true);
                    }
                }
                macroLives.add(macroLive);
            }

            // 모든 매크로 현황 제거
            macroMLiveRepository.deleteAll();
            macroMLiveRepository.saveAll(macroLives);
            // 현재 시간 가져오기 (서버 시간)
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            // 원하는 형식 지정 (yyyy-MM-dd HH:mm:ss)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 한국 시간으로 변환 후 형식에 맞춰서 시간 저장
            String formattedDate = now.format(formatter);
            MacroBatchHistory macroBatchHistory = new MacroBatchHistory();
            macroBatchHistory.setBatchDttm(formattedDate);
            macroBatchRepository.save(macroBatchHistory);
        }
    }

}   
