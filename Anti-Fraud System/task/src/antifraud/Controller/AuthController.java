package antifraud.Controller;

import antifraud.Jsons.JsonChangeUserStatusObject;
import antifraud.Jsons.JsonListOutputObject;
import antifraud.Jsons.JsonRegisterObject;
import antifraud.Jsons.JsonRoleObject;

import antifraud.Services.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/user")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid JsonRegisterObject object){
        return AuthenticationService.authenticate(object);
    }

    @DeleteMapping ("/user/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Valid String username) throws JsonProcessingException {
        return AuthenticationService.deleteUser(username);
    }

    @GetMapping("/list")
    public List<JsonListOutputObject> getUserList() {
        return AuthenticationService.getUserList();
    }

    @PutMapping("/role")
    @Transactional
    public ResponseEntity<Object> changeUserRole(@RequestBody @Valid JsonRoleObject object) throws JsonProcessingException {
        return AuthenticationService.changeUserRole(object);
    }

    @PutMapping("/access")
    public ResponseEntity<Object>  changeUserAccountStatus(@RequestBody @Valid JsonChangeUserStatusObject object) throws JsonProcessingException {
        return AuthenticationService.changeUserAccountStatus(object);
    }

}
