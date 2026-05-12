package com.brunofragadev.infrastructure.default_return_api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean status;
    @Getter
    private String message;
    @Getter
    private T dados;
    private ApiError erro;

    public ApiResponse() {
    }

    public ApiResponse(boolean status, String message, T dados, ApiError erro) {
        this.status = status;
        this.message = message;
        this.dados = dados;
        this.erro = erro;
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, message, data, null);
    }
    public static <T> ApiResponse<T> error(String message, ApiError erro){
        return new ApiResponse<>(false, "Erro ao utilizar o recurso", null, erro);
    }
    public boolean getStatus(){
        return status;
    }

}
