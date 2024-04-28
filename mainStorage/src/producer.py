import multiprocessing
import threading
from confluent_kafka import Producer
import json


config = {
    'bootstrap.servers': 'kafka-1:9092', # сервер кафки
    'group.id': 'update_demo_manager',
    'auto.offset.reset': 'earliest'  # начальная точка чтения (earliest or latest)}
}


_requests_queue: multiprocessing.Queue = None


def proceed_to_deliver(id, details):
    print(f"[debug] queueing for delivery event id: {id}, payload: {details}")
    _requests_queue.put(details)
    
    
def producer_job(_, config, requests_queue: multiprocessing.Queue):
    # Create Producer instance
    producer = Producer(config)
    
    
    def delivery_callback(err, msg):
        if err:
            print('[error] Message failed delivery: {}'.format(err))
        else:
            print(f"[debug] Testing, got image {msg.value().decode('utf-8')}")
            

    while True:
        event_details = requests_queue.get()                
        producer.produce('main-storage-main-manager-output', json.dumps(event_details), "default".encode('utf-8'), headers={"from": "main-storage", "to": "main-manager-output"}, callback=delivery_callback)
        # Block until the messages are sent.
        producer.poll(10000)
        producer.flush()
        
        
def start_producer(args, config, requests_queue):
    global _requests_queue
    _requests_queue = requests_queue
    threading.Thread(target=lambda: producer_job(args, config, requests_queue)).start()
    
    
if __name__ == '__main__':
    start_producer(None, config=config, requests_queue=None)    
    