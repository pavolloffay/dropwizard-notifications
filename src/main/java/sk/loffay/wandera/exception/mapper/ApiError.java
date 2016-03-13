package sk.loffay.wandera.exception.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Pavol Loffay
 */
public class ApiError {

    private String errorMessage;


    @JsonCreator
    public ApiError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
