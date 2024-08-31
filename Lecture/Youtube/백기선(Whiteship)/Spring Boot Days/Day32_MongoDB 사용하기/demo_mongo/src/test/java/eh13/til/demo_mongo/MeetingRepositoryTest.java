package eh13.til.demo_mongo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class MeetingRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.14");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @Autowired
    MeetingRepository meetingRepository;

    @Test
    void findByLocationIgnoreCase() {
        // Given
        Meeting meeting = new Meeting();
        meeting.setTitle("new meeting");
        meeting.setLocation("Redmond");

        meetingRepository.save(meeting).block();

        Meeting mongoMeeting = new Meeting();
        mongoMeeting.setTitle("mongo study");
        mongoMeeting.setLocation("Seattle");

        meetingRepository.save(mongoMeeting).block();

        // When
        List<Meeting> result = meetingRepository.findByLocationIgnoreCase("seattle").collectList().block();

        //Then
        assertEquals(1, result.size());
        assertTrue(result.contains(mongoMeeting));

    }

}