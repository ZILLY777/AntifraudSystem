package antifraud.Services;

import antifraud.Ip.SuspiciousIp;
import antifraud.Ip.SuspiciousIpRepository;
import antifraud.Jsons.JsonDeleteIpObject;
import antifraud.Jsons.JsonIpObject;
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
public class IpService {

    private static ObjectMapper mapper;

    private static SuspiciousIpRepository suspiciousIpRepository;

    @Autowired
    public IpService(ObjectMapper mapper, SuspiciousIpRepository suspiciousIpRepository){
        IpService.mapper = mapper;
        IpService.suspiciousIpRepository = suspiciousIpRepository;
    }

    public static ResponseEntity<Object> addSuspiciousIp(JsonIpObject object) throws JsonProcessingException {
        if (suspiciousIpRepository.findSuspiciousIpByIp(object.ip)==null){
            try{
                IPv4ValidatorApache.isValid(object.ip);
                SuspiciousIp suspiciousIp = new SuspiciousIp(object.ip);
                suspiciousIpRepository.save(suspiciousIp);
                return new ResponseEntity<>(mapper.writeValueAsString(suspiciousIpRepository
                        .findSuspiciousIpByIp(object.ip)), HttpStatus.OK);
            }catch (ValidatorException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public static ResponseEntity<Object> deleteSuspiciousIp(String ip) throws JsonProcessingException {
        try {
            IPv4ValidatorApache.isValid(ip);
            if(suspiciousIpRepository.findSuspiciousIpByIp(ip)!=null){
                suspiciousIpRepository.deleteSuspiciousIpByIp(ip);
                return new ResponseEntity<>(mapper.writeValueAsString(new JsonDeleteIpObject(ip)), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
       } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public static List<SuspiciousIp> getSuspiciousIpList(){
        return suspiciousIpRepository.findAll();
    }

}
