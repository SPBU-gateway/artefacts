from fastapi import FastAPI, Depends, HTTPException
from typing import Annotated, List, Dict

from pydantic import BaseModel
# from database import engine, SessionLocal
# from sqlalchemy.orm import Session
from fastapi.middleware.cors import CORSMiddleware


app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["GET", "POST", "OPTIONS", "DELETE", "PATCH", "PUT"],
    allow_headers=["*"],
)

class DeviseBase(BaseModel):
    devices: List[Dict[str, str]]
    

@app.post('/')
def home(devices: DeviseBase):
    print(f"Got devices: {devices}")
    return f"Got devices: {devices}"




