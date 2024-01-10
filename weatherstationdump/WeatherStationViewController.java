package pl.mlodawski.weatherstationdump;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mlodawski.weatherstationdump.model.DecodedData;
import pl.mlodawski.weatherstationdump.model.ErrorModel;
import pl.mlodawski.weatherstationdump.model.ErrorResponse;
import pl.mlodawski.weatherstationdump.service.WeatherStationCommandService;
import pl.mlodawski.weatherstationdump.service.WeatherStationViewService;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@Slf4j
public class WeatherStationViewController {

    private final WeatherStationViewService weatherStationViewService;

    @Autowired
    public WeatherStationViewController(WeatherStationViewService weatherStationViewService) {
        this.weatherStationViewService = weatherStationViewService;
    }

    /**
     * Retrieves the weather station data.
     *
     * @return The response entity containing the decoded data.
     */
    @GetMapping(value = "/api/weather_station", produces = "application/json")
    public ResponseEntity<DecodedData> getWeatherStationData() {
       return new ResponseEntity<>(weatherStationViewService.getDecodedData(), HttpStatus.OK);
    }

    /**
     * Handles FileNotFoundException and returns an appropriate error response.
     *
     * @param ex The FileNotFoundException that occurred.
     * @return The ResponseEntity with the error response.
     */
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(FileNotFoundException ex) {
        log.error("Exception in {}::{} - {}", ex.getCause(), ex.getStackTrace(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(List.of(new ErrorModel("FileNotFoundException", "There was an FileNotFoundException", null, null)));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
