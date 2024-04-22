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
	"devices": [
		{
			"name": "device1",
			"password": "VLf5DbMKoYYb+pualiE9CA==",
			"message": "Message1"
		}
	]
}
```

## Для to-verifier-auth

```
{
	"from": "ManagerOutput",
	"to": "Verifier",
	"devices": [
		{
			"name": "device1",
			"password": "VLf5DbMKoYYb+pualiE9CA==",
			"message": "Message1"
		},
		{
			"name": "device2",
			"password": "VLf5DbMKoYYb+pualiE9CA==",
			"message": "Message2"
		}
	]
}
```

## Для to-verifier-new-device

```
{
	"name": "newDevice",
	"password": "password"
}
```