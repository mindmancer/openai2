package pl.mlodawski.weatherstationdump.model;

import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
public class SignalData {

     /**
      * The shortWidth variable represents the width of a signal pulse.
      * It is used in the SignalData class to store the value of the short width dimension.
      * The short width is an integer value that is measured in unspecified units.
      * It is used in the context of signal analysis and modulation.
      *
      * Example usage:
      * SignalData signal = new SignalData();
      * signal.setShortWidth(5);
      *
      * @see SignalData
      */
     int shortWidth;
     /**
      * The variable longWidth represents the width of a long value. It is typically used as a field in the SignalData class.
      * <p>
      * Example usage:
      * <pre>{@code
      * SignalData signalData = new SignalData();
      * signalData.setLongWidth(longWidth);
      * }</pre>
      * </p>
      * <p>
      * Please refer to the SignalData class for more information about the related variables and their usage.
      * </p>
      */
     int longWidth;
     /**
      * Represents the limit for resetting a variable.
      *
      * The resetLimit variable is used to determine the threshold at which a variable should be reset to its initial value.
      * It is typically used in the context of signal processing or data analysis algorithms.
      *
      * For example, if a variable exceeds the resetLimit value, it may indicate an anomaly or an error, and the variable should be reset to its initial state.
      *
      * The resetLimit variable is a member of the SignalData class, which represents a data structure used to store signal-related information.
      *
      * This variable is an integer that holds the reset limit value.
      *
      * Example usage:
      *
      * SignalData data = new SignalData();
      * int limit = data.getResetLimit();
      *
      * Please refer to the SignalData class for more information about the related variables and their usage.
      */
     int resetLimit;
     /**
      * Represents the synchronization width of a signal.
      * <p>
      * The {@code syncWidth} variable is an integer that stores the synchronization width of a signal.
      * It is a member of the {@link SignalData} class.
      * </p>
      * <p>
      * Example usage:
      * <pre>{@code
      * SignalData signalData = new SignalData();
      * signalData.setSyncWidth(syncWidth);
      * int syncWidth = signalData.getSyncWidth();
      * }</pre>
      * </p>
      * @see SignalData
      */
     int syncWidth;
     /**
      * The number of rows in a table.
      * <p>
      * The {@code numberOfRows} variable represents the number of rows in a table.
      * This variable is usually used in the {@link SignalData} class to determine
      * the size or length of the table.
      * </p>
      * <p>
      * Example usage:
      * <pre>{@code
      * SignalData signalData = new SignalData();
      * signalData.setNumberOfRows(numberOfRows);
      * }</pre>
      * </p>
      *
      * @see SignalData
      */
     int numberOfRows;
     /**
      * Represents a list of row data.
      * <p>
      * The {@code rows} variable is of type {@link List} and stores objects of type {@link RowData}.
      * It is used to store multiple rows of data in a table.
      * </p>
      * <p>
      * Example usage:
      * <pre>{@code
      * List<RowData> rows = new ArrayList<>();
      * rows.add(row1);
      * rows.add(row2);
      * }</pre>
      * </p>
      *
      * @see RowData
      */
     List<RowData> rows = new ArrayList<>();
     /**
      * ModulationData represents the data related to modulation type and variables used in modulation calculations.
      */
     ModulationData modulation;
     /**
      * Represents the signal strength data.
      * <p>
      * The {@code SignalStrengthData} class is a data class used to store information about the signal strength of a signal.
      * It contains three properties: rssi, snr, and noise.
      *</p>
      * <p>
      * Example usage:
      * <pre>{@code
      * SignalStrengthData signalStrength = new SignalStrengthData();
      * signalStrength.setRssi(-70);
      * signalStrength.setSnr(20);
      * signalStrength.setNoise(10);
      * }</pre>
      * </p>
      * <p>
      * The {@code rssi} property represents the received signal strength indicator (RSSI) value in decibels (dB).
      * The {@code snr} property represents the signal-to-noise ratio (SNR) value in decibels (dB).
      * The {@code noise} property represents the noise value in decibels (dB).
      *
      * Please refer to the {@link SignalData} class for more information about related properties and their usage.
      * </p>
      *
      * @see SignalData
      */
     SignalStrengthData signalStrength;
     /**
      * Stores frequency offset information.
      */
     FrequencyOffsetData frequencyOffset;
}
