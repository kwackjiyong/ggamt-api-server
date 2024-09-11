package info.ggamt.gest.domain.baram.macro;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 매크로 유저 접속여부 */
@Entity
@Table(name = "br_macro_min_live_tb")
public class MacroMLive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String description;
    private Boolean isLive;
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
    public Boolean getIsLive() {
        return isLive;
    }
    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }
}
