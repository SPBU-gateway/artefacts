# Используемые топики
* to-verifier-auth
* to-verifier-new-device
* monitor

# Используемые сообщения (Примеры)
## monitor 
```
key: default
{
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8=",
	"devices": [
		{
			"name": "vvRNyDH0gbjLyNRILIeZZQ==",
			"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA=",
			"message": "UXv155brNtuLBKzkWCy0uA=="
		}
	]
}
headers:
{
	"from": "device-storage",
	"to": "verifier"
}
```

## verifier-device-storage

```
key: default
{
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8=",
	"devices": [
		{
			"name": "vvRNyDH0gbjLyNRILIeZZQ==",
			"message": "UXv155brNtuLBKzkWCy0uA=="
		},
		{
			"name": "vvrNyDH0gbjLyNRILIeZZQ==",
			"message": "UXv155brNtuLBKzkWCy0ua="
		}
	]
}
```
```
key: new-device
{
	"name": "vvRNyDH0gbjLyNRILIeZZQ==",
	"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA="
}
```