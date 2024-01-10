package pl.mlodawski.weatherstationdump.model;


import lombok.Data;

@Data

public class ErrorModel {
    private String code;
    private String message;
    private String path;
    private String userMessage;

    public ErrorModel(String code, String message, String path, String userMessage) {
        this.code = code;
        this.message = message;
        this.path = path;
        this.userMessage = userMessage;
    }
}
