package main

import (
	"SkiCardService/config"
	"SkiCardService/handlers"
	"SkiCardService/routes"
	"log"
	"net/http"
	"os"

	"google.golang.org/grpc"
	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"go.mongodb.org/mongo-driver/mongo"

	pb "SkiCardService/messaging"
)

var mongoClient *mongo.Client

var RUN_ON_DOCKER = false;

func main() {
	if err := godotenv.Load(); err != nil {
		log.Printf("Error loading .env file %v", err)
	}

	base := gin.New()
	base.Use(gin.Logger())
	router := base.Group("/api/v1")

	db, err := config.NewDB(os.Getenv("MONGODB_URL"), os.Getenv("DB_NAME"))
	if err != nil {
		log.Fatal(err)
	}

	// Set up the controllers
	skiCardsHandlers := &handlers.SkiCardsHandler{
		DB:         db,
		Collection: db.Collection(os.Getenv("SKI_CARDS_COLLECTION_NAME")),
	}
	physicalCardsHandler := &handlers.PhysicalCardsHandler{
		DB:         db,
		Collection: db.Collection(os.Getenv("PHYSICAL_CARDS_COLLECTION_NAME")),
	}

	// index route
	router.GET("/", Root_Handler)

	// Set up the routes
	routes.SetUpSkiCardsRoutes(router, *skiCardsHandlers)
	routes.SetUpPhysicalCardsRoutes(router, *physicalCardsHandler)

	// Serve the application
	port := "9090"
	log.Printf("Server listening on port %s", port)

	if err := http.ListenAndServe(":"+port, base); err != nil {
		log.Fatal(err)
	}
}

func Root_Handler(ctx *gin.Context) {
	if (RUN_ON_DOCKER) {
		conn, err := grpc.Dial(os.Getenv("GRPC_URL"), grpc.WithInsecure())
		if err != nil {
			log.Fatalf("Failed to dial: %v", err)
		}
		defer conn.Close()

		client := pb.NewMessagingClient(conn)

		req := &pb.CreateRequest{
			Queue:   "ITA-SkiCardService",
			Message: "Root endpoint triggered",
		}

		// Send the request to the gRPC server
		_, err = client.SendMessageToQueue(ctx, req)
		if err != nil {
			log.Fatalf("Failed to send message: %v", err)
		}
	}

	ctx.JSON(http.StatusOK, gin.H{"message": "Welcome to the SkiCardService API"})
}
