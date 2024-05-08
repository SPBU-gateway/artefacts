# Используемые топики
* monitor


# Используемые сообщения (Примеры)
## Для monitor
```
key: default
{
    "address": "bTQRCsFOQKQO98nRUXNR87XxNuTcR7L32Yj/VG8r5W8=",
    "devices": '[{"name": "...", "message": "..."}, {"name": "...", "message": "..."}, ...]'
}
headers:
{
    "from": "main-hub",
    "to": "monitor" 
}
```
## Принимает Http запрос вида:
```
curl -X 'POST' 'http://127.0.0.1:8801/mainhub' -H 'accept: application/json' -H 'Content-Type: application/json' -d '{"name": "vvRNyDH0gbjLyNRILIeZZQ==", "message": "UXv155brNtuLBKzkWCy0uA=="}'
```
