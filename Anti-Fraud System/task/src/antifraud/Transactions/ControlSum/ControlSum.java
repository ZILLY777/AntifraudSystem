package antifraud.Transactions.ControlSum;


import antifraud.Status.TransactionStatus;
import antifraud.Transactions.TransactionObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ControlSum {
    @Getter
    private static long limitOne = 200;
    @Getter
    private static long limitTwo = 1500;

    private static void increaseLimitOne(long transactionValue){
        limitOne = (long) Math.ceil(0.8 * limitOne + 0.2 * transactionValue);
    }
    private static void decreaseLimitOne(long transactionValue){
        limitOne = (long) Math.ceil(0.8 * limitOne - 0.2 * transactionValue);
    }
    private static void increaseLimitTwo(long transactionValue){
        limitTwo = (long) Math.ceil(0.8 * limitTwo + 0.2 * transactionValue);
    }
    private static void decreaseLimitTwo(long transactionValue){
        limitTwo = (long) Math.ceil(0.8 * limitTwo - 0.2 * transactionValue);
    }
    public static void limitManipulation(TransactionObject object){
        if (object.feedback.equals(TransactionStatus.getAllowed())){
            increaseLimitOne(object.amount);
            if(object.result.equals(TransactionStatus.getProhibited())){
                increaseLimitTwo(object.amount);
            }
        } else if (object.feedback.equals(TransactionStatus.getManual_processing())) {
            if(object.result.equals(TransactionStatus.getProhibited())){
                increaseLimitTwo(object.amount);
            } else {
                decreaseLimitOne(object.amount);
            }
        }else{
            decreaseLimitTwo(object.amount);
            if(object.result.equals(TransactionStatus.getAllowed())){
                decreaseLimitOne(object.amount);

        }
    }

}}
