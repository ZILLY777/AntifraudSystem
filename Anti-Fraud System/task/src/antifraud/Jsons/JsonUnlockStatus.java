package antifraud.Jsons;

import antifraud.Status.Status;

public class JsonUnlockStatus {

    public String status;

    public JsonUnlockStatus(JsonChangeUserStatusObject object){
        this.status = Status.getEnableStatus(object);
    }

}
