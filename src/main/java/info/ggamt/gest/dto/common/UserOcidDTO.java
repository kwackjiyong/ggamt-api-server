package info.ggamt.gest.dto.common;

public class UserOcidDTO {
    private String ocid;
    private ErrorDTO error;
    public ErrorDTO getError() {
        return error;
    }
    public void setError(ErrorDTO error) {
        this.error = error;
    }
    public String getOcid() {
        return this.ocid;
    }
    public void setOcid(String ocid) {
        this.ocid = ocid;
    }
}
