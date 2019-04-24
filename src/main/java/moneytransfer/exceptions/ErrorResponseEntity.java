package moneytransfer.exceptions;

import java.util.Objects;

public class ErrorResponseEntity {
    private final String message;
    private final int code;


    public ErrorResponseEntity(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponseEntity that = (ErrorResponseEntity) o;
        return code == that.code &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {

        return Objects.hash(message, code);
    }
}
