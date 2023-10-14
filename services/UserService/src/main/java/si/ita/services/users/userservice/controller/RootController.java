package si.ita.services.users.userservice.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.ita.services.users.userservice.messaging.CreateRequest;
import si.ita.services.users.userservice.messaging.MessagingGrpc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class RootController {
    private static Boolean RUN_ON_DOCKER = false;
    private Logger logger = LoggerFactory.getLogger(RootController.class);

    @Value("${MESSAGING_SERVICE_PORT}")
    private int grpcServerPort;
    @Value("${MESSAGING_SERVICE_URL}")
    private String grpcServerHost;

    private ManagedChannel channel;

    @PostConstruct
    public void init() {
        if (RUN_ON_DOCKER) {
            logger.info("gRPC server host: " + grpcServerHost);
            logger.info("gRPC server port: " + grpcServerPort);

            channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                    .usePlaintext()
                    .build();
        }
    }

    @PreDestroy
    public void shutdown() {
        if (RUN_ON_DOCKER)
            channel.shutdown();
    }

    @CrossOrigin
    @RequestMapping("/")
    public String index() {
        logger.info("[GET]\t\t/");

        if (RUN_ON_DOCKER) {
            MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
            stub.sendMessageToQueue(CreateRequest.newBuilder()
                    .setQueue("ITA-UserService")
                    .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Root endpoint ([GET] '/')")
                    .build());
        }

        return "Welcome to the UserService API";
    }
}
