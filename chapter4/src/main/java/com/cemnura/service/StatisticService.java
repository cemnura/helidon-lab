package com.cemnura.service;

import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.metrics.MetricsSupport;
import io.helidon.metrics.RegistryFactory;
import io.helidon.webserver.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.metrics.*;

import javax.json.JsonObject;
import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Vector;

public class StatisticService implements Service {

    private static List<BigDecimal> records = new Vector<>();
    private static final Logger logger = LogManager.getLogger(StatisticService.class);

    private static RegistryFactory registryFactory = RegistryFactory.create();

    private static MetricRegistry appRegistry = registryFactory.getRegistry(MetricRegistry.Type.APPLICATION); // <1>
    private final Timer accessTimer = appRegistry.timer("timer"); // <2>
    private final Counter accessCtr = appRegistry.counter("access_counter"); // <3>
    private final Histogram histogram = appRegistry.histogram("histogram"); // <4>

    @Override
    public void update(Routing.Rules rules)
    {
        rules
                .register(MetricsSupport.builder()
                        .registryFactory(registryFactory) // <5>
                        .build())
                .any(this::startTime)
                .any(this::incrementAccessCnt)
                .any(this::logEvent)
                .register(JsonSupport.create())
                .post("/record", Handler.create(JsonObject.class, this::postStatistic))
                .get("/average", this::getAverage)
                .get("/max", this::getMax)
                .get("/min", this::getMin)
                ;

    }


    private void startTime(ServerRequest req, ServerResponse res)   // <6>
    {
        accessTimer.time(() -> req.next());
    }

    private void incrementAccessCnt(ServerRequest req, ServerResponse res)  // <7>
    {
        accessCtr.inc();
        req.next();
    }

    private void updateHistogram(BigDecimal value) // <8>
    {
        histogram.update(value.longValue());
    }

    private void logEvent(ServerRequest req, ServerResponse res)
    {
        logger.info("Retrieved {} request", accessCtr.getCount());
        req.next();
    }


    private void postStatistic(ServerRequest req, ServerResponse res, JsonObject jsonObject)
    {
        BigDecimal value = jsonObject.getJsonNumber("value").bigDecimalValue();

        records.add(value);
        updateHistogram(value);

        res.send("Recorded : "  + value);
    }

    private void getAverage(ServerRequest req, ServerResponse res)
    {
        OptionalDouble avg = records.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .average();

        res.send(avg.toString());
    }

    private void getMax(ServerRequest req, ServerResponse res)
    {
        res.send(records.stream().mapToDouble(BigDecimal::doubleValue).max().toString());
    }

    private void getMin(ServerRequest req, ServerResponse res)
    {
        res.send(records.stream().mapToDouble(BigDecimal::doubleValue).min().toString());
    }

}
