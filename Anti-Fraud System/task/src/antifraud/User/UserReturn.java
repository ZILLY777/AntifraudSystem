package antifraud.User;

import antifraud.Jsons.JsonRoleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserReturn {

    private static UserRepository userRepository ;

    @Autowired
    public UserReturn(UserRepository userRepository){
        UserReturn.userRepository = userRepository;
    }



    @Transactional
    public User UserReturn(JsonRoleObject object){
        return userRepository.getUserByUsername(object.username);
    }
}
