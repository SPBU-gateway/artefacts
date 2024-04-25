from fastapi import FastAPI
from sqlalchemy.ext.declarative import declarative_base
from pydantic import BaseModel
from threading import Thread
import uvicorn
import json
from typing import List
from producer import proceed_to_deliver


app = FastAPI()
Base = declarative_base()
    

class DeviceDataBase(BaseModel):
    name: str
    message: str
# curl -d "[{"name": "a1", "message": "a1m"}, {"name": "a2", "message": "a2m"}]" -X POST 127.0.0.1:8000/mainhub


@app.post("/mainhub")
async def create_data(device: List[DeviceDataBase]):
    print(f"got list {device}")
    result = proceed_to_deliver([dev.model_dump() for dev in device])
    return result
    

def run_rest(host: str, port: int):    
    uvicorn.run(app, host=host, port=port, log_level='info')


def start_fastapi_in_thread(requests_queue=None):
    global _requests_queue
    _requests_queue = requests_queue
    host_name = "127.0.0.1"  
    port = 8000  
    thread = Thread(target=lambda: run_rest(host_name, port))
    thread.start()


if __name__ == "__main__":   
    print("Starting API")      
    start_fastapi_in_thread()
