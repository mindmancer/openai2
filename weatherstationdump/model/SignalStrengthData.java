package pl.mlodawski.weatherstationdump.model;

import lombok.Data;


@Data
public class SignalStrengthData {
     /**
      * The Received Signal Strength Indicator (RSSI) represents the power level of a received signal.
      * It is used to measure the strength and quality of a wireless signal.
      */
     double rssi;
     /**
      * Signal-to-Noise Ratio (SNR) is a measure of the relative amount of signal power to the amount of noise power in a signal.
      * It is defined as the ratio of signal power to the noise power.
      */
     double snr;
     /**
      * Represents the noise level in a {@link SignalStrengthData} object.
      */
     double noise;
}
