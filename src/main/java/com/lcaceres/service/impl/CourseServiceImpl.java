package com.lcaceres.service.impl;

import com.lcaceres.model.Course;
import com.lcaceres.repository.ICourseRepository;
import com.lcaceres.repository.IGenericRepository;
import com.lcaceres.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CrudServiceImpl<Course, String> implements ICourseService {

    private final ICourseRepository repo;

    @Override
    protected IGenericRepository<Course, String> getRepo() {
        return repo;
    }

}
