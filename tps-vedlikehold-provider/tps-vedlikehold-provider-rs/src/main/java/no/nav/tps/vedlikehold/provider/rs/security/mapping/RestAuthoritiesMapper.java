package no.nav.tps.vedlikehold.provider.rs.security.mapping;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_O;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_P;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_Q;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_T;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_U;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_O;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_P;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_Q;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_T;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_U;


public class RestAuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Value("${tps.vedlikehold.security.t.writeroles}")
    private List<String> writeRolesT;

    @Value("${tps.vedlikehold.security.t.readroles}")
    private List<String> readRolesT;

    @Value("${tps.vedlikehold.security.q.writeroles}")
    private List<String> writeRolesQ;

    @Value("${tps.vedlikehold.security.q.readroles}")
    private List<String> readRolesQ;

    @Value("${tps.vedlikehold.security.u.writeroles}")
    private List<String> writeRolesU;

    @Value("${tps.vedlikehold.security.u.readroles}")
    private List<String> readRolesU;

    @Value("${tps.vedlikehold.security.p.writeroles}")
    private List<String> writeRolesP;

    @Value("${tps.vedlikehold.security.p.readroles}")
    private List<String> readRolesP;

    @Value("${tps.vedlikehold.security.o.writeroles}")
    private List<String> writeRolesO;

    @Value("${tps.vedlikehold.security.o.readroles}")
    private List<String> readRolesO;

    private Map<String, UserRole> roles;

    @PostConstruct
    public void postConstruct() {
        Map<String, UserRole> roleMap = new HashMap<>();

        roleMap.putAll(mapRolesFromADToCommonUserRole(writeRolesT, ROLE_WRITE_T));
        roleMap.putAll(mapRolesFromADToCommonUserRole(readRolesT, ROLE_READ_T));
        roleMap.putAll(mapRolesFromADToCommonUserRole(writeRolesQ, ROLE_WRITE_Q));
        roleMap.putAll(mapRolesFromADToCommonUserRole(readRolesQ, ROLE_READ_Q));
        roleMap.putAll(mapRolesFromADToCommonUserRole(writeRolesU, ROLE_WRITE_U));
        roleMap.putAll(mapRolesFromADToCommonUserRole(readRolesU, ROLE_READ_U));
        roleMap.putAll(mapRolesFromADToCommonUserRole(writeRolesP, ROLE_WRITE_P));
        roleMap.putAll(mapRolesFromADToCommonUserRole(readRolesP, ROLE_READ_P));
        roleMap.putAll(mapRolesFromADToCommonUserRole(writeRolesO, ROLE_WRITE_O));
        roleMap.putAll(mapRolesFromADToCommonUserRole(readRolesO, ROLE_READ_O));

        roles = Collections.unmodifiableMap(roleMap);
    }

    private Map<String, UserRole> mapRolesFromADToCommonUserRole(List<String> rolesList, UserRole commonUserRole) {
        Map<String, UserRole> roleMap = new HashMap<>();

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