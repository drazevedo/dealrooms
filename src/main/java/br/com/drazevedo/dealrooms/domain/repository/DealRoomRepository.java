package br.com.drazevedo.dealrooms.domain.repository;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealRoomRepository extends MongoRepository<DealRoom, String> {

    Optional<DealRoom> findOneByName(String name);

    List<DealRoom> findDealRoomsByViewersContaining(User user);
}