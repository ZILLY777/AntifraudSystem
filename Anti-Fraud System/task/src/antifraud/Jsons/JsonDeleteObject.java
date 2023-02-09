package antifraud.Jsons;

import antifraud.Status.Status;
import antifraud.User.User;

public class JsonDeleteObject {
    public String username;
    public String status;

    public JsonDeleteObject(String username){
        this.username = username;
        this.status = Status.getDeleteStatus();
    }



}
