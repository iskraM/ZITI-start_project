package si.ita.services.users.userservice.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.ita.services.users.userservice.messaging.CreateRequest;
import si.ita.services.users.userservice.messaging.MessagingGrpc;
import si.ita.services.users.userservice.model.User;
import si.ita.services.users.userservice.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static Boolean RUN_ON_DOCKER = false;

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Value("${MESSAGING_SERVICE_PORT}")
    private int grpcServerPort;
    @Value("${MESSAGING_SERVICE_URL}")
    private String grpcServerHost;
    private ManagedChannel channel;

    @PostConstruct
    public void init() {
        if (RUN_ON_DOCKER) {
            channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                    .usePlaintext()
                    .build();
        }
    }

    @PreDestroy
    public void shutdown() {
        if (RUN_ON_DOCKER) {
            channel.shutdown();
        }
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // login user
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user) {
        try {
            // log request body contents
            logger.info("[POST]\t\t/users/login");

            if (RUN_ON_DOCKER) {
                MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
                stub.sendMessageToQueue(CreateRequest.newBuilder()
                        .setQueue("ITA-UserService")
                        .setMessage(String.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                                .format(LocalDateTime.now()) + ": Login endpoint ([POST] '/users/login') with username: %s and password: %s", user.getUsername(), user.getPassword()))
                        .build());
            }

            if (userService.getAllUsers().stream().anyMatch(u -> u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()))) {
                var match = userService.getAllUsers().stream().filter(u -> u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())).findFirst().get();
                match.setPassword(null);

                return new ResponseEntity<User>(match, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Invalid username or password", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(String.format("{\"error\": \"%s\"}", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {
        try {
            logger.info("[POST]\t\t/users/register");

            if (RUN_ON_DOCKER) {
                MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
                stub.sendMessageToQueue(CreateRequest.newBuilder()
                        .setQueue("ITA-UserService")
                        .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Register endpoint ([POST] '/users/register')")
                        .build());
            }

            if (userService.getAllUsers().stream().findAny().filter(u -> u.getUsername().equals(user.getUsername())).isPresent()) {
                return new ResponseEntity<String>("User with given username already exists!", HttpStatus.BAD_REQUEST);
            }
            else if (userService.getAllUsers().stream().findAny().filter(u -> u.getEmail().equals(user.getEmail())).isPresent()) {
                return new ResponseEntity<String>("Given email is already in use!", HttpStatus.BAD_REQUEST);
            } else {
                if (user.getName() != null && user.getSurname() != null && user.getEmail() != null && user.getAge() != null && user.getUsername() != null && user.getPassword() != null) {
                    return new ResponseEntity<User>(userService.addUser(user), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<String>("Invalid user data", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(String.format("{\"error\": \"%s\"}", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity updateUser(@RequestBody User user) {
        try {
            logger.info("[PUT]\t\t/users");

            if (RUN_ON_DOCKER) {
                MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
                stub.sendMessageToQueue(CreateRequest.newBuilder()
                        .setQueue("ITA-UserService")
                        .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Update endpoint ([PUT] '/users' updating user with id: " + user.getID() + ")")
                        .build());
            }

            if (user.getID() != null && (user.getUsername() != null || user.getPassword() != null || user.getEmail() != null)) {
                return ResponseEntity.ok(userService.updateUser(user));
            } else {
                return new ResponseEntity<String>("Invalid user data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(String.format("{\"error\": \"%s\"}", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            logger.info("[GET]\t\t/users");
            if (RUN_ON_DOCKER) {
                MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
                stub.sendMessageToQueue(CreateRequest.newBuilder()
                        .setQueue("ITA-UserService")
                        .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Get all users endpoint ([GET] '/users')")
                        .build());
            }

            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return new ResponseEntity(String.format("{\"error\": \"%s\"}", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping("/age_range/{min}/{max}")
    public ResponseEntity<List<User>> getUsersInAgeRange(@PathVariable int min, @PathVariable int max) {
        try {
            logger.info(String.format("[GET]\t\t/users/age_range/%d/%d", min, max));

            if (RUN_ON_DOCKER) {
                MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
                stub.sendMessageToQueue(CreateRequest.newBuilder()
                        .setQueue("ITA-UserService")
                        .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + String.format(": Get users in age range endpoint ([GET] '/users/age_range/%d/%d')", min, max))
                        .build());
            }

            return ResponseEntity.ok(userService.getUsersInAgeRange(min, max));
        } catch (Exception e) {
            return new ResponseEntity(String.format("{\"error\": \"%s\"}", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @DeleteMapping("/{ID}")
    public ResponseEntity deleteUser(@PathVariable String ID) {
        try {
            logger.info(String.format("[DELETE]\t\t/users/%s", ID));

            if (RUN_ON_DOCKER) {
                MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
                stub.sendMessageToQueue(CreateRequest.newBuilder()
                        .setQueue("ITA-UserService")
                        .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Delete user endpoint ([DELETE] '/users/" + ID + "')")
                        .build());
            }

            userService.deleteUser(ID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return new ResponseEntity<String>(String.format("{\"error\": \"%s\"}", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}