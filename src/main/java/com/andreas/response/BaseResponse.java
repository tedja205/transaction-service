package com.andreas.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    boolean success;

    List<?> data;

    String messageTitle;

    String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> validationMessage;

    public BaseResponse setBaseResponse(boolean success, List<?> data, String messageTitle, String message, List<String> validationMessage) {
        return new BaseResponse(success, data, messageTitle, message, validationMessage);
    }

}
