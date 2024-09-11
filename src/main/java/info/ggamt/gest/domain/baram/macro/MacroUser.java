package info.ggamt.gest.domain.baram.macro;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 매크로 유저 목록 */
@Entity
@Table(name = "br_macro_user_lst_tb")
public class MacroUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String description;
    private String ocid;
    private Boolean isHalt; // 검거 여부
    private Boolean isDelete; // 삭제 여부
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getOcid() {
        return ocid;
    }
    public void setOcid(String ocid) {
        this.ocid = ocid;
    }
    public Boolean getIsHalt() {
        return isHalt;
    }
    public void setIsHalt(Boolean isHalt) {
        this.isHalt = isHalt;
    }
    public Boolean getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
