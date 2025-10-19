package br.com.drazevedo.dealrooms.domain.repository;

import br.com.drazevedo.dealrooms.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByUsername(String username);
}