package com.lcaceres.handler;

import com.lcaceres.model.Student;
import com.lcaceres.service.IStudentService;
import com.lcaceres.util.Constants;
import com.lcaceres.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final IStudentService service;

    private final RequestValidator requestValidator;

    public Mono<ServerResponse> save(ServerRequest req){
        Mono<Student> studentMono = req.bodyToMono(Student.class);

        return studentMono
                .flatMap(requestValidator::validate)
                .flatMap(service::save)
                .flatMap(student -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(student.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(student)));
    }

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Student.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req){
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(student -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(student))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> orderByAge(ServerRequest req){
        String key = req.pathVariable("key");
        Flux<Student> studentFlux = service.findAll()
                .sort(key.equalsIgnoreCase(Constants.STRING_A) ? Comparator.comparing(Student::getAge) : Comparator.comparing(Student::getAge).reversed());

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentFlux, Student.class);
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        Mono<Student> studentMono = req.bodyToMono(Student.class);
        Mono<Student> monoDB = service.findById(id);

        return monoDB.zipWith(studentMono, (db, sm) -> {
                    db.setId(id);
                    db.setNames(sm.getNames());
                    db.setSurnames(sm.getSurnames());
                    db.setDni(sm.getDni());
                    db.setAge(sm.getAge());

                    return db;
                })
                .flatMap(requestValidator::validate)
                .flatMap(service::update)
                .flatMap(student -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(student)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(student -> service.delete(student.getId())
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
