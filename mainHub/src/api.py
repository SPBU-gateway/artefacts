from fastapi import FastAPI, Query
from sqlalchemy.ext.declarative import declarative_base
from pydantic import BaseModel
from threading import Thread
import uvicorn
import json
from typing import Annotated
from producer import proceed_to_deliver


app = FastAPI()
Base = declarative_base()
    

class DeviceDataBase(BaseModel):
    name: str
    message: str
    
    def to_dict(self):
        return {"name": self.name, "message": self.message}


@app.post("/mainhub")
async def create_data(device: DeviceDataBase):
    print(f"Main hub got device: {device}")
    proceed_to_deliver(device.to_dict())
    return device
    

def run_rest(host: str, port: int):    
    uvicorn.run(app, host=host, port=port, log_level='info')


def start_fastapi_in_thread(requests_queue=None):
    global _requests_queue
    _requests_queue = requests_queue
    host_name = "0.0.0.0"  
    port = 8801
    thread = Thread(target=lambda: run_rest(host_name, port))
    thread.start()


if __name__ == "__main__":   
    print("Starting API")      
    start_fastapi_in_thread()
