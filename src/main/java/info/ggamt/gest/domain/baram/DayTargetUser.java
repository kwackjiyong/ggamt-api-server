package info.ggamt.gest.domain.baram;

import java.sql.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
/**
 * 당일 수집 대상자
 */
@Entity
@Table(name = "gest.br_d_retv_user_lst_tb")
public class DayTargetUser {

    @Id
    private Long id;
    /** 날짜구분 */
    private String hsDt;
    /** ocid */
    private String ocid;
    /** 직업구분 */
    private Long jobTp;
    /** 등록일시 */
    private Date regDttm;
    /** 수정일시 */
    private Date modDttm;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHsDt() {
        return hsDt;
    }
    public String getOcid() {
        return ocid;
    }
    public void setOcid(String ocid) {
        this.ocid = ocid;
    }
    public void setHsDt(String hsDt) {
        this.hsDt = hsDt;
    }
    public Long getJobTp() {
        return jobTp;
    }
    public void setJobTp(Long jobTp) {
        this.jobTp = jobTp;
    }
    public Date getRegDttm() {
        return regDttm;
    }
    public void setRegDttm(Date regDttm) {
        this.regDttm = regDttm;
    }
    public Date getModDttm() {
        return modDttm;
    }
    public void setModDttm(Date modDttm) {
        this.modDttm = modDttm;
    }
}
