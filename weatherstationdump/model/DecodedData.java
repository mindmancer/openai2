package pl.mlodawski.weatherstationdump.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class DecodedData {
    /**
     * The model of the decoded device.
     */
    private String model;
    /**
     * The ID of the decoded device.
     *
     * <p>
     * This variable represents the ID associated with the decoded device. The ID is an integer value.
     * </p>
     *
     * <p>
     * This variable is a private field, which means it can only be accessed within the same class.
     * </p>
     */
    private int id;
    /**
     * The channel number for the device.
     */
    private int channel;
    /**
     * Represents the battery status.
     */
    @JsonProperty("batteryStatus")
    private boolean batteryOk;
    /**
     * Represents the temperature in Celsius.
     *
     * This variable is used in the {@link DecodedData} class to store the temperature value in Celsius.
     *
     * <p>
     * Example usage:
     * <pre>
     * DecodedData decodedData = new DecodedData();
     * decodedData.setTemperatureC(25.6);
     * double temperature = decodedData.getTemperatureC();
     * </pre>
     * </p>
     *
     * @see DecodedData
     * @since 1.0
     */
    @JsonProperty("temperatureCelsius")
    private double temperatureC;
    /**
     * Represents the humidity level.
     */
    private int humidity;
}
