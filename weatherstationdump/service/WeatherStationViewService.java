package pl.mlodawski.weatherstationdump.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.mlodawski.weatherstationdump.model.DecodedData;

import java.io.FileNotFoundException;

@Service
@Slf4j
public class WeatherStationViewService {

    ApplicationEventPublisher eventPublisher;

    DecodedData decodedData;

    @Autowired
    public WeatherStationViewService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    /**
     * This method is an event listener for DecodedDataEvent. It is called when a DecodedDataEvent
     * occurs. The method checks if the source of the event is an instance of DecodedData, and if
     * so, it assigns the DecodedData object to the decodedData field of the WeatherStationViewService class.
     *
     * @param event The DecodedDataEvent object representing the occurrence of the event.
     */
    @EventListener
    private void getDataEvent(DecodedDataEvent event) {
        if(event.getSource() instanceof DecodedData myDecodedData){
            this.decodedData = myDecodedData;
        }
    }

    /**
     * Retrieves the decoded data.
     *
     * @return The decoded data.
     */
    @SneakyThrows
    public DecodedData getDecodedData() {
        if(decodedData == null){
            throw new FileNotFoundException("No data");
        }
        return decodedData;
    }
}
