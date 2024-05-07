from fastapi import FastAPI, HTTPException
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine
from pydantic import BaseModel
from threading import Thread
import uvicorn
from fastapi.responses import JSONResponse
import json



DB_URL = "postgresql://postgres:1234@db-storage:5432/Storage"
engine = create_engine(DB_URL)

session = sessionmaker(engine, expire_on_commit=False)
db = session()

app = FastAPI()
Base = declarative_base()




class DeviceDataBase(BaseModel):
    name: str
    message: str


class DeviceData(Base):
    __tablename__ = "Data"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    message = Column(String, index=True)
    
    def as_dict(self):
        return {'name': self.name, 'message': self.message}


Base.metadata.create_all(bind=engine)
    

@app.post("/data")
def create_data(device: DeviceDataBase):
    print(f'creating data: {device}')
    data = DeviceData(name=device['name'], message=device['message'])
    db.add(data)
    db.commit()
    db.refresh(data)
    
    return data
    


@app.get("/data")
def get_data():
    query = db.query(DeviceData).all()
    result = [i.as_dict() for i in query]
    print(f'result is {result}')
    return result


@app.delete("/data")
async def delete_data(id: int):
    query = db.query(DeviceData).filter(DeviceData.id == id).first()
    if query is not None:
        db.delete(query)
        db.commit()
        return {"Message": "Deleted"}

    return {"Message": "Not found"}
    

def run_rest(host: str, port: int):
    print(f"starting run_rest on {host}, {port}")
    uvicorn.run(app, host=host, port=port, log_level='info')
    print(f"Started run_rest")

def start_fastapi_in_thread(requests_queue=None):
    global _requests_queue
    _requests_queue = requests_queue
    host_name = "0.0.0.0"  # Укажите ваш хост
    port = 8852  # Укажите ваш порт
    thread = Thread(target=lambda: run_rest(host_name, port))
    thread.start()


if __name__ == "__main__":   
    print("Starting API")      
    start_fastapi_in_thread()
