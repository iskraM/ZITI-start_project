package config

import (
	"context"
	"time"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

func NewDB(uri, dbName string) (*mongo.Database, error) {
	// Set up the MongoDB client
	client, err := mongo.NewClient(options.Client().ApplyURI(uri))
	if err != nil {
		return nil, err
	}

	// Connect to the MongoDB instance
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	if err = client.Connect(ctx); err != nil {
		return nil, err
	}

	// Ping the MongoDB instance to verify the connection
	if err = client.Ping(ctx, nil); err != nil {
		return nil, err
	}

	// Return the database object
	return client.Database(dbName), nil
}
