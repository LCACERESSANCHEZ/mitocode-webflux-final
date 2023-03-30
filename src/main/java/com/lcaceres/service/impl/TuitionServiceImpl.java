package com.lcaceres.service.impl;

import com.lcaceres.model.Tuition;
import com.lcaceres.repository.IGenericRepository;
import com.lcaceres.repository.ITuitionRepository;
import com.lcaceres.service.ITuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TuitionServiceImpl extends CrudServiceImpl<Tuition, String> implements ITuitionService {

    private final ITuitionRepository repo;

    @Override
    protected IGenericRepository<Tuition, String> getRepo() {
        return repo;
    }
}