package pl.filiphagno.dispatch_service.util;

import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.message.OrderCreated;

import java.util.UUID;

public class TestEventData {
    public static OrderCreated buildOrderCreated(UUID orderId, String item) {
        return OrderCreated.builder().orderId(orderId).item(item).build();
    }

    public static DispatchPreparing buildDispatchPreparingEvent(UUID orderId) {
        return DispatchPreparing.builder()
                .uuid(orderId)
                .build();
    }
}
