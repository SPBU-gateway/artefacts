from fastapi import FastAPI, HTTPException
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from pydantic import BaseModel
from threading import Thread
import uvicorn


DB_URL = "postgresql://postgres:1234@db:5432/MainStorage"
engine = create_engine(DB_URL)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


app = FastAPI()
Base = declarative_base()


class DeviceDataBase(BaseModel):
    name: str
    message: str


class DeviceData(Base):
    __tablename__ = "MainData"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    message = Column(String, index=True)
    

Base.metadata.create_all(bind=engine)
    

@app.post("/maindata/")
async def create_data(device: DeviceDataBase):
    data = DeviceData(name=device.name, message=device.message)
    db = SessionLocal()
    db.add(data)
    db.commit()
    db.refresh(data)
    
    return data


@app.get("/maindata/")
async def get_data():
    db = SessionLocal()
    query = db.query(DeviceData)
    return query.all()


@app.delete("/maindata/")
async def delete_data(id: int):
    db = SessionLocal()
    query = db.query(DeviceData).filter(DeviceData.id == id).first()
    if query is not None:
        db.delete(query)
        db.commit()
        return {"Message": "Deleted"}

    return {"Message": "Not found"}
    

def run_rest(host: str, port: int):
    uvicorn.run(app, host=host, port=port, reload=False)
    

def start_fastapi_in_thread(requests_queue):
    global _requests_queue
    _requests_queue = requests_queue
    host_name = "127.0.0.1"  # Укажите ваш хост
    port = 8000  # Укажите ваш порт
    thread = Thread(target=lambda: run_rest(host_name, port))
    thread.start()


if __name__ == "__main__":         
    run_rest()
