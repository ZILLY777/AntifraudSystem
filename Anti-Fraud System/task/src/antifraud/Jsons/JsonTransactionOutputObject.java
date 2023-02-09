package antifraud.Jsons;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonTransactionOutputObject {
    public String result;
    public String info;

    public JsonTransactionOutputObject(String result, String info){
        this.result = result;
        this.info = info;
    }
}
