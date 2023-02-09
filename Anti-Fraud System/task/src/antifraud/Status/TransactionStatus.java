package antifraud.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class TransactionStatus {
    @Getter
    private static final String allowed  = "ALLOWED";
    @Getter
    private static final String manual_processing = "MANUAL_PROCESSING";
    @Getter
    private static final String prohibited = "PROHIBITED";
}
