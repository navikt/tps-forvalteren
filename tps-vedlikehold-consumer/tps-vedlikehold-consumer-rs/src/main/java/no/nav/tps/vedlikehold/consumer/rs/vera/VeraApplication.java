package no.nav.tps.vedlikehold.consumer.rs.vera;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VeraApplication implements Comparable<VeraApplication> {
    private static final List<Character> ENV_CLASSES = Lists.newArrayList('p', 'q', 't', 'u');

    private String application;
    private String environment;
    private String version;
    private String deployer;
    private String deployed_timestamp;
    private String replaced_timestamp;
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

    public String getDeployed_timestamp() {
        return deployed_timestamp;
    }

    public void setDeployed_timestamp(String deployed_timestamp) {
        this.deployed_timestamp = deployed_timestamp;
    }

    public String getReplaced_timestamp() {
        return replaced_timestamp;
    }

    public void setReplaced_timestamp(String replaced_timestamp) {
        this.replaced_timestamp = replaced_timestamp;
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

    @Override
    public int compareTo(VeraApplication veraApp) {
        return numberedEnv().compareTo(veraApp.numberedEnv());
    }

    private String numberedEnv() {
        return String.format("%d%s", ENV_CLASSES.indexOf(environment.charAt(0)) * 1000, environment.substring(1));

    }
}