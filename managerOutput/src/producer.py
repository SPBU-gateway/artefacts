import multiprocessing
import threading
from confluent_kafka import Producer
import json

_requests_queue: multiprocessing.Queue = None

def proceed_to_deliver(details):
    details['from'] = 'manager-output'
    _requests_queue.put(details)

def producer_job(_, config, requests_queue: multiprocessing.Queue):
    producer = Producer(config)

    def delivery_callback(err, msg):
        if err:
            print('[error] Message failed deliver: {}'.format(err))
        else:
            print(f"[debug] Testing, {msg.value().decode('utf-8')}")

    topic = 'monitor'
    while True:
        event_details = requests_queue.get()
        producer.produce(topic, json.dumps(event_details), "default", headers={"from": "manager-output", "to": "storage"}, callback=delivery_callback)
        producer.poll(10000)
        producer.flush()


config = {
    'bootstrap.servers': 'kafka-1:9092', # сервер кафки
    'group.id': 'update_demo_manager_output',
    'auto.offset.reset': 'earliest'  # начальная точка чтения (earliest or latest)}
}

def start_producer(args, config, requests_queue):
    global _requests_queue
    _requests_queue = requests_queue
    threading.Thread(target=lambda: producer_job(args, config, requests_queue)).start()

if __name__ == '__main__':
    start_producer(None, config=config, requests_queue=None)