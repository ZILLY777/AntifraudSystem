package antifraud.Jsons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;



public class JsonRegisterObject {

    @NotBlank
    public
    String name;

    @NotBlank
    public
    String username;

    @NotBlank
    public String password;
}
