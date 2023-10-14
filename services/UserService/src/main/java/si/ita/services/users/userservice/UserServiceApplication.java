package si.ita.services.users.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}

// generate classes
// protoc --plugin=protoc-gen-grpc-java="C:\Program Files\protoc\bin\protoc-gen-grpc-java.exe" --grpc-java_out=. --java_out=. messaging.proto
