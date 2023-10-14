package si.ita.services.orders;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class MyApplication implements QuarkusApplication {
    public static void main(String[] args) {
        Quarkus.run(MyApplication.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        Quarkus.waitForExit();
        return 0;
    }
}

// protoc --plugin=protoc-gen-grpc-java="C:\Program Files\protoc\bin\protoc-gen-grpc-java.exe" --grpc-java_out=. --java_out=. messaging.proto
