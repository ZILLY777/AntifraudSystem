package antifraud.Services;

import antifraud.Card.StolenCard;
import antifraud.Card.StolenCardRepository;
import antifraud.Ip.SuspiciousIp;
import antifraud.Ip.SuspiciousIpRepository;
import antifraud.Jsons.JsonCardnumberObject;
import antifraud.Jsons.JsonDeleteIpObject;
import antifraud.Jsons.JsonDeleteStolenCardObject;
import antifraud.Validators.CreditCardValidator;
import antifraud.Validators.IPv4ValidatorApache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private static ObjectMapper mapper;
    private static StolenCardRepository stolenCardRepository;

    @Autowired
    public CardService(StolenCardRepository stolenCardRepository, ObjectMapper mapper){
        CardService.mapper = mapper;
        CardService.stolenCardRepository = stolenCardRepository;
    }

    public static ResponseEntity<Object> addStolenCard(JsonCardnumberObject object) throws JsonProcessingException {
        if (stolenCardRepository.findStolenCardByNumber(object.number)==null){
            try {
                CreditCardValidator.isValid(object.number);
                StolenCard stolencard = new StolenCard(object.number);
                stolenCardRepository.save(stolencard);
                return new ResponseEntity<>(mapper.writeValueAsString(stolenCardRepository
                        .findStolenCardByNumber(object.number)), HttpStatus.OK);
            } catch (ValidatorException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public static ResponseEntity<Object> deleteStolenCard(String number) throws JsonProcessingException{
        try {
            CreditCardValidator.isValid(number);
            if(stolenCardRepository.findStolenCardByNumber(number)!=null){
                stolenCardRepository.deleteStolenCardByNumber(number);
                return new ResponseEntity<>(mapper.writeValueAsString(new JsonDeleteStolenCardObject(number)
                ), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public static List<StolenCard> getStolenCardList(){
        return stolenCardRepository.findAll();
    }

}
