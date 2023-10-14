package routes

import (
	"SkiCardService/handlers"

	"github.com/gin-gonic/gin"
)

func SetUpPhysicalCardsRoutes(router *gin.RouterGroup, pch handlers.PhysicalCardsHandler) {
	physicalCardRoutes := router.Group("/physicalcards")
	{
		// GET /api/v1/physicalcard/:physical_card_code
		physicalCardRoutes.GET("/:physical_card_code", pch.GetPhysicalCardByCode_Handler)

		// POST /api/v1/physicalcard
		physicalCardRoutes.POST("/", pch.CreatePhysicalCard_Handler)
	}
}
