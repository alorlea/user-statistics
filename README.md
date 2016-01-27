User Login Tracker Service
===

Small Web service in dropwizard which allows registration and authentication of users.
Upon login, we generate a timestamp of such login and store it in a SQL based database,
in our case we use MySQL for the standard system and h2 based database for integration testing.

This REST service contains 3 modules which include:

* REST client to access the service
* Dropwizard service with configuration file to start the service.
* API module with common representations used by the client and the service.

The service REST api is as follows:

* To register a user: POST /register Content-Type: application/json, a user is in the form: {"username":"123","password":"hello123"}
* To login a user: GET /login,  we use basic HTTP authentication for login the user. Upon success, we show back 
necessary information for future requests.
* To list login attempts: GET /loginAttemts/{userId}, we get a list of login attempts based on the userId.
The resource is secured so you need to login first. In addition, users can only view their list, we block access to other users views.

How to run
===

We use maven to build in a multimodule setup.

```cli
mvn clean install 
```

This will generate the artifacts.

Launching the Service:
====

This command will generate all the jars and will generate a shaded jar with the dropwizard service.

```cli
java -jar target/user-statistics-service-1.0-SNAPSHOT.jar server conf/config.yaml
```

You can run the service making use of either h2 database or MySQL. The configuration files are under the conf folder.
The structure is as follows:

By default the ports of the service are 8080 and 8081 for the admin port. You can access the /healthcheck resource through the admin port.

You can change dropwizard port configuration based on the configuration reference found on [dropwizard reference](http://dropwizard.github.io/dropwizard/0.8.2/docs/manual/configuration.html)

```yaml

database:
  driverClass: com.mysql.jdbc.Driver
  user: root
  password: admin123
  url: jdbc:mysql://127.0.0.1:3306/userdb
  properties:
    charSet: UTF-8
    hibernate.dialect: org.hibernate.dialect.MySQLDialect
    hibernate.hbm2ddl.auto: update
  maxWaitForConnection: 1s
  minSize: 8
  maxSize: 32
  evictionInterval: 10s
  minIdleTime: 1 minute
# Logging settings.
logging:
  level: INFO
  appenders:
    - type: console
    - type: file
      threshold: INFO
      logFormat: "%-6level [%d{HH:mm:ss.SSS}] [%t] %logger{15} - %X{code} %msg %n"
      currentLogFilename: /var/log/user-statistics.log
      archive: true
      archivedLogFilenamePattern: /var/log/player-doc-%d{yyyy-MM-dd}-%i.log.gz
      archivedFileCount: 5
      timeZone: CET
      
```

NOTE: If you make use of MySQL, make sure that the database userdb, exists before launching the service, you can do so 
by running through MySQL CLI:

``` SQL
CREATE SCHEMA userdb;
```

Launching the small testing client:
====

To launch the small REST client through CLI you can do, once you builded the maven artifacts:

```cli
java -jar user-statistics-client-1.0-SNAPSHOT.jar
```

This will launch the client and will ask for the base URL, username and password to use during the session. 
You will be able to do the following commands:

* 1. Register user
* 2. login the user
* 3. Get list of login attempts for the current user.


Extras:
===

This is out of scope but I tried to add docker support in order to automate the deployment with docker compose but I 
encountered an issue when bringing the mysql container (basically the remote access is not enabled by default) so it 
crashes when deploying, still if you do a go around by login to the mysql container, you can fix the issue.

Docker (Beta):
====

The project contains already a dockerFile to generate a docker image for the service.
It is possible to launch the service with a connection to a MySQL server by making use of Docker by using docker 
compose if remote access is enabled.

For now there are problems launching the service, as the plain docker image for Mysql does not allow 
remote access natively

Extra commands:
====

This can be handy to fix the issues of the mysql container.

```cli
sudo docker run --name test-db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=admin123 -e MYSQL_DATABASE=userdb -d mysql:latest
```

Access the container:
```cli
sudo docker exec -it test-db bash
```

connect to the database inside the docker container:
```cli
mysql -u root -p -h 127.0.0.1
```
also remember to setup remote access:
```
GRANT ALL PRIVILEGES ON * . * TO 'root'@'%'
```

