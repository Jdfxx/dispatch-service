package pl.filiphagno.dispatch_service.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.filiphagno.dispatch_service.message.OrderCreated;
import pl.filiphagno.dispatch_service.services.DispatchService;
import pl.filiphagno.dispatch_service.util.TestEventData;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderCreatedHandlerTest {

    private OrderCreatedHandler orderCreatedHandler;
    private DispatchService dispatchService;

    @BeforeEach
    void setup() {
        dispatchService = Mockito.mock(DispatchService.class);
        orderCreatedHandler = new OrderCreatedHandler(dispatchService);
    }

    @Test
    void listen() {
        OrderCreated testEvent = TestEventData.buildOrderCreated(UUID.randomUUID(), UUID.randomUUID().toString());
        orderCreatedHandler.listen(testEvent);
        verify(dispatchService, times(1)).process(testEvent);
    }
}