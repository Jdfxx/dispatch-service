package pl.filiphagno.dispatch_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.filiphagno.dispatch_service.message.DispatchCompleted;
import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.message.OrderCreated;
import pl.filiphagno.dispatch_service.message.OrderDispatched;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";
    private static final String DISPATCH_TRACKING_TOPIC = "dispatch.tracking";
    private final KafkaTemplate<String, Object> kafkaProducer;

    public void process(String key, OrderCreated payload) throws ExecutionException, InterruptedException {

        DispatchPreparing dispatchPreparing = DispatchPreparing.builder().uuid(payload.getOrderId()).build();
        kafkaProducer.send(DISPATCH_TRACKING_TOPIC, key, dispatchPreparing).get();

        OrderDispatched orderDispatched = OrderDispatched.builder().orderId(payload.getOrderId()).build();
        kafkaProducer.send(ORDER_DISPATCHED_TOPIC, key, orderDispatched).get();

        DispatchCompleted dispatchCompleted = DispatchCompleted.builder()
                .orderId(payload.getOrderId())
                .dispatchedDate(LocalDate.now().toString())
                .build();
        kafkaProducer.send(DISPATCH_TRACKING_TOPIC, key, dispatchCompleted).get();

    };
}
