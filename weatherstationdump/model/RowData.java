package pl.mlodawski.weatherstationdump.model;

import lombok.Data;
import lombok.Value;

@Data
public class RowData {

     /**
      * The index of a row in a table.
      */
     int index;
     /**
      * Represents the length of a variable.
      */
     int length;
     /**
      * Represents a hexadecimal string value.
      */
     String dataHex;
     /**
      * Represents a binary data string.
      * <p>
      * The {@code dataBin} variable stores a binary data string.
      * It is usually used as a field in the {@link RowData} class.
      * </p>
      * <p>
      * Example usage:
      * <pre>{@code
      * RowData rowData = new RowData();
      * rowData.setDataBin(dataBin);
      * }</pre>
      * </p>
      */
     String dataBin;
}
