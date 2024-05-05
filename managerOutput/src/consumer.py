import threading
from confluent_kafka import Consumer, OFFSET_BEGINNING
import json
from producer import proceed_to_deliver
import requests

def execute_update(id: str, details: dict):
    try:
        # url = details['adress']
        url = "http://127.0.0.1:8000"
        response = requests.post(f"{url}/", json=details)
        print(response.status_code)
        print(response.text)
            
    except Exception as e:
        print(f"[error] failed to execute update: {e}")

def handle_event(id: str, details: dict):
    print(f"[info] handling event {id}, {details['from']}->{details['to']}")
    try:
        if details['from'] == 'manager-input':
            details['to'] = 'storage'
            proceed_to_deliver(id, details)
        if details['from'] == 'storage':
            execute_update(id, details)
    except Exception as e:
        print(f"[error] failed to handle request: {e}")

def consumer_job(args, config):
    manager_output_consumer = Consumer(config)

    def reset_offset(manager_output_consumer, partitions):
        if args.reset:
            for p in partitions:
                p.offset = OFFSET_BEGINNING
            manager_output_consumer.assign(partitions)

    topic1 = "manager-input-manager-output"
    topic2 = "storage-manager-output"

    manager_output_consumer.subscribe([topic1, topic2], on_assign=reset_offset)

    try:
        while True:
            msg = manager_output_consumer.poll(1.0)
            if msg is None:
                continue
            elif msg.error():
                print(f"[error] {msg.error()}")
            else:
                try:
                    id = msg.key().decode('utf-8')
                    details = msg.value().decode('utf-8')
                    details = json.loads(details)
                    handle_event(id, details)
                except Exception as e:
                    print(f"[error] malformed event received from manager output: {msg.value()}. {e}")
    except KeyboardInterrupt:
        pass
    finally:
        manager_output_consumer.close()


config = {
    'bootstrap.servers': 'kafka-1:9092', # сервер кафки
    'group.id': 'update_demo_manager_output',
    'auto.offset.reset': 'earliest'  # начальная точка чтения (earliest or latest)}
}

def start_consumer(args, config):
    threading.Thread(target=lambda: consumer_job(args, config)).start()

if __name__ == '__main__':
    start_consumer(None, None)