package antifraud.Jsons;

import antifraud.Status.Status;
import lombok.AllArgsConstructor;


public class JsonDeleteIpObject {
    public String status;

    public JsonDeleteIpObject(String ip){
        this.status = Status.getDeleteIpStatus(ip);
    }

}
