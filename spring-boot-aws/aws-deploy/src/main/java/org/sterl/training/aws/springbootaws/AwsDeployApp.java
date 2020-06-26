package org.sterl.training.aws.springbootaws;

import software.amazon.awscdk.core.App;

public class AwsDeployApp {
    public static void main(final String[] args) {
        App app = new App();

        new AwsDeployStack(app, "AwsDeployStack");

        app.synth();
    }
}
