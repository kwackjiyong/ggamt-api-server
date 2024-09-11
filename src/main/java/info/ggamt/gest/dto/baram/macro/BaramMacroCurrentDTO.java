package info.ggamt.gest.dto.baram.macro;

import java.util.List;

import info.ggamt.gest.domain.baram.macro.MacroMLive;

public class BaramMacroCurrentDTO {
    String gthrDttm; // 수집시간
    List<MacroMLive> macroInfos; // 매크로 현황정보
    public String getGthrDttm() {
        return gthrDttm;
    }
    public void setGthrDttm(String gthrDttm) {
        this.gthrDttm = gthrDttm;
    }
    public List<MacroMLive> getMacroInfos() {
        return macroInfos;
    }
    public void setMacroInfos(List<MacroMLive> macroInfos) {
        this.macroInfos = macroInfos;
    }
}
