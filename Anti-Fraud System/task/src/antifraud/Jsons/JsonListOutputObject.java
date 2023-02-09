package antifraud.Jsons;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id",  "name", "username, role"})
public interface JsonListOutputObject {
    int getId();
    String getUsername();
    String getName();
    String getrole();
}
