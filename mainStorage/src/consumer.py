from confluent_kafka import Consumer, OFFSET_BEGINNING
import json
from producer import proceed_to_deliver
import api
import threading


def handle_event(id: str, msg):
    # print(f"[debug] handling event {id}, {details}")
    details = json.loads(msg.value())
    hd = json.loads(msg.headers())
    print(
        f"[info] handling event {id}, {hd['from']}->{hd['to']}.")
    try:
        details['devices'] = api.get_data()
        proceed_to_deliver(details=details)
    except Exception as e:
        print(f"[error] failed to handle request: {e}")


def consumer_job(args, config):
    verifier_consumer = Consumer(config)


    def reset_offset(verifier_consumer, partitions):
        if args.reset:
            for p in partitions:
                p.offset = OFFSET_BEGINNING
            verifier_consumer.assign(partitions)

    topic = "main-manager-output-main-storage"
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
                    handle_event(id, msg)
                except Exception as e:
                    print(
                        f"[error] Malformed event received from topic {topic}: {msg.value()}. {e}")
    except KeyboardInterrupt:
        pass
    finally:
        verifier_consumer.close()


config = {'bootstrap.servers': 'kafka-1:9092', # сервер кафки
    'group.id': 'update_demo_manager',
    'auto.offset.reset': 'earliest'  # начальная точка чтения (earliest or latest)}
}

def start_consumer(args, config):
    threading.Thread(target=lambda: consumer_job(args, config)).start()


if __name__ == '__main__':
    start_consumer(None, None, None) 