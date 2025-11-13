package pl.filiphagno.dispatch_service.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.filiphagno.dispatch_service.message.DispatchCompleted;
import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.services.TrackingService;
import pl.filiphagno.dispatch_service.util.TestEventData;

import java.time.LocalDate;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

class DispatchTrackingHandlerTest {
    private TrackingService trackingServiceMock;

    private DispatchTrackingHandler handler;

    @BeforeEach
    public void setup() {
        trackingServiceMock = mock(TrackingService.class);
        handler = new DispatchTrackingHandler(trackingServiceMock);
    }

    @Test
    public void listen_DispatchPreparing() throws Exception {
        DispatchPreparing testEvent = TestEventData.buildDispatchPreparingEvent(randomUUID());
        handler.listen(testEvent);
        verify(trackingServiceMock, times(1)).processDispatchPreparing(testEvent);
    }

    @Test
    public void listen_DispatchPreparingException() throws Exception {
        DispatchPreparing testEvent = TestEventData.buildDispatchPreparingEvent(randomUUID());
        doThrow(new RuntimeException("Service failure")).when(trackingServiceMock).processDispatchPreparing(testEvent);

        handler.listen(testEvent);

        verify(trackingServiceMock, times(1)).processDispatchPreparing(testEvent);
    }

    @Test
    public void listen_DispatchCompleted() throws Exception {
        DispatchCompleted testEvent = TestEventData.buildDispatchCompletedEvent(randomUUID(), LocalDate.now().toString());
        handler.listen(testEvent);
        verify(trackingServiceMock, times(1)).processDispatched(testEvent);
    }

    @Test
    public void listen_DispatchCompletedThrowsException() throws Exception {
        DispatchCompleted testEvent = TestEventData.buildDispatchCompletedEvent(randomUUID(), LocalDate.now().toString());
        doThrow(new RuntimeException("Service failure")).when(trackingServiceMock).processDispatched(testEvent);

        handler.listen(testEvent);

        verify(trackingServiceMock, times(1)).processDispatched(testEvent);
    }
}