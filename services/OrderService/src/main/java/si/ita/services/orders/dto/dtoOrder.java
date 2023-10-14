package si.ita.services.orders.dto;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class dtoOrder {
    public ObjectId userID;
    public List<dtoBuySkiCard> createSkiCards;
}
