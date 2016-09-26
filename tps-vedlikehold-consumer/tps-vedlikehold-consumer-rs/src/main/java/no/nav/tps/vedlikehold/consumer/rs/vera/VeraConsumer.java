package no.nav.tps.vedlikehold.consumer.rs.vera;

import java.util.Set;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface VeraConsumer {

    Set<String> getEnvironments(String application);

    Set<String> getEnvironments(String application, boolean onlyLatest, boolean filterUndeployed);

    boolean ping();
}