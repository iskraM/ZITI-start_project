package si.ita.services.users.userservice.service;

import org.springframework.stereotype.Service;
import si.ita.services.users.userservice.model.User;
import si.ita.services.users.userservice.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User addUser(User user) {
        return userRepository.insert(user);
    }

    public User updateUser(User user) {
        User userToUpdate = userRepository.findById(user.getID())
                .orElseThrow(() -> new RuntimeException(
                        String.format("User with ID: %s not found!", user.getID())));

        userToUpdate.setUsername((user.getUsername() != null && !user.getUsername().isBlank()) ? user.getUsername() : userToUpdate.getUsername());
        userToUpdate.setPassword((user.getPassword() != null && !user.getPassword().isBlank())  ? user.getPassword() : userToUpdate.getPassword());
        userToUpdate.setEmail((user.getEmail() != null && !user.getEmail().isBlank()) ? user.getEmail() : userToUpdate.getEmail());

        return userRepository.save(userToUpdate);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersInAgeRange(int min, int max) {
        return userRepository.findByAgeRange(min, max)
                .orElseThrow(() -> new RuntimeException(
                        String.format("No users in age range: %d - %d", min, max)));
    }

    public void deleteUser(String ID) {
        userRepository.deleteById(ID);
    }
}
