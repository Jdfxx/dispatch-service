package pl.filiphagno.dispatch_service.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.services.TrackingService;
import pl.filiphagno.dispatch_service.util.TestEventData;

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
    public void listen_Success() throws Exception {
        DispatchPreparing testEvent = TestEventData.buildDispatchPreparingEvent(randomUUID());
        handler.listen(testEvent);
        verify(trackingServiceMock, times(1)).process(testEvent);
    }

    @Test
    public void listen_ServiceThrowsException() throws Exception {
        DispatchPreparing testEvent = TestEventData.buildDispatchPreparingEvent(randomUUID());
        doThrow(new RuntimeException("Service failure")).when(trackingServiceMock).process(testEvent);

        handler.listen(testEvent);

        verify(trackingServiceMock, times(1)).process(testEvent);
    }
}