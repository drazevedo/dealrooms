package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.repository.DealRoomRepository;
import br.com.drazevedo.dealrooms.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealRoomServiceImpl implements DealRoomService {

    private final DealRoomRepository repository;

    public DealRoomServiceImpl(DealRoomRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DealRoom> findAll() {
        return repository.findAll();
    }

    @Override
    public List<DealRoom> findByUser(User user) {
        return repository.findDealRoomsByViewersContaining(user);
    }

    @Override
    public DealRoom createRoom(String roomName) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        repository.findOneByName(roomName).ifPresent(dealRoom -> {
            throw new RuntimeException("Room already exists");
        });

        DealRoom room = new DealRoom();
        room.setName(roomName);
        room.getViewers().add(loggedUser);

        return repository.save(room);
    }
}
