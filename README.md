# job4j_chat
[![Build Status](https://app.travis-ci.com/hasover/job4j_chat.svg?branch=master)](https://app.travis-ci.com/hasover/job4j_chat)

* [Описание](#описание)
* [Технологии](#технологии)
* [Функционал](#функционал)
* [Контакты](#контакты)

## Описание
Приложение реализовывает чат c комнатами, работает через REST API.

## Технологии
* Spring Boot (Web, Data, Security)
* JWT
* PostgreSQL
* Maven
* Travis CI

## Функционал

### 1. Регистрация сайта.
Чтобы зарегистрировася, нужно отправить запрос POST на `localhost:8080/user/sign-up` c телом JSON объекта с полями name, username,password.
![alt text](https://github.com/hasover/job4j_chat/blob/master/images/reg.PNG)

### 2. Авторизация.
Пользователь отправляет POST запрос на `localhost:8080/login` с login и password и получает ключ.
Этот ключ отправляется в запросе в блоке HEAD.
![alt text](https://github.com/hasover/job4j_chat/blob/master/images/auth.png)

### 3. Операции с комнатами.
Все основные операции REST API с комнатами производятся по адресу `localhost:8080/room`.
- Список всех комнат
  - Get запрос по адресу `localhost:8080/room/`
- Определенная комната
  - Get запрос по адресу `localhost:8080/room/{id}`
- Создание новой комнаты
  - Post запрос по адресу `localhost:8080/room/`
- Полное редактирование комнаты
  - Put запрос по адресу `localhost:8080/room/`
- Чаcтичное редактирование комнаты
  - Patch запрос по адресу `localhost:8080/room/`
- Удаление комнаты
  - Delete запрос по адресу `localhost:8080/room/{id}`

Пример cоздания комнаты: 
![alt text](https://github.com/hasover/job4j_chat/blob/master/images/room.PNG)

### 4. Операции с сообщениями.
Все основные операции REST API с сообщениями производятся по адресу `localhost:8080/message`.
- Список всех сообщений
  - Get запрос по адресу `localhost:8080/message/`
- Определенное сообщение
  - Get запрос по адресу `localhost:8080/message/{id}`
- Создание нового сообщения
  - Post запрос по адресу `localhost:8080/message/`
- Полное редактирование сообщения
  - Put запрос по адресу `localhost:8080/message/`
- Чаcтичное редактирование сообщения
  - Patch запрос по адресу `localhost:8080/message/`
- Удаление сообщения
  - Delete запрос по адресу `localhost:8080/message/{id}`

Пример cоздания сообщения: 
![alt text](https://github.com/hasover/job4j_chat/blob/master/images/message.PNG)

## Сборка приложения
- Для сборки приложения на вашем компьютере должны быть установлены:
    - JDK 14+
    - Maven
    - PostgreSQL
- Укажите настройки для подключения к БД в файле `src/main/resources/application.properties`
- Выполните команду `mvn package`
- Далее `java -jar target/chat-0.0.1-SNAPSHOT.jar`

Адрес сервера по умолчанию: http://localhost:8080/

## Контакты
telegram: @hasover






