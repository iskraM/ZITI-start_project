package si.ita.services.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import si.ita.services.orders.dto.dtoOrder;
import si.ita.services.orders.messaging.CreateRequest;
import si.ita.services.orders.messaging.MessagingGrpc;
import si.ita.services.orders.model.Order;
import si.ita.services.orders.repository.OrderRepository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Path("/orders")
public class OrderController {
    private static Boolean RUN_ON_DOCKER = false;

    @Inject
    OrderRepository orderRepository;
    Logger LOGGER = Logger.getLogger(OrderController.class.getName());

    public ManagedChannel getChannel() {
        String grpcServerHost = System.getenv("MESSAGING_SERVICE_URL");
        int grpcServerPort = Integer.parseInt(System.getenv("MESSAGING_SERVICE_PORT"));

        LOGGER.info("gRPC server host: " + grpcServerHost);
        LOGGER.info("gRPC server port: " + grpcServerPort);

        return ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                .usePlaintext()
                .build();
    }

    // enable CORS

    @GET
    public Response getAllOrdersSortByDate() {
        LOGGER.info("[GET]\t/orders");

        if (RUN_ON_DOCKER) {
            var channel = getChannel();
            MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
            stub.sendMessageToQueue(CreateRequest.newBuilder()
                    .setQueue("ITA-OrderService")
                    .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Get all orders endpoint ([GET] '/orders')")
                    .build());

            channel.shutdown();
        }

        return Response.ok(orderRepository.getOrderedByDate()).build();
    }

    @GET
    @Path("/{order_id}")
    public Response getOrder(@PathParam("order_id") ObjectId orderId) {
        LOGGER.info("[GET]\t/orders/" + orderId);

        if (RUN_ON_DOCKER) {
            var channel = getChannel();
            MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
            stub.sendMessageToQueue(CreateRequest.newBuilder()
                    .setQueue("ITA-OrderService")
                    .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Get order endpoint ([GET] '/orders/" + orderId + "')")
                    .build());

            channel.shutdown();
        }

        return Response.ok(orderRepository.findById(orderId)).build();
    }

    @GET
    @Path("/owned/{user_id}")
    public Response getAllUserOrders(@PathParam("user_id") ObjectId userId) {
        LOGGER.info("[GET]\t/orders/owned/" + userId);

        if (RUN_ON_DOCKER) {
            var channel = getChannel();
            MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
            stub.sendMessageToQueue(CreateRequest.newBuilder()
                    .setQueue("ITA-OrderService")
                    .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Get all user orders endpoint ([GET] '/orders/owned/" + userId + "')")
                    .build());

            channel.shutdown();
        }

        return Response.ok(orderRepository.findByUserID(userId)).build();
    }

    @POST
    public Response createOrder(dtoOrder order) throws URISyntaxException {
        LOGGER.info("[POST]\t/orders");

        if (RUN_ON_DOCKER) {
            var channel = getChannel();
            MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
            stub.sendMessageToQueue(CreateRequest.newBuilder()
                    .setQueue("ITA-OrderService")
                    .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Create order endpoint ([POST] '/orders')")
                    .build());

            channel.shutdown();
        }

        if (order.userID == null || order.createSkiCards == null || order.createSkiCards.size() == 0)
            return Response.status(400).entity("Empty body").build();

        String api_url = System.getenv("SKI_CARD_SERVICE_URL");

        if (api_url == null) {
            LOGGER.error("Ski card service url not set");
            return Response.status(500).entity("Ski card service url not set").build();
        }

        LOGGER.info("URL: " + api_url);

        try {
            URL url = new URL(api_url + "/skicards");

            Order newOrder = new Order();
            for (var card : order.createSkiCards) {
                var body = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(card);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                con.getOutputStream().write(body.getBytes());

                var responseCode = con.getResponseCode();

                LOGGER.info("Response code: " + responseCode);

                if (responseCode != 201)
                    return Response.status(responseCode).entity("Error while creating ski card").build();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                var id = response.toString().split(":")[1].split(",")[0].replace("\"", "");

                newOrder.skiCardIDs.add(id);
            }

            // set today date to new order
            newOrder.orderDate = new Date();
            newOrder.userID = order.userID;
            newOrder.quantity = order.createSkiCards.size();
            newOrder.price = order.createSkiCards.stream().mapToDouble(el -> el.price).sum();

            orderRepository.persist(newOrder);
            return Response.created(new URI("/orders/" + newOrder.id)).build();
        } catch (Exception ex) {
            LOGGER.error("ERROR: " + ex.getMessage());
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{order_id}")
    public Response deleteOrder(@PathParam("order_id") ObjectId orderId) {
        try {
            LOGGER.info("[DELETE]\t/orders/" + orderId);

            if (RUN_ON_DOCKER) {
                var channel = getChannel();
                MessagingGrpc.MessagingBlockingStub stub = MessagingGrpc.newBlockingStub(channel);
                stub.sendMessageToQueue(CreateRequest.newBuilder()
                        .setQueue("ITA-OrderService")
                        .setMessage(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + ": Delete order endpoint ([DELETE] '/orders/" + orderId + "')")
                        .build());

                channel.shutdown();
            }

            orderRepository.deleteById(orderId);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
