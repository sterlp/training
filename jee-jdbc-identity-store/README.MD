# Description

This examples show a simple JEE JDBC Realm in combination with payara.

- **Spring Boot example:** https://github.com/sterlp/training/tree/master/spring-jdbc-security

**Bot can use the same JDBC store for user and password.**

# How to run

- minimal requirement: Payara 4.1.2.181
- postgres db for the schema

## Datasources
- jdbc/app
- jdbc/identity-store

## JDBC Realm Schema and users for PostgreSQL

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

# Application Links

## Test Get
- http://localhost:8080/jee-jdbc-identity-store/

- user: test
- password: test

## Hash a password
- POST http://localhost:8080/jee-jdbc-identity-store/hash

# Links
- https://docs.payara.fish/enterprise/docs/5.21.0/documentation/payara-server/public-api/realm-identitystores/realm-identity-store-definition.html
- https://javaee.github.io/tutorial/security-advanced003.html
- https://blog.payara.fish/securing-a-rest-service
- https://www.codeflow.site/de/article/java-ee-8-security