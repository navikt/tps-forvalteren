package no.nav.tps.vedlikehold.provider.rs.api.v1.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;



@RunWith(MockitoJUnitRunner.class)
public class EnvironmentsFilterTest {

    @Test
    public void onlyIncludedEnvironmentsAreReturned() {
        Set<String> environments = newSet("u1", "t4", "u4", "q2", "p");

        Set<String> result = EnvironmentsFilter.create()
                .include("u*")
                .filter(environments);

        assertThat(result, containsInAnyOrder("u1", "u4"));
    }

    @Test
    public void exceptionsAreNotReturned() {
        Set<String> environments = newSet("u1", "t4", "u4", "q2", "p");

        Set<String> result = EnvironmentsFilter.create()
                .include("u1")
                .exception("u4")
                .filter(environments);

        assertThat(result, containsInAnyOrder("u1"));
    }

    private Set<String> newSet(String... environments) {
        return new HashSet<>(
                Arrays.asList(environments)
        );
    }
}
