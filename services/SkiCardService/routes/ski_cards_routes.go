package routes

import (
	"github.com/gin-gonic/gin"

	"SkiCardService/handlers"
)

func SetUpSkiCardsRoutes(router *gin.RouterGroup, sch handlers.SkiCardsHandler) {
	// Define the routes for the SkiCards
	skiCardRoutes := router.Group("/skicards")
	{
		// GET /api/v1/skicards
		skiCardRoutes.GET("/", sch.GetSkiCards_Handler)

		// GET /api/v1/skicards/:id
		skiCardRoutes.GET("/:id", sch.GetSkiCardById_Handler)

		// POST /api/v1/skicards
		skiCardRoutes.POST("/", sch.CreateSkiCard_Handler)

		// PUT /api/v1/skicards/:ski_card_code/:physical_card_code
		skiCardRoutes.PUT("/:ski_card_code/:physical_card_code", sch.ActivateSkiCard_Handler)

		// DELETE /api/v1/skicards/:physical_card_code
		skiCardRoutes.DELETE("/:physical_card_code", sch.DeactivateSkiCard_Handler)
	}
}
