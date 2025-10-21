package br.com.drazevedo.dealrooms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
@Import(TestMongoConfig.class)
class DealroomsApplicationTests {

    @Test
    void contextLoads() {
        // Default test made by Spring.io
	}
}