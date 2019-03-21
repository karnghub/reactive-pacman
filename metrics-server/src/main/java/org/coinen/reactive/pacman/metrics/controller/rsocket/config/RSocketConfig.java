package org.coinen.reactive.pacman.metrics.controller.rsocket.config;

import java.util.Optional;

import io.micrometer.influx.InfluxMeterRegistry;
import io.rsocket.SocketAcceptor;
import io.rsocket.rpc.RSocketRpcService;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import org.coinen.pacman.metrics.MetricsSnapshotHandlerServer;
import org.coinen.reactive.pacman.metrics.controller.rsocket.MetricsController;
import org.coinen.reactive.pacman.metrics.service.MetricsService;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RSocketConfig {

    @Bean
    public SocketAcceptor metricsSocketAcceptor(RequestHandlingRSocket requestHandlingRSocket) {
        return (setup, sendingSocket) -> Mono.just(requestHandlingRSocket);
    }

    @Bean
    @Scope("prototype")
    public RequestHandlingRSocket requestHandlingRSocket(
        RSocketRpcService[] rSocketRpcServices
    ) {
        return new RequestHandlingRSocket(rSocketRpcServices);
    }

    @Bean
    public MetricsSnapshotHandlerServer metricsSnapshotHandlerServer(
        MetricsService metricsService,
        InfluxMeterRegistry meterRegistry
    ) {
        return new MetricsSnapshotHandlerServer(
            new MetricsController(metricsService),
            Optional.of(meterRegistry),
            Optional.empty()
        );
    }
}
