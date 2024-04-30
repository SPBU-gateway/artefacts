from argparse import ArgumentParser, FileType
from configparser import ConfigParser
from api import start_fastapi_in_thread
from consumer import start_consumer
from producer import start_producer
from multiprocessing import Queue

if __name__ == "__main__":
    
    parser = ArgumentParser()
    parser.add_argument('--reset', action='store_true')
    args = parser.parse_args()
    

    config = {
        'bootstrap.servers': 'kafka-1:9092', # сервер кафки
        'group.id': 'update_demo_manager',
        'auto.offset.reset': 'earliest'  # начальная точка чтения (earliest or latest)}
    }

    requests_queue = Queue()
    start_fastapi_in_thread(requests_queue)
    start_consumer(args, config)
    start_producer(args, config, requests_queue)
    