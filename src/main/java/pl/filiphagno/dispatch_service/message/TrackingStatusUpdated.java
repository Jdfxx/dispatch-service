package pl.filiphagno.dispatch_service.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TrackingStatusUpdated {
    UUID orderId;
    Status status;
}
