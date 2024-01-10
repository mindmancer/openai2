package pl.mlodawski.weatherstationdump.model;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private List<ErrorModel> errors;

    public ErrorResponse(List<ErrorModel> errors) {
        this.errors = errors;
    }
}
