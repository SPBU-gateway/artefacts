from Crypto.Cipher import AES
import base64

# Функция для добавления дополнительных символов в ключ, если его длина меньше требуемой
def pad_key(key):
    while len(key) % 16 != 0:
        key += ' '
    return key

# Функция для шифрования сообщения
def pad_message(message):
    block_size = AES.block_size
    padding_length = block_size - (len(message) % block_size)
    padding = bytes([padding_length]) * padding_length
    return message + padding

def unpad_message(padded_message):
    padding_length = padded_message[-1]
    return padded_message[:-padding_length]

def encrypt(message, secret_key):
    padded_message = pad_message(message.encode())
    padded_key = pad_key(secret_key)
    cipher = AES.new(padded_key.encode(), AES.MODE_ECB)
    encrypted_bytes = cipher.encrypt(padded_message)
    return base64.b64encode(encrypted_bytes).decode()

def decrypt(encrypted_message, secret_key):
    padded_key = pad_key(secret_key)
    cipher = AES.new(padded_key.encode(), AES.MODE_ECB)
    decrypted_bytes = cipher.decrypt(base64.b64decode(encrypted_message))
    return unpad_message(decrypted_bytes).decode()