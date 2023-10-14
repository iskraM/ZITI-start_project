import os
import json 
import requests

from typing import List
from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware

# Environment variables
USERS_SERVICE_URL = os.getenv("USERS_SERVICE_URL", "http://localhost:8080/api/v1")
SKI_CARDS_SERVICE_URL = os.getenv("SKI_CARDS_SERVICE_URL", "http://localhost:9090/api/v1")
ORDERS_SERVICE_URL= os.getenv("ORDERS_SERVICE_URL", "http://localhost:7070/api/v1")

# FastAPI
app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
)

@app.get("/", tags=["Root"])
def root():
    return {"message": "Welcome to WebGateway API"}

#region Orders
class CreateSkiCard(BaseModel):
    active_time: int
    type: str
    price: float

class Order(BaseModel):
    userID: str
    createSkiCards: List[CreateSkiCard]

class OrderCards(BaseModel):
    skiCards: List[str]


@app.get("/orders", tags=["Orders"])
def get_all_orders():
    response = requests.get(ORDERS_SERVICE_URL + "/orders")
    return response.json()

@app.get("/orders/{order_id}", tags=["Orders"])
def get_order_by_id(order_id: str):
    response = requests.get(ORDERS_SERVICE_URL + "/orders/" + order_id)
    return response.json()
    
@app.get("/orders/cards/{order_id}", tags=["Orders"])
def get_cards_by_order_id(order_id: str):
    order = requests.get(ORDERS_SERVICE_URL + "/orders/" + order_id).json()

    cards = []

    for card in order['skiCardIDs']:
        response = requests.get(SKI_CARDS_SERVICE_URL + "/skicards/" + card)

        if response.status_code == 200:
            cards.append(response.json())

    return cards

@app.get("/orders/owned/{user_id}", tags=["Orders"])
def get_orders_by_user_id(user_id: str):
    response = requests.get(ORDERS_SERVICE_URL + "/orders/owned/" + user_id)
    return response.json()

@app.post("/orders", tags=["Orders"])
def create_order(order: Order):
    response = requests.post(ORDERS_SERVICE_URL + "/orders", json=order.dict())

    if response.status_code == 201:
        return {"message": "Order created"}
    else:
        return {"message": "Order not created: " + response.reason}

@app.delete("/orders/{order_id}", tags=["Orders"], status_code=204)
def delete_order(order_id: str):
    response = requests.delete(ORDERS_SERVICE_URL + "/orders/" + order_id)

    if response.status_code == 204:
        return None
    else:
        return response.json()

#endregion

#region SkiCards
# Physical card
class PhysicalCard(BaseModel):
    id: str | None
    code: str | None
    user_id: str
    ski_card_id: str | None
    active_until: str | None

@app.get("/physicalcards/{card_code}", tags=["PhysicalCards"])
def get_physical_card(card_code: str):
    response = requests.get(SKI_CARDS_SERVICE_URL + "/physicalcards/" + card_code)
    return response.json()

@app.post("/physicalcards", tags=["PhysicalCards"])
def create_physical_card(physical_card: PhysicalCard):
    response = requests.post(SKI_CARDS_SERVICE_URL + "/physicalcards", json=physical_card.dict())
    return response.json()

# Digital card
class SkiCard(BaseModel):
    id: str | None
    code: str | None
    is_active: bool | None
    buy_date: str | None
    activate_date: str | None
    active_time: int
    type: str


@app.get("/skicards", tags=["SkiCards"])
def get_all_ski_cards():
    response = requests.get(SKI_CARDS_SERVICE_URL + "/skicards")
    
    if response.status_code == 200:
        return response.json()
    else:
        return JSONResponse(content=json.loads(response.text), status_code=response.status_code)

@app.get("/skicards/{card_id}", tags=["SkiCards"])
def get_ski_card_by_id(card_id: str):
    response = requests.get(SKI_CARDS_SERVICE_URL + "/skicards/" + card_id)

    if response.status_code == 200:
        return response.json()
    else:
        return JSONResponse(content=json.loads(response.text), status_code=response.status_code)

@app.post("/skicards", tags=["SkiCards"])
def create_ski_card(body: SkiCard):
    response = requests.post(SKI_CARDS_SERVICE_URL + "/skicards", json=body.dict())

    if response.status_code == 201:
        return response.json()
    else:
        return JSONResponse(content=json.loads(response.text), status_code=response.status_code)

@app.put("/skicards/{ski_card_code}/{physical_card_code}", tags=["SkiCards"])
def link_ski_card_to_physical_card(ski_card_code: str, physical_card_code: str):
    response = requests.put(SKI_CARDS_SERVICE_URL + "/skicards/" + ski_card_code + "/" + physical_card_code)

    if response.status_code == 200:
        return response.json()
    else:
        return JSONResponse(content=json.loads(response.text), status_code=response.status_code)

@app.delete("/skicards/{physical_card_code}", tags=["SkiCards"])
def delete_ski_card(physical_card_code: str):
    response = requests.delete(SKI_CARDS_SERVICE_URL + "/skicards/" + physical_card_code)

    if response.status_code == 200:
        return response.json()
    else:
        return JSONResponse(content=json.loads(response.text), status_code=response.status_code)
    

#endregion

#region Users
class User(BaseModel):
    ID: str | None
    name: str | None
    surname: str | None
    email: str | None
    age: int | None
    username: str | None
    password: str | None


@app.post("/users/login", tags=["Users"])
def login(user: User):
    response = requests.post(USERS_SERVICE_URL + "/users/login", json=user.dict())
    if response.status_code == 200:
        return response.json()
    else:
        return {"message": response.text}

@app.post("/users/register", tags=["Users"])
def register(user: User):
    response = requests.post(USERS_SERVICE_URL + "/users/register", json=user.dict())
    if response.status_code == 201:
        return {"message": "User created", "id": response.json()["id"]}
    else:
        return {"message": "User not created: " + response.text}

@app.put("/users", tags=["Users"])
def update_user(user: User):
    response = requests.put(USERS_SERVICE_URL + "/users", json=user.dict())

    if response.status_code == 200:
        return response.json()
    else:
        return {"message": "User not updated: " + response.text}

@app.get("/users", tags=["Users"])
def get_all_users():
    response = requests.get(USERS_SERVICE_URL + "/users")
    return response.json()

@app.get("/users/age_range/{min_age}/{max_age}", tags=["Users"])
def get_users_by_age_range(min_age: int, max_age: int):
    response = requests.get(USERS_SERVICE_URL + "/users/age_range/" + str(min_age) + "/" + str(max_age))
    return response.json()

@app.delete("/users/{user_id}", tags=["Users"], status_code=204)
def delete_user(user_id: str):
    response = requests.delete(USERS_SERVICE_URL + "/users/" + user_id)

    if response.status_code == 204:
        return None
    else:
        return response.json()

#endregion
