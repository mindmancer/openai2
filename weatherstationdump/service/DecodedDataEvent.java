package pl.mlodawski.weatherstationdump.service;

import org.springframework.context.ApplicationEvent;
import pl.mlodawski.weatherstationdump.model.DecodedData;


/**
 * DecodedDataEvent is an event class that represents the occurrence of a decoded data event.
 * It extends the ApplicationEvent class.
 */
public class DecodedDataEvent extends ApplicationEvent {
    public DecodedDataEvent(Object source, DecodedData signalData) {
        super(source);
        super.source = signalData;
    }
}
