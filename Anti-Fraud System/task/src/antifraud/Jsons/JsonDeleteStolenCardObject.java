package antifraud.Jsons;

import antifraud.Status.Status;

public class JsonDeleteStolenCardObject {
    public String status;

    public JsonDeleteStolenCardObject(String number){
        this.status = Status.getDeleteCardNumber(number);
    }
}
