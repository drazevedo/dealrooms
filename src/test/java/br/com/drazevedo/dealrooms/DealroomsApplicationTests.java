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
    @BeforeAll
    static void checkProfile() {
        System.out.println("🔍 Active profiles: " + System.getProperty("spring.profiles.active"));
    }

    @Test
    void contextLoads() {
	}
}