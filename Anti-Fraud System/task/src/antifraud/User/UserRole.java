package antifraud.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {
    public static String ADMINISTRATOR;
    public static String SUPPORT;
    public static String Anonymous;
    public static String MERCHANT;


    public UserRole() {
        UserRole.ADMINISTRATOR = "ADMINISTRATOR";
        UserRole.SUPPORT = "SUPPORT";
        UserRole.Anonymous = "Anonymous";
        UserRole.MERCHANT = "MERCHANT";
    }
}
