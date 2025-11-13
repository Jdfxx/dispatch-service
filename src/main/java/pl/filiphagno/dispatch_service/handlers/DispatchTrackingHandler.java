package pl.filiphagno.dispatch_service.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.filiphagno.dispatch_service.message.DispatchCompleted;
import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.message.OrderCreated;
import pl.filiphagno.dispatch_service.services.DispatchService;
import pl.filiphagno.dispatch_service.services.TrackingService;


@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(
        id = "dispatchTrackingConsumerClient",
        topics = "dispatch.tracking",
        groupId = "tracking.dispatch.tracking",
        containerFactory = "kafkaListenerContainerFactory"
)
public class DispatchTrackingHandler {

    @Autowired
    private final TrackingService trackingService;

    @KafkaHandler
    public void listen(DispatchPreparing dispatchPreparing) throws Exception {
        try {
            trackingService.processDispatchPreparing(dispatchPreparing);
        } catch (Exception e) {
            log.error("DispatchPreparing processing failure", e);
        }
    }

    @KafkaHandler
    public void listen(DispatchCompleted dispatchCompleted) {
        try {
            trackingService.processDispatched(dispatchCompleted);
        } catch (Exception e) {
            log.error("DispatchCompleted processing failure", e);
        }
    }
}
