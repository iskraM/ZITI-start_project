from fastapi.testclient import TestClient 
from .main import app

cline = TestClient(app)

def test_root():
    response = cline.get("/")
    assert response.status_code == 200
    assert response.json() == {"message": "Welcome to MobileGateway API"}