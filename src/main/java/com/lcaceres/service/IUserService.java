package com.lcaceres.service;

import com.lcaceres.model.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICrudService<User, String>{

    Mono<User> saveHash(User user);
    Mono<com.lcaceres.security.model.User> searchByUser(String username);
}
