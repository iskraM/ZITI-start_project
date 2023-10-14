package tests

import (
	"SkiCardService/config"
	"SkiCardService/handlers"
	"SkiCardService/routes"
	"os"
	"testing"

	"net/http"
	"net/http/httptest"

	"github.com/joho/godotenv"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
)

func SetUpRoutes(t *testing.T, r *gin.Engine, which int) {
	godotenv.Load("../.env")

	db, err := config.NewDB(os.Getenv("MONGODB_URL"), os.Getenv("DB_NAME"))
	if err != nil {
		t.Fatal(err)
	}

	// ski = 1, physical = 2
	if which == 1 {
		skiCardHandler := handlers.SkiCardsHandler{
			DB:         db,
			Collection: db.Collection(os.Getenv("SKI_CARDS_COLLECTION_NAME")),
		}
		routes.SetUpSkiCardsRoutes(&r.RouterGroup, skiCardHandler)
	} else if which == 2 {
		physicalCardHandler := handlers.PhysicalCardsHandler{
			DB:         db,
			Collection: db.Collection(os.Getenv("PHYSICAL_CARDS_COLLECTION_NAME")),
		}
		routes.SetUpPhysicalCardsRoutes(&r.RouterGroup, physicalCardHandler)
	}
}

func TestGetAllSkiCards(t *testing.T) {
	r := gin.Default()
	SetUpRoutes(t, r, 1)

	req, err := http.NewRequest(http.MethodGet, "/skicards/", nil)
	if err != nil {
		t.Fatal(err)
	}

	rr := httptest.NewRecorder()

	r.ServeHTTP(rr, req)

	// preverim ali je odgovor 200 OK
	assert.Equal(t, http.StatusOK, rr.Code)

	// preverim ali je odgovor v obliki JSON
	assert.Equal(t, "application/json; charset=utf-8", rr.Header().Get("Content-Type"))
}

func TestGetSkiCard(t *testing.T) {
	r := gin.Default()
	SetUpRoutes(t, r, 1)

	id := "640d04888e16aa78eed6d90e"

	req, err := http.NewRequest(http.MethodGet, "/skicards/"+id, nil)
	if err != nil {
		t.Fatal(err)
	}

	rr := httptest.NewRecorder()

	r.ServeHTTP(rr, req)

	// preverim ali je odgovor 200 OK
	assert.Equal(t, http.StatusOK, rr.Code)

	// preverim ali je odgovor v obliki JSON
	assert.Equal(t, "application/json; charset=utf-8", rr.Header().Get("Content-Type"))

	assert.Contains(t, rr.Body.String(), id)
	assert.Contains(t, rr.Body.String(), "test-123")
}

func TestGetPhysicalCard(t *testing.T) {
	r := gin.Default()
	SetUpRoutes(t, r, 2)

	card_code := "test-123"

	req, err := http.NewRequest(http.MethodGet, "/physicalcards/"+card_code, nil)
	if err != nil {
		t.Fatal(err)
	}

	rr := httptest.NewRecorder()

	r.ServeHTTP(rr, req)

	// preverim ali je odgovor 200 OK
	assert.Equal(t, http.StatusOK, rr.Code)

	// preverim ali je odgovor v obliki JSON
	assert.Equal(t, "application/json; charset=utf-8", rr.Header().Get("Content-Type"))

	assert.Contains(t, rr.Body.String(), card_code)
}
