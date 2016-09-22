package no.nav.tps.vedlikehold.consumer.rs.vera;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class VeraApplication {

    private String application;
    private String environment;
    private String version;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}