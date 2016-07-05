package no.nav.tps.vedlikehold.consumer.rs.vera;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Kristian Kyvik, (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class VeraApplicationTest {

    private final List<VeraApplication> list = Lists.newArrayList();

    @Test
    public void veraApplicationShouldCompareTo() {
        VeraApplication p = new VeraApplication();
        VeraApplication q2 = new VeraApplication();
        VeraApplication q4 = new VeraApplication();
        VeraApplication t3 = new VeraApplication();
        VeraApplication l = new VeraApplication();

        p.setEnvironment("p");
        q2.setEnvironment("q2");
        q4.setEnvironment("q4");
        t3.setEnvironment("t3");
        l.setEnvironment("l");

        list.add(t3);
        list.add(q4);
        list.add(q2);
        list.add(p);
        list.add(l);

        Collections.sort(list);

        assertThat( "first environment is list is l", list.get(0).equals(l) );
        assertThat( "first environment is list is p", list.get(1).equals(p) );
        assertThat( "first environment is list is q2", list.get(2).equals(q2) );
        assertThat( "first environment is list is q4", list.get(3).equals(q4) );
        assertThat( "first environment is list is t3", list.get(4).equals(t3) );
    }
}

