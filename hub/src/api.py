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
    

# 127.0.0.1:8000/mainhub?name=name&message=message
@app.get("/hub")
async def create_data(name: Annotated[str, Query(description='name of the message')], message: Annotated[str, Query(description='message value')]):
    device = {"name": name, "message": message}
    print(f"got device: {device}")
    return device
    

def run_rest(host: str, port: int):    
    uvicorn.run(app, host=host, port=port, log_level='info')


def start_fastapi_in_thread(requests_queue=None):
    global _requests_queue
    _requests_queue = requests_queue
    host_name = "0.0.0.0"  
    port = 82
    thread = Thread(target=lambda: run_rest(host_name, port))
    thread.start()


if __name__ == "__main__":   
    print("Starting API")      
    start_fastapi_in_thread()
