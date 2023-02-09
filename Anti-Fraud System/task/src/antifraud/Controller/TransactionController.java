package antifraud.Controller;

import antifraud.Card.StolenCard;
import antifraud.Ip.SuspiciousIp;
import antifraud.Jsons.JsonCardnumberObject;
import antifraud.Jsons.JsonFeedbackObject;
import antifraud.Jsons.JsonIpObject;
import antifraud.Services.CardService;
import antifraud.Services.IpService;
import antifraud.Services.TransactionService;
import antifraud.Transactions.TransactionObject;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {

    @PostMapping("/transaction")
    public static ResponseEntity<Object> checkTransaction(@RequestBody @Valid TransactionObject object) {
        return TransactionService.checkTransaction(object);
    }

    @PutMapping("/transaction")
    public static ResponseEntity<Object> addFeedbackForTransaction(@RequestBody @Valid @NotNull JsonFeedbackObject object){
        return TransactionService.addFeedbackForTransaction(object);
    }

    @PostMapping("/suspicious-ip")
    public static ResponseEntity<Object> addSuspiciousIp(@RequestBody @Valid JsonIpObject ip)
            throws JsonProcessingException {
        return IpService.addSuspiciousIp(ip);
    }

    @GetMapping("/suspicious-ip")
    public static List<SuspiciousIp> getSuspiciousIpList(){
        return IpService.getSuspiciousIpList();
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public static ResponseEntity<Object> deleteSuspiciousIp(@PathVariable @Valid String ip) throws JsonProcessingException {
        return IpService.deleteSuspiciousIp(ip);
    }

    @PostMapping("/stolencard")
    public static ResponseEntity<Object> addStolenCard(@RequestBody @Valid JsonCardnumberObject number) throws JsonProcessingException {
        return CardService.addStolenCard(number);
    }

    @DeleteMapping("/stolencard/{number}")
    public static ResponseEntity<Object> deleteStoleCard(@PathVariable @Valid String number) throws JsonProcessingException {
        return CardService.deleteStolenCard(number);
    }

    @GetMapping("/stolencard")
    public static List<StolenCard> getStolenCardList(){
        return CardService.getStolenCardList();
    }

    @GetMapping(value = {"/history/{number}"})
    public static ResponseEntity<Object> getTransactionHistoryList(@PathVariable String number){
        return TransactionService.getTransactionHistoryList(number);
    }
    @GetMapping(value = {"/history"})
    public static ResponseEntity<Object> getAllTransactionHistoryList(){
        return TransactionService.getAllTransactionHistoryList();
    }

}
