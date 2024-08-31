package eh13.til.demo_mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MeetingRepository extends ReactiveMongoRepository<Meeting, String> {

    Flux<Meeting> findByLocationIgnoreCase(String location);
}
