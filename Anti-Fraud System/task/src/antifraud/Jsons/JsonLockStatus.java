package antifraud.Jsons;

import antifraud.Status.Status;

public class JsonLockStatus {

    public String status;

    public JsonLockStatus(JsonChangeUserStatusObject object){
        this.status = Status.getDisableStatus(object);
    }

}
