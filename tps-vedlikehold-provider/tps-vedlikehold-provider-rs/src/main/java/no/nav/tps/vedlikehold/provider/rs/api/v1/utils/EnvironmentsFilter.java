package no.nav.tps.vedlikehold.provider.rs.api.v1.utils;

import org.springframework.util.AntPathMatcher;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class EnvironmentsFilter {

    private final AntPathMatcher matcher = new AntPathMatcher();

    private Set<String> includePatterns = new HashSet<>();
    private Set<String> exceptions      = new HashSet<>();

    public static EnvironmentsFilter create() {
        return new EnvironmentsFilter();
    }

    /** Include all environments matching the ant pattern */
    public EnvironmentsFilter include(String pattern) {
        includePatterns.add(pattern);

        return this;
    }

    /** Prevent individual environments from being included. This will override any include patterns */
    public EnvironmentsFilter exception(String environment) {
        exceptions.add(environment);

        return this;
    }

    /** Filter a set of environments based on the defined rules */
    public Set<String> filter(Set<String> environments) {
        return environments.stream()
                .filter(this::shouldIncludeEnvironment)
                .collect(toSet());
    }

    private boolean shouldIncludeEnvironment(String environment) {
        if (exceptions.contains(environment)) {
            return false;
        }

        for (String pattern : includePatterns) {
            if (matcher.match(pattern, environment)) {
                return true;
            }
        }

        return false;
    }
}
