package no.nav.tps.vedlikehold.consumer.rs.vera;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class VeraApplication {

    private String application;
    private String environment;
    private String version;
    private String deployer;

    @JsonProperty("deployed_timestamp")
    private String deployedTimestamp;

    @JsonProperty("replaced_timestamp")
    private String replacedTimestamp;

    private String environmentClass;
    private String id;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeployer() {
        return deployer;
    }

    public void setDeployer(String deployer) {
        this.deployer = deployer;
    }

    public String getDeployedTimestamp() {
        return deployedTimestamp;
    }

    public void setDeployedTimestamp(String deployedTimestamp) {
        this.deployedTimestamp = deployedTimestamp;
    }

    public String getReplacedTimestamp() {
        return replacedTimestamp;
    }

    public void setReplacedTimestamp(String replacedTimestamp) {
        this.replacedTimestamp = replacedTimestamp;
    }

    public String getEnvironmentClass() {
        return environmentClass;
    }

    public void setEnvironmentClass(String environmentClass) {
        this.environmentClass = environmentClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}