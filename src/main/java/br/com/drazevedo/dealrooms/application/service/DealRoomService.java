package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface DealRoomService {

    List<DealRoom> findAll();

    List<DealRoom> findByUser(User user);

    DealRoom createRoom(String roomName);
}
