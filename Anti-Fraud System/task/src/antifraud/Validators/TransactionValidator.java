package antifraud.Validators;

import antifraud.Card.StolenCardRepository;
import antifraud.Exceptions.NotFoundException;
import antifraud.Exceptions.UnprocessableEntityException;
import antifraud.Ip.SuspiciousIpRepository;
import antifraud.Jsons.JsonFeedbackObject;
import antifraud.Status.TransactionStatus;
import antifraud.Transactions.ControlSum.ControlSum;
import antifraud.Transactions.TransactionObject;
import antifraud.Transactions.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class TransactionValidator {
    private static SuspiciousIpRepository suspiciousIpRepository;
    private static StolenCardRepository stolenCardRepository;
    private static TransactionRepository transactionRepository;
    private static ObjectMapper mapper;

    @Autowired
    public TransactionValidator(SuspiciousIpRepository suspiciousIpRepository,
                                StolenCardRepository stolenCardRepository, ObjectMapper mapper,
                                TransactionRepository transactionRepository){
        TransactionValidator.stolenCardRepository = stolenCardRepository;
        TransactionValidator.suspiciousIpRepository = suspiciousIpRepository;
        TransactionValidator.mapper = mapper;
        TransactionValidator.transactionRepository = transactionRepository;
    }

    public static int cardValidation(TransactionObject json) {
        if (stolenCardRepository.findStolenCardByNumber(json.number) != null) {
            return 9;
        }else {
            return 0;
        }
    }

    public static int ipValidation(TransactionObject json) {
        if (suspiciousIpRepository.findSuspiciousIpByIp(json.ip) != null) {
            return 9;
        }else {
            return 0;
        }
    }


    public static int regionCorrelationValidation(TransactionObject json){
        LocalDateTime beforeDate = LocalDateTime.parse(json.date);
        LocalDateTime afterDate = beforeDate.minusHours(1);
        System.out.println(beforeDate);
        System.out.println(afterDate);
        long result = transactionRepository.countByDistinctRegionAndDateBetween(json.number,
                afterDate.toString(),beforeDate.toString(), json.region);
        System.out.println(result);
        if (result<2){
            return 0;
        } else if (result==2 | result==3) {
            return 1;
        } else {
            return 9;
        }
    }

    public static int ipCorrelationValidation(TransactionObject json){
        LocalDateTime beforeDate = LocalDateTime.parse(json.date);
        LocalDateTime afterDate = beforeDate.minusHours(1);
        System.out.println(beforeDate);
        System.out.println(afterDate);
        long result = transactionRepository.countByDistinctIpAndDateBetween(json.number,
                afterDate.toString(),beforeDate.toString(), json.ip);
        System.out.println(result);
        if (result<2){
            return 0;
        } else if (result==2 | result==3) {
            return 1;
        } else {
            return 9;
        }
    }

    public static int amountValidation(TransactionObject json) throws ValidatorException {
        try{
            System.out.println("limits");
            System.out.println(ControlSum.getLimitOne());
            System.out.println(ControlSum.getLimitTwo());
            if((json.amount<= ControlSum.getLimitOne() & json.amount>0 | json.amount==1000) ){
                return 0;
            } else if(json.amount<=ControlSum.getLimitTwo() && json.amount>ControlSum.getLimitOne() ){
                return 1;
            } else if(json.amount>ControlSum.getLimitTwo()) {
                return 9;
            } else{
                throw new ValidatorException();
            }
        } catch (Exception e) {
            throw new ValidatorException();
        }
    }
     public static int getAllValidationSum(TransactionObject object) throws ValidatorException {
        return
         TransactionValidator.amountValidation(object)+
         TransactionValidator.cardValidation(object)+
         TransactionValidator.ipValidation(object)+
         TransactionValidator.ipCorrelationValidation(object)+
         TransactionValidator.regionCorrelationValidation(object);
     }

     public static ArrayList<String> getAllError(TransactionObject object) throws ValidatorException {
        ArrayList<String> errors = new ArrayList<>();
        if (TransactionValidator.amountValidation(object)!=0){
            errors.add("amount");
        }
        if (TransactionValidator.cardValidation(object)!=0){
             errors.add("card-number");
        }
        if (TransactionValidator.ipValidation(object)!=0){
             errors.add("ip");
        }
        if (TransactionValidator.ipCorrelationValidation(object)!=0){
             errors.add("ip-correlation");
        }
        if (TransactionValidator.regionCorrelationValidation(object)!=0){
             errors.add("region-correlation");
        }

        return errors;
     }

     public static void statusValidation(JsonFeedbackObject object) throws ValidatorException{
        if (!object.feedback.equals(TransactionStatus.getAllowed()) &
                !object.feedback.equals(TransactionStatus.getProhibited()) &
                !object.feedback.equals(TransactionStatus.getManual_processing())){
            throw new ValidatorException();
        }
     }
     public static void processableValidation(JsonFeedbackObject object) throws UnprocessableEntityException {
        System.out.println((object.feedback.equals( transactionRepository.findTransactionObjectByTransactionId(object
                .transactionId).result)));
        if (object.feedback.equals( transactionRepository.findTransactionObjectByTransactionId(object
                .transactionId).result)|transactionRepository.findTransactionObjectByTransactionId(object
                .transactionId).result==null ){
            throw new UnprocessableEntityException();
        }
     }
    public static void processableValidation(TransactionObject object) throws UnprocessableEntityException {
        System.out.println((object.feedback.equals( transactionRepository.findTransactionObjectByTransactionId(object
                .getTransactionId()).result)));
        if (object.feedback.equals( transactionRepository.findTransactionObjectByTransactionId(object
                .getTransactionId()).result)){
            throw new UnprocessableEntityException();
        }
    }
     public static void transactionHistoryValidation(JsonFeedbackObject object) throws NotFoundException {
        if(transactionRepository.findTransactionObjectByTransactionId(object.transactionId)==null){
            throw new NotFoundException();
        }
     }
    public static void transactionHistoryValidation(String number) throws NotFoundException {

        if(transactionRepository.findTransactionObjectByNumber(number).isEmpty()){
            throw new NotFoundException();
        }
    }
}
