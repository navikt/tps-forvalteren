package no.nav.tps.vedlikehold.consumer.rs.vera;

import java.util.Set;


public interface VeraConsumer {

    Set<String> getEnvironments(String application);

    Set<String> getEnvironments(String application, boolean onlyLatest, boolean filterUndeployed);

    boolean ping();
}