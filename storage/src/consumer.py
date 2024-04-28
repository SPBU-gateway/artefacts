from confluent_kafka import Consumer, OFFSET_BEGINNING
import json
from producer import proceed_to_deliver
import api
import threading
from typing import Dict


topic_check = "manager-output-storage"
topic_add = "hub-storage"


def handle_event(id: str, details: Dict, topic: str):
    # print(f"[debug] handling event {id}, {details}")
    # details = json.loads(msg.value().decode('utf-8'))
    # print("handled details")
    # hd = json.loads(msg.headers().decode('utf-8'))
    print(f"[debug] handling event {id}, {details}")
    print(f"[info] handling event {id}.")
    try:
        if topic == topic_check:
            details['devices'] = api.get_data()
            proceed_to_deliver(id=id, details=details)
        elif topic == topic_add:
            print(f"details are {details['device']}")
            api.create_data(details['device'])
    except Exception as e:
        print(f"[error] failed to handle request: {e}")


def consumer_job(args, config):
    verifier_consumer = Consumer(config)


    def reset_offset(verifier_consumer, partitions):
        if args.reset:
            for p in partitions:
                p.offset = OFFSET_BEGINNING
            verifier_consumer.assign(partitions)

    verifier_consumer.subscribe([topic_add, topic_check], on_assign=reset_offset)
    
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
                    print(f"topic is {msg.topic()}")
                    details = msg.value().decode('utf-8')
                    handle_event(id, details=json.loads(details), topic=msg.topic())
                except Exception as e:
                    print(f"[error] Malformed event received from topic {msg.topic()}: {msg.value()}. {e}")
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
    