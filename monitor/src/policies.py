import base64
VERIFIER_SEAL = 'verifier_seal'


def check_operation(id, headers):
    authorized = False
    # print(f"[debug] checking policies for event {id}, headers: {headers}")
    print(f"[info] checking policies for event {id},"
          f" {headers['from']}->{headers['to']}")
    src = headers['from']
    dst = headers['to']

    # CE
    if src == 'main-hub' and dst == 'main-storage' and id == 'default':
        authorized = True
    elif src == 'main-storage' and dst == 'main-manager-output' and id == 'default':
        authorized = True
    elif src == 'hub' and dst == 'storage' and id == 'default':
        authorized = True
    elif src == 'storage' and dst == 'manager-output' and id == 'default':
        authorized = True
    # ASA 
    # client-auth
    elif src == 'client-auth' and dst == 'manager-input' and (id == 'default' or id == 'new-device'):
        authorized = True
    elif src == 'client-auth' and dst == 'client-storage' and (id == 'default' or id == 'new-device'):
        authorized = True
    # client-storage
    elif src == 'client-storage' and dst == 'client-auth' and (id == 'default' or id == 'new-device'):
        authorized = True
    # device-storage
    elif src == 'device-storage' and dst == 'verifier' and id == 'default':
        authorized = True
    # main-manager-output
    elif src == 'main-manager-output' and dst == 'main-storage' and id == 'default':
        authorized = True
    elif src == 'manager-output' and dst == 'storage' and id == 'default':
        authorized = True
    elif src == 'main-manager-output' and dst == 'verifier' and (id == 'default' or id == 'new-device'):
        authorized = True
    # manager-input
    elif src == 'manager-input' and dst == 'client-auth' and (id == 'default' or id == 'new-device'):
        authorized = True
    elif src == 'manager-input' and dst == 'main-manager-output' and (id == 'default' or id == 'new-device'):
        authorized = True
    elif src == 'manager-input' and dst == 'manager-output' and id == 'default':
        authorized = True
    # verifier
    elif src == 'verifier' and dst == 'device-storage' and (id == 'default' or id == 'new-device'):
        authorized = True
    elif src == 'verifier' and dst == 'main-manager-output' and id == 'default':
        authorized = True
    
    

    return authorized


def check_payload_seal(payload):
    try:
        p = base64.b64decode(payload).decode()
        if p.endswith(VERIFIER_SEAL):
            print('[info] payload seal is valid')
            return True
    except Exception as e:
        print(f'[error] seal check error: {e}')
        return False
    