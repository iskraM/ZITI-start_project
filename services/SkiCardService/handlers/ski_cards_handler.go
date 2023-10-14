package handlers

import (
	"SkiCardService/models"
	"context"
	"math/rand"
	"net/http"
	"os"
	"strings"
	"time"

	"google.golang.org/grpc"
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"

	pb "SkiCardService/messaging"
)

var RUN_ON_DOCKER = false;

type SkiCardsHandler struct {
	DB         *mongo.Database
	Collection *mongo.Collection
}

func (h *SkiCardsHandler) GetSkiCards_Handler(ctx *gin.Context) {
	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "[GET] '/skicards' triggered",
		}

		// Send the request to the gRPC server
		_, err = client.SendMessageToQueue(ctx, req)
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
	}

	var skiCards []models.SkiCard

	cursor, err := h.Collection.Find(context.Background(), bson.M{})
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	if err := cursor.All(context.Background(), &skiCards); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	ctx.JSON(http.StatusOK, skiCards)
}

func (h *SkiCardsHandler) GetSkiCardById_Handler(ctx *gin.Context) {
	id := ctx.Param("id")

	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "[GET] '/skicards/" + id + "' triggered",
		}

		// Send the request to the gRPC server
		_, err = client.SendMessageToQueue(ctx, req)
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
	}

	objectId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	var skiCard *models.SkiCard
	if err := h.Collection.FindOne(context.Background(), bson.M{"_id": objectId}).Decode(&skiCard); err != nil {
		if err == mongo.ErrNoDocuments {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": "SkiCard not found"})
			return
		} else {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": "SkiCard not found"})
			return
		}
	}

	ctx.JSON(http.StatusOK, skiCard)
}

func (h *SkiCardsHandler) CreateSkiCard_Handler(ctx *gin.Context) {
	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "[POST] '/skicards' triggered",
		}

		// Send the request to the gRPC server
		_, err1 := client.SendMessageToQueue(ctx, req)
		if err1 != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err1.Error()})
		}
	}

	var skiCard models.SkiCard
	if err2 := ctx.ShouldBindJSON(&skiCard); err2 != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request payload. " + err2.Error()})
		return
	}

	skiCard.ID = primitive.NewObjectID()

	// generiram kodo
	rand.Seed(time.Now().UnixNano())
	first := randString(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
	second := randString(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
	third := randString(3, "0123456789")

	skiCard.Code = strings.Join([]string{first, second, third}, "-")

	// nastavim datum na trenutni
	skiCard.BuyDate = time.Now()

	skiCard.ActivateDate = nil

	_, err3 := h.Collection.InsertOne(context.Background(), skiCard)

	if err3 != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to create SkiCard. " + err3.Error()})
		return
	}

	ctx.JSON(http.StatusCreated, skiCard)
}

func (h *SkiCardsHandler) ActivateSkiCard_Handler(ctx *gin.Context) {
	ski_card_code := ctx.Param("ski_card_code")
	physical_card_code := ctx.Param("physical_card_code")

	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "[PUT] '/skicards/" + ski_card_code + "/" + physical_card_code + "' triggered",
		}

		// Send the request to the gRPC server
		_, err = client.SendMessageToQueue(ctx, req)
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
	}

	// Poiščem skicard
	var ski_card models.SkiCard
	if err := h.Collection.FindOne(context.Background(), bson.M{"code": ski_card_code}).Decode(&ski_card); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to activate SkiCard. " + err.Error()})
		return
	}

	// Poiščem fizično kartico
	pch := PhysicalCardsHandler{DB: h.DB, Collection: h.DB.Collection(os.Getenv("PHYSICAL_CARDS_COLLECTION_NAME"))}
	physical_card, err := pch.GetPhysicalCardByCode(physical_card_code)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to activate SkiCard. " + err.Error()})
		return
	}

	// Aktiviram skicard
	ski_card.IsActive = true
	t1 := time.Now()
	ski_card.ActivateDate = &t1

	if err := h.Collection.FindOneAndUpdate(context.Background(), bson.M{"_id": ski_card.ID}, bson.M{"$set": ski_card}).Decode(&ski_card); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to activate SkiCard. " + err.Error()})
		return
	}

	// Aktiviram fizično kartico
	physical_card.SkiCardID = &ski_card.ID
	t2 := time.Now().Add(time.Duration(ski_card.ActiveTime) * time.Hour)
	physical_card.ActiveUntil = &t2

	err = pch.UpdatePhysicalCard(physical_card.ID.Hex(), physical_card)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to activate SkiCard. " + err.Error()})
		return
	}

	ctx.JSON(http.StatusOK, gin.H{"message": "SkiCard activated successfully."})
}

func (h *SkiCardsHandler) DeactivateSkiCard_Handler(ctx *gin.Context) {
	physical_card_code := ctx.Param("physical_card_code")

	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "[DELETE] '/skicards/" + physical_card_code + "' triggered",
		}

		// Send the request to the gRPC server
		_, err = client.SendMessageToQueue(ctx, req)
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
	}

	// Poiščem fizično kartico in jo deaktiviram
	pch := PhysicalCardsHandler{DB: h.DB, Collection: h.DB.Collection(os.Getenv("PHYSICAL_CARDS_COLLECTION_NAME"))}
	physical_card, err := pch.GetPhysicalCardByCode(physical_card_code)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to deactivate SkiCard. " + err.Error()})
		return
	}

	// izbrišem skicard
	_, err = h.Collection.DeleteOne(context.Background(), bson.M{"_id": physical_card.SkiCardID})
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to deactivate SkiCard. " + err.Error()})
		return
	}

	// posodobim fizično kartico
	physical_card.SkiCardID = nil
	physical_card.ActiveUntil = nil

	// posodobim fizično kartico
	err = pch.UpdatePhysicalCard(physical_card.ID.Hex(), physical_card)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to deactivate SkiCard. " + err.Error()})
		return
	}

	ctx.JSON(http.StatusOK, gin.H{"message": "SkiCard deactivated successfully."})
}
