package com.lcaceres.service.impl;

import com.lcaceres.model.Student;
import com.lcaceres.repository.IGenericRepository;
import com.lcaceres.repository.IStudentRepository;
import com.lcaceres.service.IStudentService;
import com.lcaceres.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends CrudServiceImpl<Student, String> implements IStudentService {

    private final IStudentRepository repo;

    @Override
    protected IGenericRepository<Student, String> getRepo() {
        return repo;
    }


    @Override
    public Flux<Student> orderByAge(String key) {
        return repo.findAll()
                .sort(Constants.STRING_A.equalsIgnoreCase(key) ? Comparator.comparing(Student::getAge) : Comparator.comparing(Student::getAge).reversed());
    }
}
