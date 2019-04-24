package moneytransfer.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public abstract class Model {
    protected final String id;
    @JsonProperty("created_at") protected final LocalDateTime createdAt;

    @JsonIgnore protected static DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Model() {
        id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public Model(String id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }


    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonGetter("created_at")
    public String getFormattedCreatedAt() {
        return createdAt.format(dateFormatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(createdAt, model.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }

    @Override
    public String toString() {
        return "Model{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
