package br.leetjourney.neighborshare.infrastructure.metrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BusinessMetrics {

    private final Counter reservationCreatedCounter;
    private final Timer reservationProcessingTimer;

    public BusinessMetrics(MeterRegistry registry) {
        this.reservationCreatedCounter = Counter.builder("neighborshare_reservations_created_total")
                .description("Total de reservas solicitadas no sistema")
                .tag("module", "reservation")
                .register(registry);

        this.reservationProcessingTimer = Timer.builder("neighborshare_reservation_processing_time_seconds")
                .description("Tempo de processamento da regra de negócio de reserva")
                .register(registry);
    }

    public void incrementReservations() {
        this.reservationCreatedCounter.increment();
    }

    public void recordReservationTime(long durationMillis) {
        this.reservationProcessingTimer.record(durationMillis, TimeUnit.MILLISECONDS);
    }
}
