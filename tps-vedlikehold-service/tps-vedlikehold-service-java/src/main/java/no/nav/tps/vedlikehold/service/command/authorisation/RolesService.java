package no.nav.tps.vedlikehold.service.command.authorisation;

import static java.util.Collections.emptySet;
import static no.nav.tps.vedlikehold.service.command.authorisation.RolesService.RoleType.READ;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Reads roles from property files and exposes them.
 * Reduces the necessary overhead when dealing with roles in the service layer
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */


@Component
public class RolesService {

    public enum RoleType {
        READ,
        WRITE;
    }

    /* Read roles */

    @Value("${tps.vedlikehold.security.t.readroles}")
    private Set<String> readRolesT;

    @Value("${tps.vedlikehold.security.q.readroles}")
    private Set<String> readRolesQ;

    @Value("${tps.vedlikehold.security.u.readroles}")
    private Set<String> readRolesU;

    @Value("${tps.vedlikehold.security.p.readroles}")
    private Set<String> readRolesP;

    @Value("${tps.vedlikehold.security.o.readroles}")
    private Set<String> readRolesO;

    /* Write roles */

    @Value("${tps.vedlikehold.security.t.writeroles}")
    private Set<String> writeRolesT;

    @Value("${tps.vedlikehold.security.q.writeroles}")
    private Set<String> writeRolesQ;

    @Value("${tps.vedlikehold.security.u.writeroles}")
    private Set<String> writeRolesU;

    @Value("${tps.vedlikehold.security.p.writeroles}")
    private Set<String> writeRolesP;

    @Value("${tps.vedlikehold.security.o.writeroles}")
    private Set<String> writeRolesO;


    public Set<String> getRolesForEnvironment(String environment, RoleType type) {
        if (type == READ) {
            return getReadRolesForEnvironment(environment);
        }

        return getWriteRolesForEnvironment(environment);
    }

    private Set<String> getReadRolesForEnvironment(String environment) {

        String prefix = getEnvironmentPrefix(environment);

        if ("u".equalsIgnoreCase(prefix)) {
            return readRolesU;
        } else if ("t".equalsIgnoreCase(prefix)) {
            return readRolesT;
        } else if ("q".equalsIgnoreCase(prefix)) {
            return readRolesQ;
        } else if ("p".equalsIgnoreCase(prefix)) {
            return readRolesP;
        } else if ("o".equalsIgnoreCase(prefix)) {
            return readRolesO;
        } else {
            return emptySet();
        }
    }

    private Set<String> getWriteRolesForEnvironment(String environment) {
        String prefix = getEnvironmentPrefix(environment);

        if ("u".equalsIgnoreCase(prefix)) {
            return writeRolesU;
        } else if ("t".equalsIgnoreCase(prefix)) {
            return writeRolesT;
        } else if ("q".equalsIgnoreCase(prefix)) {
            return writeRolesQ;
        } else if ("p".equalsIgnoreCase(prefix)) {
            return writeRolesP;
        } else if ("o".equalsIgnoreCase(prefix)) {
            return writeRolesO;
        } else {
            return emptySet();
        }
    }

    private String getEnvironmentPrefix(String environment) {
        if (isEmpty(environment)) {
            return "";
        }

        return environment.substring(0, 1);
    }
}
