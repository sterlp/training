package org.sterl.training.aws.springbootaws;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;

public class AwsDeployStack extends Stack {
    public AwsDeployStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsDeployStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        ApplicationLoadBalancedFargateService app = ApplicationLoadBalancedFargateService.Builder.create(this, "spring-boot-aws-service")
            .desiredCount(1)
            .assignPublicIp(true)
            .cpu(256).memoryLimitMiB(512)
            .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder()
                    .image(ContainerImage.fromAsset("../app"))
                    .containerPort(8080)
                    .containerName("sterl/spring-aws-docker")
                    .enableLogging(true)
                    .build())
            .build();

        // only path needed, other stuff just for fun
        app.getTargetGroup().configureHealthCheck(HealthCheck.builder()
                .healthyHttpCodes("200-499")
                .interval(Duration.seconds(15))
                .timeout(Duration.seconds(5))
                .healthyThresholdCount(2)
                .unhealthyThresholdCount(3)
                .path("/")
                .build());
    }
}
