# Запуск инструмента

## Запуск backend

Необходимо перейти в папку `src/main/java/` и запустить класс Main
с помощью IDE или команды:
```shell
java Main
```

Если не установлена `java`, выполнить:
```shell
sudo apt update
sudo apt install openjdk-21-jdk

export JAVA_HOME=/путь/к/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

## Запуск front

При первом запуске проекта в папке `src/main/front` необходимо выполнить команду:
```shell
npm install
npm start
```
При повторном запуске только:
```shell
npm start
```

На странице браузера `http://localhost:8000` будет доступен UI приложения.

Если не установлен `npm`:
```shell
sudo apt update
sudo apt install npm -y
```