package si.ita.services.users.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import si.ita.services.users.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{'age': {$gte: ?0, $lte: ?1}}")
    Optional<List<User>> findByAgeRange(int min, int max);
}
