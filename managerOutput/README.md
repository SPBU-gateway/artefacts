# Используемые топики
* monitor

# Используемые сообщения (Примеры)
## monitor
```
key: default
{
	"address": "http://cloud:8000"
}
headers:
{
	"from": "manager-output",
	"to": "storage"
}
```
## manager-input-manager-output
```
key: default
{
    "address": "http://cloud:8000"
}
```

## storage-manager-output

```
key: default
{
    "address": "http://cloud:8000"
	"devices": [
		{
			"name": "...",
			"message": "..."
		},
		{
			"name": "...",
			"message": "..."
		},
		...
	]
}
```