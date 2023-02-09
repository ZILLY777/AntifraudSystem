package antifraud.Jsons;

import javax.validation.constraints.NotNull;

public class JsonFeedbackObject {
    @NotNull
    public long transactionId;
    public String feedback;

}
