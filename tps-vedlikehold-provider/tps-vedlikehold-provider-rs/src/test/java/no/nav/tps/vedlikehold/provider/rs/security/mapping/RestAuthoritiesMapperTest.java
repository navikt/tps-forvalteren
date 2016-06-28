package no.nav.tps.vedlikehold.provider.rs.security.mapping;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class RestAuthoritiesMapperTest {
    private static final SimpleGrantedAuthority CURRENT_WRITE_ROLE_T = new SimpleGrantedAuthority("WRITE_T1");
    private static final SimpleGrantedAuthority CURRENT_READ_ROLE_T = new SimpleGrantedAuthority("READ_T2");
    private static final SimpleGrantedAuthority CURRENT_WRITE_ROLE_Q = new SimpleGrantedAuthority("WRITE_Q2");
    private static final SimpleGrantedAuthority CURRENT_READ_ROLE_Q = new SimpleGrantedAuthority("READ_Q1");
    private static final List<String> WRITE_ROLES_T = asList("WRITE_T1", "WRITE_T2");
    private static final List<String> READ_ROLES_T = asList("READ_T1", "READ_T2");
    private static final List<String> WRITE_ROLES_Q = asList("WRITE_Q1", "WRITE_Q2");
    private static final List<String> READ_ROLES_Q = asList("READ_Q1", "READ_Q2");

    @InjectMocks
    private RestAuthoritiesMapper mapper;

    @Before
    public void before() {
        setInternalState(mapper, "writeRolesT", WRITE_ROLES_T);
        setInternalState(mapper, "readRolesT", READ_ROLES_T);
        setInternalState(mapper, "writeRolesQ", WRITE_ROLES_Q);
        setInternalState(mapper, "readRolesQ", READ_ROLES_Q);
        mapper.postConstruct();
    }

    @Test
    public void returnsEmptyListIfUserHasNoRoles() {
        Collection<UserRole> result = mapper.mapAuthorities(Collections.<GrantedAuthority>emptyList());

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void returnsEmptyListIfUserOnlyHasUnknownRoles() {
        SimpleGrantedAuthority ga = new SimpleGrantedAuthority("dummy");

        Collection<UserRole> result = mapper.mapAuthorities(singletonList(ga));

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void returnsWriteTWhenUserHasWriteT() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_WRITE_ROLE_T));

        assertThat(result, contains(UserRole.ROLE_WRITE_T));
    }

    @Test
    public void returnsWriteQWhenUserHasWriteQ() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_WRITE_ROLE_Q));

        assertThat(result, contains(UserRole.ROLE_WRITE_Q));
    }

    @Test
    public void returnsReadTWhenUserHasWriteT() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_READ_ROLE_T));

        assertThat(result, contains(UserRole.ROLE_READ_T));
    }

    @Test
    public void returnsReadQWhenUserHasReadQ() {
        Collection<UserRole> result = mapper.mapAuthorities(singletonList(CURRENT_READ_ROLE_Q));

        assertThat(result, contains(UserRole.ROLE_READ_Q));
    }

    @Test
    public void returnsReadQReadTWriteQWriteTWhenUserHasReadQReadTWriteQWriteT() {
        List<SimpleGrantedAuthority> currentRoles = asList(CURRENT_WRITE_ROLE_T, CURRENT_WRITE_ROLE_Q,
                CURRENT_READ_ROLE_T, CURRENT_READ_ROLE_Q);

        Collection<UserRole> result = mapper.mapAuthorities(currentRoles);

        assertThat(result, containsInAnyOrder(UserRole.ROLE_READ_Q,
                UserRole.ROLE_READ_T, UserRole.ROLE_WRITE_Q, UserRole.ROLE_WRITE_T));
    }
}