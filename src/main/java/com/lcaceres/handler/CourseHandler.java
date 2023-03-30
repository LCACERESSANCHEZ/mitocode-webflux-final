package com.lcaceres.handler;

import com.lcaceres.model.Course;
import com.lcaceres.service.ICourseService;
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
public class CourseHandler {

    private final ICourseService service;

    //private final IcourseService requestValidator;

    public Mono<ServerResponse> save(ServerRequest req){
        Mono<Course> courseMono = req.bodyToMono(Course.class);

        return courseMono
                //.flatMap(requestValidator::validate)
                .flatMap(service::save)
                .flatMap(course -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(course.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(course)));
    }

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Course.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req){
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(course -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(course))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        Mono<Course> courseMono = req.bodyToMono(Course.class);
        Mono<Course> monoDB = service.findById(id);

        return monoDB.zipWith(courseMono, (db, sm) -> {
                    db.setId(id);
                    db.setName(sm.getName());
                    db.setAcronym(sm.getAcronym());
                    db.setState(sm.getState());

                    return db;
                })
                //.flatMap(requestValidator::validate)
                .flatMap(service::update)
                .flatMap(course -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(course)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(course -> service.delete(course.getId())
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
}
