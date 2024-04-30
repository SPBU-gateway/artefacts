# Api
curl -X POST http://localhost:8081/new-device -H "Content-Type: application/json" -d '{"clientName": "Jonathan", "deviceName": "6IsVdC+zLkn6p5EU7Nl5uw==", "devicePassword": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA="}'
curl -X POST http://localhost:8081/get-devices -H "Content-Type: application/json" -d '{"clientName": "Jonathan", "address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8="}'

# monitor

```
key: new-device
{
	"name": "Jonathan",
	"device": {
		"name": "6IsVdC zLkn6p5EU7Nl5uw==",
		"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo 9OVN6ivCiajTeA="
	}
}
headers:
{
	"from": "manager-input",
	"to": "client-auth"
}
```

```
key: default
{
	"name": "Jonathan",
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8="
}
headers:
{
	"from": "manager-input",
	"to": "client-auth"
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
	"from": "manager-input",
	"to": "main-manager-output"
}
```

```
key: default
{
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8="
}
headers:
{
	"from": "manager-input",
	"to": "main-manager-output"
}
```
```
key:default
{
	"address": "http://cloud:8000"
}
headers:
{
	"from": "manager-input",
	"to": "manager-output"
}
```

# client-auth-manager-input

```
key: new-device
{
	"name": "vvRNyDH0gbjLyNRILIeZZQ==",
	"password": "Hu3DoIwxL7/fol4enqQK6zErQ8Lo+9OVN6ivCiajTeA="
}
```

```
key: default
{
	"address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8="
}
```

