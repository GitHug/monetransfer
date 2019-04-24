package moneytransfer.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransactionRequest {
    @JsonProperty("source_account_id") private String sourceAccountId;
    @JsonProperty("target_account_id") private String targetAccontId;
    private BigDecimal amount;

    public TransactionRequest() { }

    public TransactionRequest(String sourceAccountId, String targetAccontId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccontId = targetAccontId;
        this.amount = amount;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getTargetAccontId() {
        return targetAccontId;
    }

    public void setTargetAccontId(String targetAccontId) {
        this.targetAccontId = targetAccontId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
