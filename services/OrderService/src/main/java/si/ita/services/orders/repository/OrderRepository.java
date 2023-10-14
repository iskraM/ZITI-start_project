package si.ita.services.orders.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import org.bson.types.ObjectId;
import si.ita.services.orders.model.Order;

import javax.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheMongoRepository<Order> {
    public List<Order> findByUserID(ObjectId userID) {
        return find("userID", userID).list();
    }

    public List<Order> getOrderedByDate() {
        return listAll(Sort.by("orderDate"));
    }
}
