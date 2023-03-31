package com.lcaceres.repository;

import com.lcaceres.model.User;
import reactor.core.publisher.Mono;

public interface IUserRepository extends IGenericRepository<User, String>{

    //@Query("{username: ?}")
    Mono<User> findOneByUsername(String username);
}
