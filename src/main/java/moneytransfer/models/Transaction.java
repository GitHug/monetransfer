package moneytransfer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction extends Model {
    @JsonProperty("source_account") private final Account sourceAccount;
    @JsonProperty("target_account") private final Account targetAccount;
    @JsonProperty("amount") private final BigDecimal amount;

    public Transaction(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
    }

    public Transaction(
            @JsonProperty("id") String id,
            @JsonProperty("created_at") String createdAt,
            @JsonProperty("source_account") Account sourceAccount,
            @JsonProperty("target_account") Account targetAccount,
            @JsonProperty("amount") BigDecimal amount) {
        super(id, LocalDateTime.parse(createdAt, dateFormatter));

        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(sourceAccount, that.sourceAccount) &&
                Objects.equals(targetAccount, that.targetAccount) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sourceAccount, targetAccount, amount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "sourceAccount=" + sourceAccount +
                ", targetAccount=" + targetAccount +
                ", amount=" + amount +
                ", id='" + id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
