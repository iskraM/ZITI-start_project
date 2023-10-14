package models

import (
	"time"

	"go.mongodb.org/mongo-driver/bson/primitive"
)

type SkiCard struct {
	ID           primitive.ObjectID `json:"id" bson:"_id"`
	Code         string             `json:"code" bson:"code"`
	IsActive     bool               `json:"is_active" bson:"is_active"`
	BuyDate      time.Time          `json:"buy_date" bson:"buy_date"`
	ActivateDate *time.Time         `json:"activate_date" bson:"activate_date"`
	ActiveTime   int                `json:"active_time" bson:"active_time"`
	Type         string             `json:"type" bson:"type"`
}
