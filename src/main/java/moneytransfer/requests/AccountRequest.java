package moneytransfer.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRequest {
    private @JsonProperty String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
