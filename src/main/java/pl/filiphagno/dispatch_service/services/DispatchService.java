package pl.filiphagno.dispatch_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.message.OrderCreated;
import pl.filiphagno.dispatch_service.message.OrderDispatched;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";
    private static final String DISPATCH_PREPARING_TOPIC = "dispatch.tracking";
    private final KafkaTemplate<String, Object> kafkaProducer;

    public void process(OrderCreated payload) throws ExecutionException, InterruptedException {

        DispatchPreparing dispatchPreparing = DispatchPreparing.builder().uuid(payload.getOrderId()).build();
        kafkaProducer.send(DISPATCH_PREPARING_TOPIC, dispatchPreparing).get();

        OrderDispatched orderDispatched = OrderDispatched.builder().orderId(payload.getOrderId()).build();
        kafkaProducer.send(ORDER_DISPATCHED_TOPIC, orderDispatched).get();

    };
}
