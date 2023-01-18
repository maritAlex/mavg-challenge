# MAVG Challenge
> Code Challenge

Microservicio para transeferencia entre cuentas

## Getting started

Tener instalado 

```
JDK 17
Postgresql
Docker Engine
Docker Compose Engine
Gradle v7.6
```

Para compilar y ejecutar pruebas unitarias

```shell
gradle clean build
```

Para generar imagen docker

```shell
docker build -t mavg/mavg-challenge .
```

Para iniciar los servicios usando docker-compose
```shell
docker-compose up -d postgres
docker-compose up -d mavg-challenge
```

Una vez que el contenedor Docker este corriendo, se puede verificar utilizando el endpoint de health
```
http://localhost:8080/actuator/health
```

## Features

El proyecto consta de los siguientes namespaces
* `/login` -> Authenticacion. Actualmente solo soporta el usuario `operator` y contraseña `secret`
* `/accounts` -> Cuentas. Permite crear, actualizar el balance, y devuelve una cuenta en base a su numero de cuenta
* `/payments` -> Pagos. Permite el pago o transferencia de una cuenta origen a una destino.

Los endpoints estan documentados en [Postman Collection](./MAVG.postman_collection.json)

## Configuration

La base de datos es Postgresql 13 con los siguientes parametros por defecto
```shell
db_name: postgres
port: 5432
user: postgres
pass: postgres
```
Si se desea cambiar, dirigirse a [application.yml](./src/main/resources/application.yml) y modificar los siguientes valores

```yaml
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
```

## Licensing

"The code in this project is licensed under MIT license."
