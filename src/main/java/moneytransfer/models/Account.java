package moneytransfer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Account extends Model {
    private String name;
    private final Balance balance;
    private boolean active;

    public Account(String name) {
        this.name = name;
        this.balance = new Balance();
        this.active = true;
    }

    public Account(Account account) {
        this(account.getId(), account.getCreatedAt(), account.getName(), account.getBalance(), account.isActive());
    }

    public Account(
            @JsonProperty("id") String id,
            @JsonProperty("created_at") String createdAt,
            @JsonProperty("name") String name,
            @JsonProperty("balance") BigDecimal balance,
            @JsonProperty("active") boolean active) {
        this(id, LocalDateTime.parse(createdAt, dateFormatter), name, balance, active);
    }

    public Account(
            String id,
            LocalDateTime createdAt,
            String name,
            BigDecimal balance,
            boolean active) {
        super(id, createdAt);

        this.name = name;
        this.balance = new Balance(balance);
        this.active = active;
    }

    public BigDecimal getBalance() {
        return new BigDecimal(String.valueOf(this.balance.getBalance()));
    }

    public void setBalance (BigDecimal balance) {
        this.balance.setBalance(balance);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return active == account.active &&
                Objects.equals(name, account.name) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, balance, active);
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", active=" + active +
                ", id='" + id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
