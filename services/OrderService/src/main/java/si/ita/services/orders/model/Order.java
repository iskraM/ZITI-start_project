package si.ita.services.orders.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MongoEntity(collection = "Orders")
public class Order extends PanacheMongoEntity {
    @BsonProperty("orderDate")
    public Date orderDate;
    @BsonProperty("userID")
    public ObjectId userID;
    @BsonProperty("skiCardIDs")
    public List<String> skiCardIDs;
    @BsonProperty("quantity")
    public Integer quantity;
    @BsonProperty("price")
    public Double price;

    public Order() {
        skiCardIDs = new ArrayList<>();
    }

    public Order(Date orderDate, ObjectId userID, List<String> skiCardIDs, Integer quantity, Double price) {
        this.orderDate = orderDate;
        this.userID = userID;
        this.skiCardIDs = skiCardIDs;
        this.quantity = quantity;
        this.price = price;
    }
}
