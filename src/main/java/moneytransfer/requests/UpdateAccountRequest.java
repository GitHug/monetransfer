package moneytransfer.requests;

import org.jvnet.hk2.annotations.Optional;

import java.math.BigDecimal;

public class UpdateAccountRequest extends AccountRequest {
    @Optional
    private BigDecimal balance;


    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
