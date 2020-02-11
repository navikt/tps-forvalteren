naiseratorApplicationPipeline {
    applicationName = "tps-forvalteren"
    team = "registre"
    javaVersion = "1.8"
    namespace = "default"
    branchDeployments = [
        [branch: "release/test", namespace: "u2"],
        [branch: "feature/test-t1", namespace: "t1"]
    ]
    additionalDeployments = [
        [namespace: "u2", environment: "u2"],
        [namespace: "t1", environment: "t1"]
    ]
    isWhitelisted = true
}