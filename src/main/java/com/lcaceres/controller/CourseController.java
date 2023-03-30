package com.lcaceres.controller;

import com.lcaceres.model.Course;
import com.lcaceres.model.Student;
import com.lcaceres.service.ICourseService;
import com.lcaceres.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(Constants.URL_COURSES)
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService service;

    @PostMapping("/save")
    public Mono<ResponseEntity<Course>> save(@RequestBody Course course, final ServerHttpRequest req){
        return service.save(course)
                .map(e -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<Course>>> findAll(){
        Flux<Course> fx = service.findAll();

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/findById/{id}")
    public Mono<ResponseEntity<Course>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Course>> update(@PathVariable("id") String id, @RequestBody Course course) {
        course.setId(id);

        Mono<Course> monoBody = Mono.just(course);
        Mono<Course> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, b) -> {
                    db.setId(id);
                    db.setName(b.getName());
                    db.setAcronym(b.getAcronym());
                    db.setState(b.getState());
                    return db;
                })
                .flatMap(service::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return service.findById(id)
                .flatMap(e -> service.delete(e.getId())
                        .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
