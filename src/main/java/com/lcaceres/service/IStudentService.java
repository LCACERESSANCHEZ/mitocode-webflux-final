package com.lcaceres.service;

import com.lcaceres.model.Student;
import reactor.core.publisher.Flux;

public interface IStudentService extends ICrudService<Student, String> {

    Flux<Student> orderByAge(String key);
}
