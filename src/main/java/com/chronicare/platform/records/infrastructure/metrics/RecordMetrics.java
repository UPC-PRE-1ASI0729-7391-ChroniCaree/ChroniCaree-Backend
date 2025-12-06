package com.chronicare.platform.records.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordMetrics {

    private final MeterRegistry meterRegistry;

    public void incrementRecordsCreated(String recordType) {
        Counter.builder("records.created.count")
                .tag("type", recordType)
                .description("Number of medical records created")
                .register(meterRegistry)
                .increment();
    }

    public void incrementRecordsUpdated(String recordType) {
        Counter.builder("records.updated.count")
                .tag("type", recordType)
                .description("Number of medical records updated")
                .register(meterRegistry)
                .increment();
    }

    public void incrementRecordsDeleted(String recordType, boolean hard) {
        Counter.builder("records.deleted.count")
                .tag("type", recordType)
                .tag("hard", String.valueOf(hard))
                .description("Number of medical records deleted")
                .register(meterRegistry)
                .increment();
    }

    public void incrementRecordsAccessed(String recordType) {
        Counter.builder("records.access.count")
                .tag("type", recordType)
                .description("Number of medical record accesses")
                .register(meterRegistry)
                .increment();
    }

    public void incrementRecordsExported(String format) {
        Counter.builder("records.export.count")
                .tag("format", format)
                .description("Number of medical record exports")
                .register(meterRegistry)
                .increment();
    }

    public void incrementVersionsCreated() {
        Counter.builder("records.versions.created.count")
                .description("Number of record versions created")
                .register(meterRegistry)
                .increment();
    }

    public void incrementConsentsGranted() {
        Counter.builder("records.consents.granted.count")
                .description("Number of record consents granted")
                .register(meterRegistry)
                .increment();
    }

    public void incrementConsentsRevoked() {
        Counter.builder("records.consents.revoked.count")
                .description("Number of record consents revoked")
                .register(meterRegistry)
                .increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordQueryTime(Timer.Sample sample, String operationType) {
        sample.stop(Timer.builder("records.query.time")
                .tag("operation", operationType)
                .description("Time taken to execute record queries")
                .register(meterRegistry));
    }

    public void recordCommandTime(Timer.Sample sample, String commandType) {
        sample.stop(Timer.builder("records.command.time")
                .tag("command", commandType)
                .description("Time taken to execute record commands")
                .register(meterRegistry));
    }
}
