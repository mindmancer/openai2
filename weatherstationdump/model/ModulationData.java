package pl.mlodawski.weatherstationdump.model;

import lombok.Data;


@Data
public class ModulationData {
     /**
      * The type of modulation for a signal.
      *
      * @see ModulationData
      */
     String modulationType;
     /**
      * Represents the variable 's' used in the ModulationData class.
      *
      * This variable is an integer that holds the value of 's'.
      *
      * Example usage:
      *      ModulationData data = new ModulationData();
      *      data.setS(10);
      *      int value = data.getS();
      *
      * @see ModulationData
      */
     int s;
     /**
      * Represents the value of the variable 'l'.
      *
      * This variable is a member of the class ModulationData and represents an
      * integer value.
      *
      * @see ModulationData
      */
     int l;
     /**
      * Variable g represents the value of a specific parameter in the ModulationData class.
      */
     int g;
     /**
      * Represents the value of the 'r' variable.
      *
      * This variable is a part of the ModulationData class.
      * It is an integer representing a specific value.
      */
     int r;
}
