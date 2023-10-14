package models

import (
	"time"

	"go.mongodb.org/mongo-driver/bson/primitive"
)

type PhysicalCard struct {
	ID          primitive.ObjectID  `json:"id" bson:"_id"`
	Code        string              `json:"code" bson:"code"`
	UserID      primitive.ObjectID  `json:"user_id" bson:"user_id"`
	SkiCardID   *primitive.ObjectID `json:"ski_card_id" bson:"ski_card_id"`
	ActiveUntil *time.Time          `json:"active_until" bson:"active_until"`
}
