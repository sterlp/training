# URLs

- Config Repo: https://github.com/sterlp/spring-cloud-config-repo´

## Spring Config Server
- Config Server: GET https://localhost:8000/service1/dev
- Encrypt password: POST https://localhost:8000/encrypt

### Credentials
- username: configuser
- password: configpass

### Configured users
- user1:pass1
- user2:pass2
- user3:pass3

## Spring Client
- Spring Client: GET http://localhost:8002/
- Reload Config: POST http://localhost:8002/actuator/refresh

## Payara Client

- Payara Client: GET http://localhost:8001/
- Payara Client: POST http://localhost:8001/refresh

### Commands

- `mvn payara-micro:bundle`
- `mvn payara-micro:start`

# Config

- app: service1
- profile: dev | prod
- Disable SSL check on chrome `chrome://flags/#allow-insecure-localhost`




# Links

- https://docs.payara.fish/enterprise/docs/5.21.0/documentation/ecosystem/maven-plugin.html
- https://www.tomitribe.com/blog/custom-configsource-with-microprofile-configuration/
- https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_server.html
- SSH Private Key Settings: https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.0.2.RELEASE/single/spring-cloud-config.html#_git_ssh_configuration_using_properties
- https://github.com/spring-cloud/spring-cloud-config
- https://github.com/spring-cloud-samples/config-repo
- https://github.com/spring-cloud-samples/config-repo/blob/master/foo-dev.yml

- https://quarkus.io/guides/spring-cloud-config-client
- https://github.com/quarkusio/quarkus/tree/master/extensions/spring-cloud-config-client

- https://rmannibucau.metawerx.net/post/deltaspike-configuration-reloadable