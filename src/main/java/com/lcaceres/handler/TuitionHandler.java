package com.lcaceres.handler;

import com.lcaceres.model.Tuition;
import com.lcaceres.service.ITuitionService;
import com.lcaceres.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class TuitionHandler {

    private final ITuitionService service;

    private final RequestValidator requestValidator;

    public Mono<ServerResponse> save(ServerRequest req){
        Mono<Tuition> tuitionMono = req.bodyToMono(Tuition.class);

        return tuitionMono
                .flatMap(requestValidator::validate)
                .flatMap(service::save)
                .flatMap(tuition -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(tuition.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(tuition)));
    }

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Tuition.class);
    }

}
