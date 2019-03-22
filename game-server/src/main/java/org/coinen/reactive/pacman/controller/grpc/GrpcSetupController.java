package org.coinen.reactive.pacman.controller.grpc;

import com.google.protobuf.Empty;
import io.micrometer.core.instrument.MeterRegistry;
import io.rsocket.rpc.metrics.Metrics;
import org.coinen.pacman.Map;
import org.coinen.pacman.ReactorSetupServiceGrpc;
import org.coinen.reactive.pacman.service.MapService;
import org.lognet.springboot.grpc.GRpcService;
import reactor.core.publisher.Mono;

@GRpcService
public class GrpcSetupController extends ReactorSetupServiceGrpc.SetupServiceImplBase {

    final MapService mapService;
    final MeterRegistry registry;

    public GrpcSetupController(MapService mapService, MeterRegistry registry) {
        this.mapService = mapService;
        this.registry = registry;
    }

    @Override
    public Mono<Map> get(Mono<Empty> request) {
        return Mono.just(mapService.getMap())
                   .transform(Metrics.<Map>timed(registry, "grpc.server", "service", org.coinen.pacman.SetupService.SERVICE, "method", org.coinen.pacman.SetupService.METHOD_GET));
    }

}
