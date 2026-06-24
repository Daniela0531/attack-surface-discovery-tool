# Запуск инструмента

## Запуск backend

### Если не установлена java

Если не установлена `java`, выполнить:
```shell
sudo apt update
sudo apt install openjdk-21-jdk

export JAVA_HOME=/путь/к/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Запуск после установки java

Необходимо перейти в папку `src/main/java/` и запустить класс Main
с помощью IDE или команды:
```shell
java Main
```

Далее необходимо ввести абсолютный путь до локальной папки с проектом, который необходимо проанализировать.

## Запуск front

### Если не установлен npm

Если не установлен `npm`:
```shell
sudo apt update
sudo apt install npm -y
```

### Запуск после установки npm

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

