package pl.filiphagno.dispatch_service.services;

import org.springframework.stereotype.Service;
import pl.filiphagno.dispatch_service.message.OrderCreated;

@Service
public class DispatchService {
    public void process(OrderCreated payload) {};
}
