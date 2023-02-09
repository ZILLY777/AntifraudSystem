package antifraud.Services;

import antifraud.Exceptions.NotFoundException;
import antifraud.Exceptions.UnprocessableEntityException;
import antifraud.Ip.SuspiciousIpRepository;
import antifraud.Jsons.JsonFeedbackObject;
import antifraud.Jsons.JsonTransactionOutputObject;
import antifraud.Status.TransactionStatus;
import antifraud.Transactions.ControlSum.ControlSum;
import antifraud.Transactions.TransactionObject;
import antifraud.Transactions.TransactionRepository;
import antifraud.Validators.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class TransactionService {

    private static ObjectMapper mapper;
    private static TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(ObjectMapper mapper, TransactionRepository transactionRepository){
       TransactionService.mapper = mapper;
       TransactionService.transactionRepository = transactionRepository;
    }

    public static ResponseEntity<Object>  checkTransaction(TransactionObject object){
        JsonTransactionOutputObject outputObject = new JsonTransactionOutputObject();
        ArrayList<String> errors = new ArrayList<>();
        int errorSum = 0;
        try{

            System.out.println(transactionRepository.findAll());
            IPv4ValidatorApache.isValid(object.ip);
            CreditCardValidator.isValid(object.number);
            RegionValidator.isValid(object.region);
            DateValidator.isValid(object.date);

            //TransactionValidator.statusValidation(object);
            //TransactionValidator.processableValidation(object);

            //then data format is invalid.
            errorSum += TransactionValidator.getAllValidationSum(object);
            if (errorSum==0){
                outputObject.result = TransactionStatus.getAllowed();
                object.result =TransactionStatus.getAllowed();
                object.feedback = "";
                outputObject.info = "none";
            } else if (errorSum>0 & errorSum<9) {
                outputObject.result = TransactionStatus.getManual_processing();
                object.result = TransactionStatus.getManual_processing();
                object.feedback = "";
                errors  = TransactionValidator.getAllError(object);
                outputObject.info = String.join(", ", errors);
            }else{
                outputObject.result = TransactionStatus.getProhibited();
                object.result = TransactionStatus.getProhibited();
                object.feedback = "";
                errors  = TransactionValidator.getAllError(object);
                outputObject.info = String.join(", ", errors);
            }
            transactionRepository.save(object);

            return new ResponseEntity<>(outputObject, HttpStatus.OK);
        }catch (NullPointerException  | ValidatorException |ParseException ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }//catch (UnprocessableEntityException e) {
            //return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        //}

    }

    public static ResponseEntity<Object> addFeedbackForTransaction(JsonFeedbackObject object) {
        System.out.println("ku");
        System.out.println(transactionRepository.findTransactionObjectByTransactionId(object.transactionId).feedback);
        if(transactionRepository.findTransactionObjectByTransactionId(object.transactionId)!=null) {
            try {
                TransactionValidator.processableValidation(object);
                TransactionValidator.transactionHistoryValidation(object);
                TransactionValidator.statusValidation(object);
                System.out.println("warn");
                System.out.println(Objects.equals(transactionRepository.findTransactionObjectByTransactionId(object.transactionId)
                        .feedback, ""));
                if (!Objects.equals(transactionRepository.findTransactionObjectByTransactionId(object.transactionId)
                        .feedback, "")) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }

                TransactionObject transactionObject = transactionRepository.findTransactionObjectByTransactionId(
                        object.transactionId);
                transactionObject.feedback = object.feedback;

                TransactionObject finalTransactionObject = transactionRepository.findTransactionObjectByTransactionId(
                        object.transactionId);
                //transactionRepository.updateTransactionObjectFeedback(object.feedback, object.transactionId);
                transactionRepository.save(transactionObject);
                ControlSum.limitManipulation(finalTransactionObject);
                return new ResponseEntity<>(finalTransactionObject, HttpStatus.OK);
            } catch (ValidatorException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (UnprocessableEntityException | HttpServerErrorException.InternalServerError e) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            } catch (NotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    public static ResponseEntity<Object> getTransactionHistoryList(String number) {
        if(number!=null){
            try{
                CreditCardValidator.isValid(number);
                TransactionValidator.transactionHistoryValidation(number);
                return new ResponseEntity<>(transactionRepository.findAllByNumber(number), HttpStatus.OK);
            } catch (NotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (ValidatorException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }else{
            List<Object> list = new ArrayList<>();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }

    }
    public static ResponseEntity<Object> getAllTransactionHistoryList() {
        return new ResponseEntity<>(transactionRepository.findAll(), HttpStatus.OK);

    }
}
