package antifraud.Status;

import antifraud.Jsons.JsonChangeUserStatusObject;

import java.text.MessageFormat;

public class Status {

    public static String getDeleteStatus(){
        return "Deleted successfully!";
    }

    public static String getEnableStatus(JsonChangeUserStatusObject object){
        return MessageFormat.format("User {0} unlocked!",  object.username);
    }

    public static String getDisableStatus(JsonChangeUserStatusObject object){
        return MessageFormat.format("User {0} locked!",  object.username);
    }

    public static String getDeleteIpStatus(String ip){
        return MessageFormat.format("IP {0} successfully removed!", ip);
    }

    public static String getDeleteCardNumber(String number){
        return MessageFormat.format("Card {0} successfully removed!", number);
    }

}
