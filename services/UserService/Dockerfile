FROM amazoncorretto:17.0.6

WORKDIR /app

COPY target/user-service-*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

# ./mvnw clean package -DskipTests

# docker build -t devminty/ita-users:1.2 .
# docker run -it -d -p 8080:8080 --name ITA-USERS devminty/ita-users:1.2
# docker push devminty/ita-users:1.2