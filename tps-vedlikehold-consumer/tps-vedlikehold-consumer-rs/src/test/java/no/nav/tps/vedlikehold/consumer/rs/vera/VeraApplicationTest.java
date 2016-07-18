package no.nav.tps.vedlikehold.consumer.rs.vera;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

/**
 * @author Kristian Kyvik, (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class VeraApplicationTest {

    @Test
    public void compareToShouldReturnEqual() {
        VeraApplication p_1 = new VeraApplication();
        VeraApplication p_2 = new VeraApplication();

        p_1.setEnvironment("p");
        p_2.setEnvironment("p");

        int result = p_1.compareTo(p_2);

        assertThat(result, is(0));
    }

    @Test
    public void compareToShouldReturnGreaterThan() {
        VeraApplication q2 = new VeraApplication();
        VeraApplication t3 = new VeraApplication();

        q2.setEnvironment("q2");
        t3.setEnvironment("t3");

        int result = q2.compareTo(t3);

        assertThat(result, lessThan(0));
    }

    @Test
    public void compareToShouldReturnLessThan() {
        VeraApplication q1 = new VeraApplication();
        VeraApplication l = new VeraApplication();

        q1.setEnvironment("q1");
        l.setEnvironment("l");

        int result = q1.compareTo(l);

        assertThat(result, greaterThan(0));
    }

    @Test
    public void sortingAnArrayOfVeraApplicationsShouldOrderTheInstances() {
        List<VeraApplication> list = Lists.newArrayList();

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

        assertThat( "first environment in list is l", list.get(0).equals(l) );
        assertThat( "second environment in list is p", list.get(1).equals(p) );
        assertThat( "third environment in list is q2", list.get(2).equals(q2) );
        assertThat( "fourth environment in list is q4", list.get(3).equals(q4) );
        assertThat( "fifth environment in list is t3", list.get(4).equals(t3) );
    }
}

