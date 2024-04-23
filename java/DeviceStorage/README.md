# Используемые топики
* to-verifier-auth
* to-verifier-new-device
* monitor

# Используемые сообщения (Примеры)
## Для monitor 
```
{
	"from": "Verifier",
	"to": "ManagerOutput",
	"address": "зашифрованый адресс",
	"devices": [
		{
			"name": "device1",
			"password": "VLf5DbMKoYYb+pualiE9CA==",
			"message": "Message1"
		}
	]
}
```

## Для to-device-storage

```
{
	"from": "ManagerOutput",
	"to": "Verifier",
	"address": "зашифрованый адресс",
	"devices": [
		{
			"name": "зашифрованное имя",
			"password": "VLf5DbMKoYYb+pualiE9CA==",
			"message": "зашифрованное сообщение"
		},
		{
			"name": "зашифрованное имя",
			"password": "VLf5DbMKoYYb+pualiE9CA==",
			"message": "зашифрованное сообщение"
		}
	]
}
```

## Для to-verifier-new-device

```
{
	"name": "зашифрованное имя",
	"password": "зашифрованное пароль"
}
```