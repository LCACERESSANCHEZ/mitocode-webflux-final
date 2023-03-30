package com.lcaceres.controller;

import com.lcaceres.model.Student;
import com.lcaceres.service.IStudentService;
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
@RequestMapping(Constants.URL_STUDENTS)
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService service;

    @PostMapping("/save")
    public Mono<ResponseEntity<Student>> save(@RequestBody Student student,final ServerHttpRequest req){
        return service.save(student)
                .map(e -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<Student>>> findAll(){
        Flux<Student> fx = service.findAll();

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/findById/{id}")
    public Mono<ResponseEntity<Student>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Student>> update(@PathVariable("id") String id, @RequestBody Student student) {
        student.setId(id);

        Mono<Student> monoBody = Mono.just(student);
        Mono<Student> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, b) -> {
                    db.setId(id);
                    db.setNames(b.getNames());
                    db.setSurnames(b.getSurnames());
                    db.setAge(b.getAge());
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

    @GetMapping("/findAll/orderByAge/{key}")//order (A = ascendente , A != descendente)
    public Mono<ResponseEntity<Flux<Student>>> orderByAge(@PathVariable ("key") String key){
        Flux<Student> fx = service.orderByAge(key);

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

}
