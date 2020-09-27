# Spring JDBC Realm example

Simple example to have a Spring JDBC realm which is compatible to a JEE container.

# How to run

- minimal requirement: Payara 4.1.2.181
- postgres db for the schema

## JDBC config
```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
    driverClassName: org.postgresql.Driver
```

## Schema

Run the following script for the DB

```sql
drop table if exists authorities;
drop table if exists users;

CREATE TABLE users (
  username VARCHAR(50) NOT NULL,
  password VARCHAR(200) NOT NULL,
  enabled boolean NOT NULL DEFAULT true,
  PRIMARY KEY (username)
);
  
CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL REFERENCES users(username) on delete cascade on update cascade,
  authority VARCHAR(50) NOT NULL,
  PRIMARY key (username, authority)
);

insert into users values('test', 'PBKDF2WithHmacSHA256:2048:Muo4cAqMsHCN5d27lQ1IXSEa5mMhwMn6BmubWG7DN9g=:MLJhWHR6Cf7YNYUf3XunIWb+rR2wonhXYBUgZJfVe8M=');
insert into authorities values('test', 'user');

insert into users values('admin', 'PBKDF2WithHmacSHA256:2048:9Nnm+vHMfaS02ZiG2qYP1rONuDIidG6c0/V452w5iIM=:2XNzYzZ7f+uALsNGLgxKnFbexkJvxu2g332Kz/h+4vg=');
insert into authorities values('admin', 'admin');
insert into authorities values('admin', 'user');
commit;
```

# App Links

## Get as user

- GET http://localhost:8080/user
- user: test
- password: test

## Get as admin

- GET http://localhost:8080/admin
- user: admin
- password: admin

## Encrypt password

- POST http://localhost:8080/hash

# Links
- https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/appendix-schema.html
