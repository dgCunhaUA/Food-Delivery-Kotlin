START MYSQL

$ docker run --name cm-proj1-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql

entrar no container

$ mysql -u root

criar db "proj1"

mysql> CREATE DATABASE proj1;



$ node client.model.js
...