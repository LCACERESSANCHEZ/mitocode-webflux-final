package com.lcaceres.service.impl;

import com.lcaceres.model.User;
import com.lcaceres.repository.IGenericRepository;
import com.lcaceres.repository.IRoleRepository;
import com.lcaceres.repository.IUserRepository;
import com.lcaceres.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CrudServiceImpl<User, String> implements IUserService {

    private final IUserRepository repo;
    private final IRoleRepository rolRepo;
    private final BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepository<User, String> getRepo() {
        return repo;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public Mono<com.lcaceres.security.model.User> searchByUser(String username) {
        Mono<User> monoUser = repo.findOneByUsername(username);
        List<String> roles = new ArrayList<>();

        return monoUser.flatMap(u -> {
            return Flux.fromIterable(u.getRoles())
                    .flatMap(rol -> {
                        return rolRepo.findById(rol.getId())
                                .map(r -> {
                                    roles.add(r.getName());
                                    return r;
                                });
                    }).collectList().flatMap(list -> {
                        u.setRoles(list);
                        return Mono.just(u);
                    });
        }).flatMap(u -> Mono.just(new com.lcaceres.security.model.User(u.getUsername(), u.getPassword(), u.getStatus(), roles)));
    }
}
