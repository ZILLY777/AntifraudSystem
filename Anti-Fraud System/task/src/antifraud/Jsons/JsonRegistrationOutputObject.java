package antifraud.Jsons;

import antifraud.User.User;


public class JsonRegistrationOutputObject {
    public Long id;
    public String name;
    public String username;
    public String role;

    public JsonRegistrationOutputObject(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

    public static JsonRegistrationOutputObject getJson(User user, JsonRoleObject object ){
        user.setRole(object.role);
        return new JsonRegistrationOutputObject(user);
    }


}
