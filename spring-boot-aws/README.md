# How to build aws-deploy

- `mvn clean install`
- `cd aws-cicd`
- `cdk bootstrap`
- `cdk deploy`

# AWS URLs

- Fargate https://eu-central-1.console.aws.amazon.com/ecs/home?region=eu-central-1#/clusters
- AWS Console https://eu-central-1.console.aws.amazon.com/ec2/v2/home?region=eu-central-1


## Useful commands

* `mvn package`     compile and run tests
* `cdk ls`          list all stacks in the app
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk docs`        open CDK documentation
* `cdk destroy`     destroy your app's resources to avoid incurring any costs

# Docker build

## Build manual

- `docker build -t sterl/spring-aws-docker .`

## With maven

- `cd app/backend`
- `./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sterl/spring-aws-docker`

## Run Docker image localy

- `docker run -p 8080:8080 -t sterl/spring-aws-docker`

## Useful docker commands

- `docker ps`                       Show runing docker processes
- `docker stop <Container Id>`      Stop container
- `docker image ls`                 List docker images
- `docker image rm <Image Name>`    Delete docker image
- `docker system prune`             Remove all not used docker images
- `docker system prune -a`          Remove all docker images

# Problems and errors

## Problem: The AWS Access Key Id needs a subscription for the service

### Verify account and identity
* https://portal.aws.amazon.com/billing/signup?type=resubscribe#/identityverification
use basic if you want to use it for free

## This stack uses assets, so the toolkit stack must be deployed to the environment

For eu-central-1 region:
`aws ecs put-account-setting-default --name containerInsights --value enabled --region eu-central-1`


### Check account sns (simple notficiation service) and payment are active:
* https://console.aws.amazon.com/iam/home#/account_settings
* https://eu-central-1.console.aws.amazon.com/sns/home
* https://console.aws.amazon.com/billing/home#/paymentmethods

## Problem: This stack uses assets, so the toolkit stack must be deployed to the environment
Run
- `cdk bootstrap`

# Setup AWS CDK
## Required

* node.js
* `npm install -g aws-cdk`
* recommended aws cli https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-windows.html

## Init new project

Create a new folder and switch into it -- folder has to be empty.

* `cdk init app --language java`

#ä Setup account

* https://docs.aws.amazon.com/cdk/latest/guide/getting_started.html#getting_started_prerequisites
* `aws configure`   with aws cli
* AWS Access Key ID etc: https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html
  * direct link: https://console.aws.amazon.com/iam/home?#/security_credentials

### Maybe usesefull 
If js or TypeScript using NPM ...

* `npm install @aws-cdk/aws-rds @aws-cdk/aws-secretsmanager`
* `npm install @aws-cdk/aws-ecs @aws-cdk/aws-ec2 @aws-cdk/aws-ecs-patterns`

# Links
- https://github.com/enghwa/springboot-fargate-cdk/blob/master/lib/springboot-fargate-cdk-stack.ts
- https://medium.com/aws-factory/continuous-delivery-of-a-java-spring-boot-webapp-with-aws-cdk-codepipeline-and-docker-56e045812bd2
- https://docs.aws.amazon.com/de_de/cdk/latest/guide/ecs_example.html
- https://spring.io/guides/gs/spring-boot-docker/
- https://epsagon.com/development/deploying-java-spring-boot-on-aws-fargate/
- https://cloud.spring.io/spring-cloud-aws/reference/html/
- https://github.com/dsyer/spring-boot-allocations