package no.nav.tps.forvalteren.consumer.rs.environments;

import java.util.Set;


public interface FetchEnvironmentsConsumer {

    Set<String> getEnvironments(String application);

    Set<String> getEnvironments(String application, boolean usage);

    boolean ping();
}