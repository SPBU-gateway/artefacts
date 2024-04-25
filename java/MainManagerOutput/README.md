# Используемые топики
* to-verifier-auth
* to-verifier-new-device
* monitor

# Используемые сообщения (Примеры)
## monitor 
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
```
key: default
{
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8="
}
headers:
{
	"from": "main-manager-output",
	"to": "main-storage"
}
```

```
key: new-device
{
	"name": "vvRNyDH0gbjLyNRILIeZZQ==",
	"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA="
}
headers:
{
	"from": "main-manager-output",
	"to": "verifier"
}
```
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
	"from": "main-manager-output",
	"to": "verifier"
}
```
## manager-input-main-manager-output
```
key: default
{
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8="
}
```
```
key: new-device
{
	"name": "vvRNyDH0gbjLyNRILIeZZQ==",
	"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA="
}
```

## main-storage-main-manager-output

```
key: default
{
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8=",
	"devices": [
		{
			"name": "vvRNyDH0gbjLyNRILIeZZQ==",
			"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA=",
			"message": "UXv155brNtuLBKzkWCy0uA=="
		},
		{
			"name": "vvrNyDH0gbjLyNRILIeZZQ==",
			"password": "VLf5DbMKoYYb+pualiE9SA==",
			"message": "UXv155brNtuLBKzkWCy0ua="
		}
	]
}
```

## verifier-main-manager-output

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
```