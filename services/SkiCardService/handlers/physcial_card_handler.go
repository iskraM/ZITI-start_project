package handlers

import (
	"SkiCardService/models"
	"context"
	"errors"
	"math/rand"
	"net/http"
	"strings"
	"time"
	"os"

	"google.golang.org/grpc"
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"

	pb "SkiCardService/messaging"
)

type PhysicalCardsHandler struct {
	DB         *mongo.Database
	Collection *mongo.Collection
}

// methods
func (h *PhysicalCardsHandler) GetPhysicalCardById(id string) (*models.PhysicalCard, error) {
	var physicalCard *models.PhysicalCard

	objectId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return nil, err
	}

	if err := h.Collection.FindOne(context.Background(), bson.M{"_id": objectId}).Decode(&physicalCard); err != nil {
		if err == mongo.ErrNoDocuments {
			return nil, errors.New("PhysicalCard not found")
		} else {
			return nil, err
		}
	}

	return physicalCard, nil
}

func (h *PhysicalCardsHandler) UpdatePhysicalCard(id string, physicalCard *models.PhysicalCard) error {
	objectId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return err
	}

	_, err = h.Collection.UpdateOne(context.Background(), bson.M{"_id": objectId}, bson.M{"$set": physicalCard})

	return err
}

func (h *PhysicalCardsHandler) GetPhysicalCardByCode(code string) (*models.PhysicalCard, error) {
	var physicalCard *models.PhysicalCard

	if err := h.Collection.FindOne(context.Background(), bson.M{"code": code}).Decode(&physicalCard); err != nil {
		if err == mongo.ErrNoDocuments {
			return nil, errors.New("PhysicalCard not found")
		} else {
			return nil, err
		}
	}

	return physicalCard, nil
}

// endpoints
func (h *PhysicalCardsHandler) GetPhysicalCardByCode_Handler(ctx *gin.Context) {
	code := ctx.Param("physical_card_code")

	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "[GET] '/physicalcards/" + code + "' triggered",
		}

		// Send the request to the gRPC server
		_, err = client.SendMessageToQueue(ctx, req)
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
	}
	

	physicalCard, err := h.GetPhysicalCardByCode(code)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	ctx.JSON(http.StatusOK, physicalCard)
}

func (h *PhysicalCardsHandler) CreatePhysicalCard_Handler(ctx *gin.Context) {
	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "[POST] '/physicalcards' triggered",
		}

		// Send the request to the gRPC server
		_, err = client.SendMessageToQueue(ctx, req)
		if err != nil {
			ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		}
	}

	var physicalCard models.PhysicalCard
	if err1 := ctx.ShouldBindJSON(&physicalCard); err1 != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request payload. " + err1.Error()})
		return
	}
	

	physicalCard.ID = primitive.NewObjectID()

	// generiram kodo
	rand.Seed(time.Now().UnixNano())
	first := randString(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
	second := randString(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
	third := randString(3, "0123456789")

	physicalCard.Code = strings.Join([]string{first, second, third}, "-")

	physicalCard.SkiCardID = nil
	physicalCard.ActiveUntil = nil

	_, err2 := h.Collection.InsertOne(context.Background(), physicalCard)

	if err2 != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to create PhysicalCard. " + err2.Error()})
		return
	}
	ctx.JSON(http.StatusCreated, physicalCard)
}
