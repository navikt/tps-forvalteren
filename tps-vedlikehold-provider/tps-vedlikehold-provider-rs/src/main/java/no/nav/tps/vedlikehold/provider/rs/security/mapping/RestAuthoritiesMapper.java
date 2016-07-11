package no.nav.tps.vedlikehold.provider.rs.security.mapping;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.stream.Collectors.toSet;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.*;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class RestAuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Value("${tps.vedlikehold.security.t.writeroles}")
    private List<String> writeRolesT;

    @Value("${tps.vedlikehold.security.t.readroles}")
    private List<String> readRolesT;

    @Value("${tps.vedlikehold.security.q.writeroles}")
    private List<String> writeRolesQ;

    @Value("${tps.vedlikehold.security.q.readroles}")
    private List<String> readRolesQ;

    private Map<String, UserRole> roles;

    @PostConstruct
    public void postConstruct() {
        Map<String, UserRole> roleMap = new HashMap<String, UserRole>();

        roleMap.putAll(mapRolesFromADToCommonUserRole(writeRolesT, ROLE_WRITE_T));
        roleMap.putAll(mapRolesFromADToCommonUserRole(readRolesT, ROLE_READ_T));
        roleMap.putAll(mapRolesFromADToCommonUserRole(writeRolesQ, ROLE_WRITE_Q));
        roleMap.putAll(mapRolesFromADToCommonUserRole(readRolesQ, ROLE_READ_Q));

        roles = Collections.unmodifiableMap(roleMap);
    }

    private Map<String, UserRole> mapRolesFromADToCommonUserRole(List<String> rolesList, UserRole commonUserRole) {
        Map<String, UserRole> roleMap = new HashMap<String, UserRole>();

        for (String role : rolesList) {
            roleMap.put(role, commonUserRole);
        }

        return roleMap;
    }

    @Override
    public Collection<UserRole> mapAuthorities(Collection<? extends GrantedAuthority> collection) {
        return collection.stream()
                .filter(Objects::nonNull)
                .map(GrantedAuthority::getAuthority)
                .map(roles::get)
                .filter(Objects::nonNull)
                .collect(toSet());
    }
}