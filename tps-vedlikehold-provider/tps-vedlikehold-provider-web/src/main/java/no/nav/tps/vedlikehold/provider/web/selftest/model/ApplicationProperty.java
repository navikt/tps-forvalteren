package no.nav.tps.vedlikehold.provider.web.selftest.model;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class ApplicationProperty {

    private String name;
    private String value;

    public ApplicationProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}