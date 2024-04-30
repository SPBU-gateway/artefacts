# Используемые топики
* to-verifier-auth
* to-verifier-new-device
* monitor

# Используемые сообщения (Примеры)
## Для monitor 
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
headers:
{
	"from": "verifier",
	"to": "device-storage"
}
```

```
key: new-device
{
	"name": "vvRNyDH0gbjLyNRILIeZZQ==",
	"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA="
}
{
	"from": "verifier",
	"to": "device-storage"
}
```

```
key: default
{
	"address": "http://cloud:8000",
	"devices": [
		{
			"name": "device1",
			"message": "some message"
		}
	]
}
{
	"from": "verifier",
	"to": "main-manager-output"
}
```

## main-manager-output-verifier

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

## device-storage-main-manager-output

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
```