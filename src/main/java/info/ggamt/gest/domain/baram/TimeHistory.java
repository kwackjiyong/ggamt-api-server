package info.ggamt.gest.domain.baram;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 실시간 동시 접속자 수집기록 */
@Entity
@Table(name = "gest.br_t_hist_tb")
public class TimeHistory {
    @Id
    private Long id;
    private String hsDttm;
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
    public String getHsDttm() {
        return hsDttm;
    }
    public void setHsDttm(String hsDttm) {
        this.hsDttm = hsDttm;
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
