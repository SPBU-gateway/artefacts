# КБП
## Краткое описание назначения и применения продукта
Продукт -- шлюз, который отправляет данные от некоторых устройств в облако для пользователя.  
### Ценности продукта и негативные события в их отношении
| Ценность |Негативное событие | Величина ущерба | Комментарий |
|---------------------|---------------------|---------------------|---------------------|
| Облачное хранилище  |Поддельный девайс| Средний | В облако уйдут данные от нереального устройства |
| Девайс  |Поломка девайса| Низкий | Атака девайса извне |
| Система шлюза |Взлом шлюза и отправка данных в чужое облако| Высокий | Отправка данных производства в облако негодяев |
| Система шлюза | Подмена реальных данных устройства/устройств | Высокий |Пользователь может предпринять неверные решения на некорректных данных|
| Система шлюза | Полная поломка шлюза | Низкий |Событие заметное, будет сразу заметно, что был взлом|

## Роли пользователей
| Роль |Описание                        | 
|------|--------------------------------|
|Клиент|Непосредственный владелец данных|
|Клиент|Знает адрес нужного облака, может брать оттуда данные|
|Облако|Не имеет доступа к шлюзу|
|Облако|Получает данные от шлюза|
|Девайс|Не получает данные извне|
|Девайс|С некоторой периодичностью отправляет некоторую отчётность|

# Контекст

![alt text](diagrams/context/context.png)

# Базовый сценарий
![basic_scenary](diagrams/basicScenary/basic-scenary.png)

# Основные блоки шлюза
| Компонент |Описание                        | 
|-----------|--------------------------------|
|Auth|Выполняет аутентификацию устройств|
|Gateway|Получает запросы от клиента, получает данные от HUB,отправляет данные в облако, аутентифицирует клиента|
|HUB|Получает данные от девайса, валидирует данные, кэширует данные до запроса клиента|

# Архитектура (HLA) v.0.01
![alt text](diagrams/architectory-hla-0-01/architectory-0-01.png)

# Базовый сценарий + HLA
![alt text](diagrams/basicScenaryHla/basic-scenary-hla.png)
# ЦПБ-v1
## Цели безопасности
1. В облако попадают данные только от аутентичных девайсов
2. В облако передаются только валидные данные
3. Данные передаются только в аутентичное и авторизованное облако
4. Шлюз обрабатывает только аутентичные запросы от авторизованных клиентов
## Предположения безопасности 
1. Девайсы благонадёжны


# Негативные сценарии
## Негативный сценарий 1. В облако передаются данные от неаутентичных девайсов.
![alt text](diagrams/negative_scenarios/SG-1/Negative-scenario.Attack-on-Auth.SG-1.png)
Результат: недостижение цели безопасности 1 - в облаке присутствуют данные от неаутентичных девайсов
## Негативный сценарий 2. В облако передаются невалидные данные.
![alt text](diagrams/negative_scenarios/SG-2/Negative-scenario.Attack-on-Hub.SG-2.png)
Результат: недостижение цели безопасности 2 - в облаке присутствуют невалидные данные.
## Негативный сценарий 3. Данные передаются в неаутентичное и неавторизованное облако
![alt text](diagrams/negative_scenarios/SG-3/Negative-scenario.Attack-on-Gateway.SG-3.png)
Результат: недостижение цели безопасности 3 - данные передаются в неаутентичное и неавторизованное облако, клиент может получить неверные данные или не получить их вовсе.
## Негативный сценарий 4. Шлюз обрабатывает неаутентичные запросы от неавторизованных клиентов
![alt text](diagrams/negative_scenarios/SG-4/Negative-scenario.Attack-on-Gateway.SG-4.png)
Результат: недостижение цели безопасности 4 - будут выполнены неаутентичные запросы от неавторизованных клиентов, данные могут быть скомпрометированны.

# Политика архитектуры

