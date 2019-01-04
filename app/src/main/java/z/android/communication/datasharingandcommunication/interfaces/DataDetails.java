package z.android.communication.datasharingandcommunication.interfaces;

import java.io.Serializable;

public class DataDetails implements Serializable {
    private String type,details;

    public void setDetails(String details) {
        this.details = details;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public String getType() {
        return type;
    }
}
