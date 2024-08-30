package info.ggamt.gest.domain.baram;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 일간 접속자 수집기록 */
@Entity
@Table(name = "br_d_hist_tb")
public class DayHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hsDt;
    private Long jobTp;
    private Long accCnt;
    private Date regDttm;
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
    public void setHsDt(String hsDt) {
        this.hsDt = hsDt;
    }
    public Long getJobTp() {
        return jobTp;
    }
    public void setJobTp(Long jobTp) {
        this.jobTp = jobTp;
    }
    public Long getAccCnt() {
        return accCnt;
    }
    public void setAccCnt(Long accCnt) {
        this.accCnt = accCnt;
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
