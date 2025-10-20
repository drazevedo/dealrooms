package br.com.drazevedo.dealrooms.domain.repository;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DealRoomRepositoryTests {

    @Autowired
    private FolderRepository repository;

    @Test
    void shoudCreateDealRoom() {
        DealRoom room = new DealRoom(null, "Room Name");
        DealRoom saved = repository.save(room);

        Assertions.assertNotNull(saved.getId());
    }
}
