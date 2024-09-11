package info.ggamt.gest.domain.baram.macro;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 매크로 유저 접속여부 배치 기록 */
@Entity
@Table(name = "br_macro_batch_hist_tb")
public class MacroBatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String batchDttm;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBatchDttm() {
        return batchDttm;
    }
    public void setBatchDttm(String batchDttm) {
        this.batchDttm = batchDttm;
    }
}
