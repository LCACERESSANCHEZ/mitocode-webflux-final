package com.lcaceres.controller;

import com.lcaceres.model.Tuition;
import com.lcaceres.service.ITuitionService;
import com.lcaceres.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(Constants.URL_LICENSEPLATES)
@RequiredArgsConstructor
public class TuitionController {

    private final ITuitionService service;

    @PostMapping("/save")
    public Mono<ResponseEntity<Tuition>> save(@RequestBody Tuition tuition, final ServerHttpRequest req){
        return service.save(tuition)
                .map(e -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<Tuition>>> findAll(){
        Flux<Tuition> fx = service.findAll();

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

}
