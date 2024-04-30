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
curl -d '[{"name": "vvRNyDH0gbjLyNRILIeZZQ==", "message": "UXv155brNtuLBKzkWCy0uA=="}, {"name": "a2", "message": "a2m"}]' -X POST 127.0.0.1:8801/mainhub
```
