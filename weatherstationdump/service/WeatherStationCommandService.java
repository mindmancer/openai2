package pl.mlodawski.weatherstationdump.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.mlodawski.weatherstationdump.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class WeatherStationCommandService {

    private static final List<String> KILL_TASK_COMMAND = Arrays.asList("taskkill", "/F", "/IM", "rtl_433-rtlsdr.exe");
    private static final long SLEEP_TIME_IN_MILLISECONDS = 1000;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public WeatherStationCommandService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Starts a new thread to dump data by invoking the initDumpData method.
     */
    public void startDumpData() {
        Runnable dumpTask = this::initDumpData;
        Thread dumpThread = new Thread(dumpTask);
        dumpThread.start();
    }


    /**
     * Initializes the dumping process by setting the kill task command, initializing the process, and handling the process output.
     * If the process fails to start, an error message is logged and the method returns.
     *
     * @throws Exception if any exception is thrown during the execution of the method.
     */
    @SneakyThrows
    public void initDumpData() {
        setKillTaskCommand();
        initializeProcess();
        log.info("Task has been started successfully");

    }

    /**
     * Executes a command to kill a task.
     * <p>
     * The method uses the {@link ProcessBuilder} class to start a new process with the specified command.
     * The command for killing the task is represented by the constant {@link #KILL_TASK_COMMAND}.
     * After executing the command, the method pauses for a certain amount of time specified by the constant {@link #SLEEP_TIME_IN_MILLISECONDS}.
     * </p>
     * <p>
     * If an {@link IOException} or {@link InterruptedException} occurs during the execution of the command,
     * an error message is logged using the {@link #log} instance, with the exception as the cause.
     * Additionally, the method interrupts the current thread by calling {@link Thread#interrupt()}.
     * </p>
     * <p>
     * Note: This method is private and does not have any return value.
     * </p>
     */
    private void setKillTaskCommand() {
        try {
            new ProcessBuilder(KILL_TASK_COMMAND).start();
            Thread.sleep(SLEEP_TIME_IN_MILLISECONDS);
        } catch (IOException | InterruptedException e) {
            log.error("Error while killing task", e);
            Thread.currentThread().interrupt();
        }
    }



    /**
     * Initializes the process by starting the rtl_433-rtlsdr executable and reading its output.
     *
     * @throws IOException if an I/O error occurs when starting the process
     */
    private void initializeProcess() throws IOException {
        String currentPath = System.getProperty("user.dir");
        String executablePath = currentPath + File.separator + "RTL" + File.separator + "rtl_433-rtlsdr.exe";
        ProcessBuilder pb = new ProcessBuilder(executablePath, "-f", "433.902M", "-A", "-R", "0");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        SignalData signalData = new SignalData();
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                log.info("Process has terminated.");
                break;
            }
            processLine(line, signalData);
            if(signalData.getRows().size() >= 6){
                decodeData(signalData);
                signalData.getRows().clear();
            }
        }
        int exitCode;
        try {
            exitCode = process.waitFor();
            log.info("Process exited with code: " + exitCode);
        } catch (InterruptedException e) {
            log.error("Process was interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Processes a line of data based on its content and updates the signal data accordingly.
     *
     * @param line       The line of data to be processed.
     * @param signalData The signal data object to be updated.
     */
    private void processLine(String line, SignalData signalData) {
        if (line.startsWith("Attempting demodulation")) {
            processDemodulationData(line, signalData);
        } else if (line.startsWith("bitbuffer:: Number of rows")) {
            processBufferData(line, signalData);
        } else if (line.startsWith("[")) {
            processRowData(line, signalData);
        } else if (line.contains("m=OOK_PPM,")) {
            processModulationData(line, signalData);
        } else if (line.startsWith("RSSI:")) {
            processSignalStrengthData(line, signalData);
        } else if (line.startsWith("Frequency offsets [F1, F2]:")) {
            processFrequencyOffsetData(line, signalData);
        }
    }

    /**
     * Processes demodulation data and updates the values of SignalData object.
     *
     * @param line       the line of data containing the demodulation information
     * @param signalData the SignalData object to update with demodulation data
     */
    private void processDemodulationData(String line, SignalData signalData) {
        Matcher m = Pattern.compile("short_width: (\\d+), long_width: (\\d+), reset_limit: (\\d+), sync_width: (\\d+)").matcher(line);
        if (m.find()) {
            signalData.setShortWidth(Integer.parseInt(m.group(1)));
            signalData.setLongWidth(Integer.parseInt(m.group(2)));
            signalData.setResetLimit(Integer.parseInt(m.group(3)));
            signalData.setSyncWidth(Integer.parseInt(m.group(4)));
        }
    }

    /**
     * Processes the buffer data in the given line and updates the number of rows in the SignalData object.
     *
     * @param line       the line containing the buffer data
     * @param signalData the SignalData object to update
     */
    private void processBufferData(String line, SignalData signalData) {
        Matcher m = Pattern.compile("bitbuffer:: Number of rows: (\\d+)").matcher(line);
        if (m.find()) {
            signalData.setNumberOfRows(Integer.parseInt(m.group(1)));
        }
    }

    /**
     * Processes a single row of data from a given line and updates the SignalData object.
     *
     * @param line       The line containing the row data
     * @param signalData The SignalData object to update with the row data
     */
    private void processRowData(String line, SignalData signalData) {
        Matcher m = Pattern.compile("\\[(\\d{2})\\] \\{(\\d+)\\} ([\\w\\s:]+)").matcher(line);
        if (m.find()) {
            RowData rowData = new RowData();
            rowData.setIndex(Integer.parseInt(m.group(1)));
            rowData.setLength(Integer.parseInt(m.group(2)));

            String[] dataParts = m.group(3).split(":");
            if (dataParts.length >= 2) {
                rowData.setDataHex(dataParts[0].trim());
                rowData.setDataBin(dataParts[1].trim());
            }
            signalData.getRows().add(rowData);
        }
    }

    /**
     * Processes the modulation data from a given line and updates the signal data object.
     *
     * @param line       the line containing the modulation data
     * @param signalData the signal data object to update
     */
    private void processModulationData(String line, SignalData signalData) {
        Matcher m = Pattern.compile("m=(\\w+),s=(\\d+),l=(\\d+),g=(\\d+),r=(\\d+)").matcher(line);
        if (m.find()) {
            ModulationData modulation = new ModulationData();
            modulation.setModulationType(m.group(1));
            modulation.setS(Integer.parseInt(m.group(2)));
            modulation.setL(Integer.parseInt(m.group(3)));
            modulation.setG(Integer.parseInt(m.group(4)));
            modulation.setR(Integer.parseInt(m.group(5)));
            signalData.setModulation(modulation);
        }
    }

    /**
     * Process the signal strength data from the given line and update the signalData object with the parsed values.
     *
     * @param line       the line of text containing the signal strength data
     * @param signalData the SignalData object to update with the parsed signal strength values
     */
    private void processSignalStrengthData(String line, SignalData signalData) {
        Matcher m = Pattern.compile("RSSI: (-?\\d+\\.?\\d*) dB SNR: (-?\\d+\\.?\\d*) dB Noise: (-?\\d+\\.?\\d*) dB").matcher(line);
        if (m.find()) {
            SignalStrengthData signalStrength = new SignalStrengthData();
            signalStrength.setRssi(Double.parseDouble(m.group(1)));
            signalStrength.setSnr(Double.parseDouble(m.group(2)));
            signalStrength.setNoise(Double.parseDouble(m.group(3)));
            signalData.setSignalStrength(signalStrength);
        }
    }

    /**
     * Processes the frequency offset data from a given line and updates the signal data.
     *
     * @param line       the line containing the frequency offset data
     * @param signalData the signal data object to be updated
     */
    private void processFrequencyOffsetData(String line, SignalData signalData) {
        Matcher m = Pattern.compile("Frequency offsets \\[F1, F2\\]:\\s+(\\d+),\\s+(\\d+)\\s+\\(([-+]?\\d+\\.?\\d*) kHz,\\s+([-+]?\\d+\\.?\\d*) kHz\\)").matcher(line);
        if (m.find()) {
            FrequencyOffsetData frequencyOffset = new FrequencyOffsetData();
            frequencyOffset.setF1(Integer.parseInt(m.group(1)));
            frequencyOffset.setF2(Integer.parseInt(m.group(2)));
            frequencyOffset.setF1KHz(Double.parseDouble(m.group(3)));
            frequencyOffset.setF2KHz(Double.parseDouble(m.group(4)));
            signalData.setFrequencyOffset(frequencyOffset);
        }
    }


    /**
     * Decodes the provided SignalData and returns the decoded information in the form of DecodedData object.
     *
     * @param signalData The SignalData object to be decoded.
     * @return The DecodedData object containing the decoded information.
     */
    public DecodedData decodeData(SignalData signalData) {
        DecodedData decodedData = new DecodedData();
        if (!signalData.getRows().isEmpty()) {
            for (RowData row : signalData.getRows()) {
                try {
                    String dataBin = row.getDataBin().replace(" ", "");
                    if (!dataBin.equals("111111111111111111111111111111111111111111")) {
                        decodedData.setModel("EN8822C");
                        byte[] b = new byte[dataBin.length() / 8];
                        for (int i = 0; i < b.length; i++) {
                            String byteString = dataBin.substring(8 * i, 8 * i + 8);
                            b[i] = (byte) Integer.parseInt(byteString, 2);
                        }

                        int deviceID = b[0] & 0xFF;
                        int channel = ((b[1] & 0x30) >> 4) + 1;
                        int batteryLow = (b[4] & 0x10) >> 4;
                        int tempRAW = (((b[1] & 0x0F) << 12) | ((b[2] & 0xFF) << 4)) >> 4;
                        double tempC = Math.round(tempRAW * 0.1 * 100.0) / 100.0;
                        int humidity = (b[3] & 0xFF) >> 1;

                        decodedData.setId(deviceID);
                        decodedData.setChannel(channel);
                        decodedData.setBatteryOk(batteryLow == 0);
                        decodedData.setTemperatureC(tempC);
                        decodedData.setHumidity(humidity);
                        log.info("Decoded data: {}", decodedData);
                        eventPublisher.publishEvent(new DecodedDataEvent(this, decodedData));
                        return decodedData;

                    }
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    log.error("Error while decoding data", e);
                }
            }
        }
        return decodedData;
    }
}
