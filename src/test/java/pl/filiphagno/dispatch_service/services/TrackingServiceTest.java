package pl.filiphagno.dispatch_service.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import pl.filiphagno.dispatch_service.message.DispatchPreparing;
import pl.filiphagno.dispatch_service.message.TrackingStatusUpdated;
import pl.filiphagno.dispatch_service.util.TestEventData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class TrackingServiceTest {
    private KafkaTemplate kafkaProducerMock;
    private TrackingService service;

    @BeforeEach
    public void setup() {
        kafkaProducerMock = mock(KafkaTemplate.class);
        service = new TrackingService(kafkaProducerMock);
    }

    @Test
    public void process_Success() throws Exception {
        when(kafkaProducerMock.send(anyString(), any(TrackingStatusUpdated.class))).thenReturn(mock(CompletableFuture.class));

        DispatchPreparing testEvent = TestEventData.buildDispatchPreparingEvent(UUID.randomUUID());
        service.process(testEvent);

        verify(kafkaProducerMock, times(1)).send(eq("tracking.status"), any(TrackingStatusUpdated.class));
    }

    @Test
    public void process_DispatchPreparingProducerThrowsException() {
        doThrow(new RuntimeException("dispatch preparing producer failure")).when(kafkaProducerMock).send(eq("tracking.status"), any(TrackingStatusUpdated.class));

        DispatchPreparing testEvent = TestEventData.buildDispatchPreparingEvent(UUID.randomUUID());
        Exception exception = assertThrows(RuntimeException.class, () -> service.process(testEvent));

        verify(kafkaProducerMock, times(1)).send(eq("tracking.status"), any(TrackingStatusUpdated.class));
        assertThat(exception.getMessage(), equalTo("dispatch preparing producer failure"));
    }
}