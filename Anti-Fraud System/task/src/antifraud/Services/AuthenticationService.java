package antifraud.Services;


import antifraud.Jsons.*;
import antifraud.Status.Status;
import antifraud.User.User;
import antifraud.User.UserRepository;
import antifraud.User.UserReturn;
import antifraud.User.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AuthenticationService {
    private static PasswordEncoder encoder;
    private static ObjectMapper mapper;
    private static UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository,ObjectMapper mapper, PasswordEncoder encoder){
        AuthenticationService.userRepository = userRepository;
        AuthenticationService.mapper = mapper;
        AuthenticationService.encoder = encoder;
    }
    public static ResponseEntity<Object> authenticate(JsonRegisterObject object){
        try{
            if (userRepository.findall().isEmpty()){
                User user = new User(object.name, object.username,encoder.encode(object.password),
                        new UserRole().ADMINISTRATOR, true);
                userRepository.save(user);
                return new ResponseEntity<>(mapper.writeValueAsString(new JsonRegistrationOutputObject(user)),
                        HttpStatus.CREATED);
            }else if (userRepository.findByUsername(object.username)==null){
                User user = new User(object.name, object.username,encoder.encode(object.password),
                        new UserRole().MERCHANT, false);
                userRepository.save(user);
                return new ResponseEntity<>(mapper.writeValueAsString(new JsonRegistrationOutputObject(user)),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

    }
    public static List<JsonListOutputObject> getUserList()  {
        return userRepository.findall();
    }

    public static ResponseEntity<Object> deleteUser(String username) throws JsonProcessingException {
        if(userRepository.findByUsername(username)!=null){
            userRepository.deleteUserByUsername(username);
            return new ResponseEntity<>(mapper.writeValueAsString(new JsonDeleteObject(username)),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public static ResponseEntity<Object> changeUserRole(JsonRoleObject object) throws JsonProcessingException {
        if (userRepository.findByUsername(object.username)!=null) {
            if (object.role.equals("SUPPORT") | object.role.equals("MERCHANT")){
                if (!userRepository.getUserByUsername(object.username).getRole().equals(object.role)){
                    userRepository.changeUserRole(object.role, object.username);
                    return new ResponseEntity<>(mapper.writeValueAsString(JsonRegistrationOutputObject.getJson(
                            userRepository.getUserByUsername(object.username),object)
                    ), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public static ResponseEntity<Object> changeUserAccountStatus(JsonChangeUserStatusObject object) throws
            JsonProcessingException {
        if (userRepository.findByUsername(object.username)!=null) {
            if (!userRepository.getUserByUsername(object.username).getRole().equals(UserRole.ADMINISTRATOR)){
                if(object.operation.equals("UNLOCK")){
                    userRepository.changeUserAccountStatus(1, object.username);
                    return new ResponseEntity<>(mapper.writeValueAsString(new JsonUnlockStatus(object)),
                            HttpStatus.OK);
                } else if (object.operation.equals("LOCK")) {
                    userRepository.changeUserAccountStatus(0, object.username);
                    return new ResponseEntity<>(mapper.writeValueAsString(new JsonLockStatus(object)),
                            HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
