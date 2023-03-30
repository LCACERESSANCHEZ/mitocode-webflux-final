package com.lcaceres.service.impl;

import com.lcaceres.repository.IGenericRepository;
import com.lcaceres.service.ICrudService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CrudServiceImpl<T, ID> implements ICrudService<T, ID> {

    protected abstract IGenericRepository<T, ID> getRepo();

    @Override
    public Mono<T> save(T t) {
        return getRepo().save(t);
    }

    @Override
    public Mono<T> update(T t) {
        return getRepo().save(t);
    }

    @Override
    public Flux<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepo().findById(id);
    }

    @Override
    public Mono<Void> delete(ID id) {
        return getRepo().deleteById(id);
    }
}
