package pl.filiphagno.dispatch_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.message.Status;
import pl.filiphagno.dispatch_service.message.TrackingStatusUpdated;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingService {

    private static final String TRACKING_STATUS_TOPIC = "tracking.status";

    private final KafkaTemplate<String, Object> kafkaProducer;

    public void process(DispatchPreparing dispatchPreparing) throws Exception {
        log.info("Received dispatch preparing message : " + dispatchPreparing);

        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .orderId(dispatchPreparing.getUuid())
                .status(Status.PREPARING)
                .build();
        kafkaProducer.send(TRACKING_STATUS_TOPIC, trackingStatusUpdated).get();
    }
}
