package no.nav.tps.forvalteren.provider.rs.security.mapping;

import no.nav.tps.forvalteren.service.user.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;


@RunWith(MockitoJUnitRunner.class)
public class RestAuthoritiesMapperTest {
    private static final SimpleGrantedAuthority CURRENT_ACCESS_ROLE_O = new SimpleGrantedAuthority("0000-GA-NORG_Skriv");
    private static final SimpleGrantedAuthority CURRENT_DISK_6_ROLE = new SimpleGrantedAuthority("0000-GA-GOSYS_KODE6");
    private static final SimpleGrantedAuthority CURRENT_DISK_7_ROLE = new SimpleGrantedAuthority("0000-GA-GOSYS_KODE7");
    private static final SimpleGrantedAuthority CURRENT_EGEN_ANSATT_ROLE = new SimpleGrantedAuthority("0000-GA-GOSYS_UTVIDET");

    @InjectMocks
    private RestAuthoritiesMapper mapper;

    @Test
    public void returnsEmptyListIfUserHasNoRoles() {
        Collection<UserRole> result = mapper.mapAuthorities(Collections.emptyList());

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void returnsEmptyListIfUserOnlyHasUnknownRoles() {
        SimpleGrantedAuthority ga = new SimpleGrantedAuthority("dummy");

        Collection<UserRole> result = mapper.mapAuthorities(singletonList(ga));

        assertThat(result.isEmpty(), is(true));
    }


    @Test
    public void returnsEgenAnsattRoleWhenUserHasEgenAnsatt() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_EGEN_ANSATT_ROLE));

        assertThat(result, contains(UserRole.ROLE_EGEN_ANSATT_READ));
    }

    @Test
    public void returnsReadOWhenUserHasReadO() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_ACCESS_ROLE_O));

        assertThat(result, contains(UserRole.ROLE_ACCESS));
    }

    @Test
    public void returnsDisk6WhenUserHasDisk6Role() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_DISK_6_ROLE));

        assertThat(result, contains(UserRole.ROLE_DISKRESJONESKODE_6_READ));
    }

    @Test
    public void returnsDisk7WhenUserHasDisk7Role() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_DISK_7_ROLE));

        assertThat(result, contains(UserRole.ROLE_DISKRESJONESKODE_7_READ));
    }

    @Test
    public void returnsReadRoleDiskresjon7And6RolesEgenansattRoleWhenUserHasAppropriateRoles() {
        List<SimpleGrantedAuthority> currentRoles = asList(
                CURRENT_DISK_6_ROLE,
                CURRENT_DISK_7_ROLE,
                CURRENT_EGEN_ANSATT_ROLE,
                CURRENT_ACCESS_ROLE_O);

        Collection<UserRole> result = mapper.mapAuthorities(currentRoles);

        assertThat(result, containsInAnyOrder(
                UserRole.ROLE_ACCESS,
                UserRole.ROLE_DISKRESJONESKODE_6_READ,
                UserRole.ROLE_EGEN_ANSATT_READ,
                UserRole.ROLE_DISKRESJONESKODE_7_READ
        ));
    }

    @Test
    public void mapAuthoritiesEklsludererRollerIrrelevanteForTPSKSomBrukerInnehar() {
        List<SimpleGrantedAuthority> currentRoles = asList(
                CURRENT_DISK_6_ROLE,
                CURRENT_DISK_7_ROLE,
                CURRENT_EGEN_ANSATT_ROLE,
                CURRENT_ACCESS_ROLE_O,
                new SimpleGrantedAuthority("dummy"),
                new SimpleGrantedAuthority("dummy_2"));

        Collection<UserRole> result = mapper.mapAuthorities(currentRoles);

        assertThat(result, containsInAnyOrder(
                UserRole.ROLE_ACCESS,
                UserRole.ROLE_DISKRESJONESKODE_6_READ,
                UserRole.ROLE_EGEN_ANSATT_READ,
                UserRole.ROLE_DISKRESJONESKODE_7_READ
        ));
    }

    @Test
    public void mapAuthoritiesReturnererKunDeRolleneSomBrukerInnehar() {
        List<SimpleGrantedAuthority> currentRoles = Collections.singletonList(CURRENT_ACCESS_ROLE_O);

        Collection<UserRole> result = mapper.mapAuthorities(currentRoles);

        assertThat(result, contains(UserRole.ROLE_ACCESS));

        assertThat(result, not(contains(UserRole.ROLE_EGEN_ANSATT_READ)));
        assertThat(result, not(contains(UserRole.ROLE_DISKRESJONESKODE_6_READ)));
        assertThat(result, not(contains(UserRole.ROLE_DISKRESJONESKODE_7_READ)));
    }
}