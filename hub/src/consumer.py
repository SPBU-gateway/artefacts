from confluent_kafka import Consumer, OFFSET_BEGINNING
import json
from producer import proceed_to_deliver
import api
import threading
from typing import Dict


def handle_event(id: str, headers: Dict, details: Dict):
    print(f"[debug] handling event {id}, {headers}, {details}")
    print(f"[info] handling event {id}, {headers['from']}->{headers['to']}.")
    # try:
    #     pass
    #     
    # except Exception as e:
    #     print(f"[error] failed to handle request: {e}")


def consumer_job(args, config):
    verifier_consumer = Consumer(config)


    def reset_offset(verifier_consumer, partitions):
        if args.reset:
            for p in partitions:
                p.offset = OFFSET_BEGINNING
            verifier_consumer.assign(partitions)

    topic = "hub-storage"
    verifier_consumer.subscribe([topic], on_assign=reset_offset)
    
    try:
        while True:
            msg = verifier_consumer.poll(1.0)
            if msg is None:
                continue
            elif msg.error():
                print(f"[error] {msg.error()}")
            else:
                try:
                    id = msg.key()
                    print("handling ", id)
                    details = msg.value().decode('utf-8')
                    handle_event(id, {"from": details['from'], "to": details['to']}, details=json.loads(details))
                except Exception as e:
                    print(f"[error] Malformed event received from topic {topic}: {msg.value()}. {e}")
    except KeyboardInterrupt:
        pass
    finally:
        verifier_consumer.close()


config = {
    'bootstrap.servers': 'kafka-1:9092', # сервер кафки
    'group.id': 'update_demo_manager',
    'auto.offset.reset': 'earliest'  # начальная точка чтения (earliest or latest)}
}

def start_consumer(args, config):
    threading.Thread(target=lambda: consumer_job(args, config)).start()


if __name__ == '__main__':
    start_consumer(None, None) 
    
    ##{"from": "asd", "to": "asldads"}  {"address": "asd"}
    