package pl.mlodawski.weatherstationdump;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mlodawski.weatherstationdump.model.ErrorModel;
import pl.mlodawski.weatherstationdump.model.ErrorResponse;
import pl.mlodawski.weatherstationdump.service.WeatherStationCommandService;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class WeatherStationCommandController {

    private final WeatherStationCommandService weatherStationCommandService;

    @Autowired
    public WeatherStationCommandController(WeatherStationCommandService weatherStationCommandService) {
        this.weatherStationCommandService = weatherStationCommandService;
    }

    /**
     * Saves weather station data by starting the process of dumping data.
     * This method is used as the endpoint for initiating the data dump.
     *
     * @return The ResponseEntity with HTTP status code 201 (CREATED).
     */
    @PostMapping("/api/weather_station/init")
    public ResponseEntity<String> saveWeatherStationData() {
        weatherStationCommandService.startDumpData();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Handles the IllegalArgumentException exception.
     *
     * @param ex The IllegalArgumentException that was caught
     * @return The ResponseEntity containing the ErrorResponse with information about the error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException in {}::{} - {}", ex.getCause(), ex.getStackTrace(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(List.of(new ErrorModel("IllegalArgumentException", "There was an IllegalArgumentException", null, null)));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IOException exceptions.
     *
     * @param ex The IOException that was caught
     * @return The ResponseEntity containing the ErrorResponse with information about the error
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOExceptionException(IOException ex) {
        log.error("IOException in {}::{} - {}", ex.getCause(), ex.getStackTrace(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(List.of(new ErrorModel("IOException", "There was an IOException", null, null)));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
