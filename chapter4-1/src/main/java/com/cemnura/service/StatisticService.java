package com.cemnura.service;

import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.*;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;
import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Vector;

public class StatisticService implements Service {

    private static List<BigDecimal> records = new Vector<>();
    private static final Logger logger = LogManager.getLogger(StatisticService.class);
    // tag::fields[]
    private static final Counter COUNTER = Counter  // <1>
            .build("access_counter", "Shows the numbers of total request")
            .register();

    private static final Gauge INPROGRESS_GAUGE = Gauge // <2>
            .build("inprogress_gauge", "Shows amount of requests in progress")
            .register();

    private static final Histogram REQUEST_DURATION = Histogram // <3>
            .build("request_duration", "Shows the average duration of response")
            .register();
    // end::fields[]
    @Override
    public void update(Routing.Rules rules)
    {
        rules
                .any(this::startTimer)
                .any(this::incProgressGauge)
                .any(this::incrementAccessCnt)
                .any(this::logEvent)
                .register(JsonSupport.create())
                .post("/record", Handler.create(JsonObject.class, this::postStatistic))
                .get("/average", this::getAverage)
                .get("/max", this::getMax)
                .get("/min", this::getMin)
                .any(this::decProgressGauge)
                ;

    }
    // tag::snippets[]
    private void startTimer(ServerRequest req, ServerResponse res)
    {
        REQUEST_DURATION.time(() -> req.next());
    }

    private void incProgressGauge(ServerRequest req, ServerResponse res)
    {
        INPROGRESS_GAUGE.inc();
        req.next();
    }

    private void decProgressGauge(ServerRequest req, ServerResponse res)
    {
        INPROGRESS_GAUGE.dec();
    }

    private void incrementAccessCnt(ServerRequest req, ServerResponse res)
    {
        COUNTER.inc();
        req.next();
    }
    // end::snippets[]

    private void logEvent(ServerRequest req, ServerResponse res)
    {
        logger.info("Retrieved {} request", COUNTER.get());
        req.next();
    }


    private void postStatistic(ServerRequest req, ServerResponse res, JsonObject jsonObject)
    {
        BigDecimal value = jsonObject.getJsonNumber("value").bigDecimalValue();

        records.add(value);

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
        req.next();
    }

}
