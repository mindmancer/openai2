package pl.mlodawski.weatherstationdump.model;

import lombok.Data;


@Data
public class FrequencyOffsetData {
     /**
      * Represents the value of f1.
      *
      * This variable is a member of the FrequencyOffsetData class, which is a data class used to store frequency offset information.
      *
      * f1 represents a frequency offset in an integral format, without a specific unit. To obtain the frequency offset in kilohertz, use f1KHz variable.
      *
      * Example usage:
      *
      * FrequencyOffsetData data = new FrequencyOffsetData();
      * int offset = data.f1;
      *
      * Please refer to the FrequencyOffsetData class for more information about the related variables and their usage.
      */
     int f1;
     /**
      * Represents the frequency offset value f2.
      */
     int f2;
     /**
      * The frequency in kilohertz (kHz) represented by the variable f1KHz.
      */
     double f1KHz;
     /**
      * Frequency offset in Kilohertz (KHz).
      */
     double f2KHz;
}