| Компонент |Описание                        | Сложность |Размер|
|-----------|--------------------------------|--|--|
|Auth|Выполняет аутентификацию устройств| C | XL |
|Gateway|Получает запросы от клиента, получает данные от HUB,отправляет данные в облако, аутентифицирует клиента| C | XL |
|HUB|Получает данные от девайса, валидирует данные, кэширует данные до запроса клиента| S | S |

![alt text](diagrams/politic/old-colored-architectory.png)

| Компонент | Статус |Комментарий                     | 
|-----------|--------------------------------|--|
|Auth|Доверенный| Нарушает цб 1 |
|Gateway|Повышает доверие| Нарушает цб 1, 3, 4 |
|HUB|Повышает доверие| Нарушает цб 2|

## Новая архитектура v.0.02
| Компонент |Описание                        | Сложность |Размер|
|-----------|--------------------------------|--|--|
|DeviceAuth|Выполняет аутентификацию устройств| C | XL |
|HUB|Получает данные от девайсов и передаёт в storage| S | S |
|Storage|Хранит последние данные девайсов| C | XL |
|ManagerOutput|Валидирует данные, отправляет данные в облако| M | M |
|ClientAuth|Аутентификация клиентов| C | XL |
|ManagerInput|Получает запрос от клиентов, валидирует полученные данные| M | M |

![alt text](diagrams/politic/new-colored-architectory.png)

| Компонент | Статус |Комментарий                     | 
|-----------|--------------------------------|--|
|DeviceAuth|Доверенный| Нарушает цб 1 |
|ClientAuth|Доверенный| Нарушает цб 4 |
|ManagerInput|Повышает доверие| Нарушает цб 3, 4 |
|ManagerOutput|Повышает доверие| Нарушает цб 1, 2, 3|
|Hub|Недоверенный| |
|Storage|Недоверенный| |

### Базовый сценарий

![alt text](diagrams/politic/basic-scenary.png)

# Политика архитектуры
## ЦБП-v2

### Цели безопасности
1. Данные ключевых девайсов попадают в облако только после аутентификации
2. От ключевых девайсов в облако передаются только аутентичные данные
3. Ключевые данные передаются только в аутентичное и авторизованное облако
4. Шлюз обрабатывает только аутентичные запросы от авторизованных клиентов
### Предположения безопасности 
1. Девайсы предприятия благонадёжны
2. Ключевые девайсы передают зашифрованные данные

## Новая архитектура v.0.03
| Компонент |Описание                        | Сложность |Размер|
|-----------|--------------------------------|--|--|
|Verifier|Выполняет аутентификацию критических устройств, проверяет подпись переданных данных| M | S |
|MainHub|Получает данные от критических девайсов и передаёт в MainStorage| S | S |
|MainStorage|Хранит последние данные критических девайсов| С | XL |
|MainManagerOutput|Отправляет данные в облако после аутентификации через Verifier| S | S |
|ClientAuth|Аутентификация клиентов| M | S |
|ClientStorage|Хранит данные клиентов|C|XL|
|ManagerInput|Получает запрос от клиентов и передаёт их в ManagerOutput и MainManagerOutput| S | S |
|Hub|Получает данные от девайсов и передаёт в Storage| S | S |
|Storage|Хранит последние данные девайсов| L | XL |
|ManagerOutput|Отправляет данные в облако| S | S |
|DeviceStorage|Хранит данные ключевых девайсов|C|XL|

### Негативные сценарии
* Взлом MainManagerOutput -- передача данных бог знает куда ЦБ3
* Взлом ManagerInput, ClientAuth -- левые клиенты ЦБ4
* Взлом Verifier -- не будет валидации, левые ключевые девайсы передадут данные ЦБ2, ЦБ1

### Архитектура
![alt text](diagrams/politic-v-0-03/colored-architecture.png)

![alt text](image.png)

### Базовый сценарий

![alt text](diagrams/politic-v-0-03/main-basic-scenary.png)

![alt text](diagrams/politic-v-0-03/basic-scenary.png)

![alt text](diagrams/politic-v-0-03/register-device.png)