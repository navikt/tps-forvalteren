package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ILLEGAL_MELDINGSTYPE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalMeldingstypeException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFeltDefinisjon;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;

@RunWith(MockitoJUnitRunner.class)
public class DetectMeldingstypeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    @Mock
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    @InjectMocks
    private DetectMeldingstype detectMeldingstype;

    private String skdEndringsmeldingT1WithAllFieldsSupplied;
    private String skdEndringsmeldingT2WithAllFieldsSupplied;

    private static final String T1 = "1";
    private static final String T2_TRANSTYPE_2 = "2";
    private static final String T2_TRANSTYPE_3 = "3";
    private static final String T2_TRANSTYPE_4 = "4";
    private static final String ILLEGAL_TRANSTYPE = "0";

    private static final String T1_MELDING_WITH_TRANSTYPE_1 = "1337992055120990630073403" + T1;
    private static final String T2_MELDING_WITH_TRANSTYPE_2 = "1337991614920991117151855" + T2_TRANSTYPE_2;
    private static final String T2_MELDING_WITH_TRANSTYPE_3 = "1337991614920991117151855" + T2_TRANSTYPE_3;
    private static final String T2_MELDING_WITH_TRANSTYPE_4 = "1337991614920991117151855" + T2_TRANSTYPE_4;
    private static final String MELDING_WITH_ILLEGAL_TRANSTYPE = "1337991614920991117151855" + ILLEGAL_TRANSTYPE;

    @Before
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        skdEndringsmeldingT1WithAllFieldsSupplied = FileUtils.fileRead(new File(classLoader.getResource("melding-t1-alle-felter-utfylt.txt").getFile()));
        skdEndringsmeldingT2WithAllFieldsSupplied = FileUtils.fileRead(new File(classLoader.getResource("melding-t2-alle-felter-utfylt.txt").getFile()));

        RsMeldingstype1Felter meldingT1 = new RsMeldingstype1Felter();
        RsMeldingstype2Felter meldingT2 = new RsMeldingstype2Felter();
        when(objectMapper.convertValue(any(HashMap.class), eq(RsMeldingstype1Felter.class))).thenReturn(meldingT1);
        when(objectMapper.convertValue(any(HashMap.class), eq(RsMeldingstype2Felter.class))).thenReturn(meldingT2);

        List<SkdFeltDefinisjon> felter = new ArrayList<>();
        when(skdFelterContainerTrans1.hentSkdFelter()).thenReturn(felter);
        when(skdFelterContainerTrans2.hentSkdFelter()).thenReturn(felter);
    }

    @Test
    public void returnsT1() {
        RsMeldingstype melding = detectMeldingstype.execute(T1_MELDING_WITH_TRANSTYPE_1);

        assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
    }

    @Test
    public void returnsT1WhenCalledWithSkdEndringsmeldingAllFieldsSupplied() {
        when(skdFelterContainerTrans1.hentSkdFelter()).thenCallRealMethod();

        RsMeldingstype melding = detectMeldingstype.execute(skdEndringsmeldingT1WithAllFieldsSupplied);

        assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
    }

    @Test
    public void checkThatAllFielsGetInitializedT1() {
        when(skdFelterContainerTrans1.hentSkdFelter()).thenCallRealMethod();

        RsMeldingstype melding = detectMeldingstype.execute(skdEndringsmeldingT1WithAllFieldsSupplied);

        assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
        for(Field f : melding.getClass().getDeclaredFields()) {
            assertThat(f, is(not(nullValue())));
        }
    }

    @Test
    public void checkThatAllFielsGetInitializedT2() {
        when(skdFelterContainerTrans2.hentSkdFelter()).thenCallRealMethod();

        RsMeldingstype melding = detectMeldingstype.execute(skdEndringsmeldingT2WithAllFieldsSupplied);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
        for(Field f : melding.getClass().getDeclaredFields()) {
            assertThat(f, is(not(nullValue())));
        }
    }

    @Test
    public void transtype2ReturnsT2() {
        RsMeldingstype melding = detectMeldingstype.execute(T2_MELDING_WITH_TRANSTYPE_2);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void returnsT2WhenCalledWithSkdEndringsmeldingAllFieldsSupplied() {
        when(skdFelterContainerTrans2.hentSkdFelter()).thenCallRealMethod();

        RsMeldingstype melding = detectMeldingstype.execute(skdEndringsmeldingT2WithAllFieldsSupplied);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void transtype3ReturnsT2() {
        RsMeldingstype melding = detectMeldingstype.execute(T2_MELDING_WITH_TRANSTYPE_3);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void transtype4ReturnsT2() {
        RsMeldingstype melding = detectMeldingstype.execute(T2_MELDING_WITH_TRANSTYPE_4);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void throwsIllegalMeldingstypeException() {
        expectedException.expect(IllegalMeldingstypeException.class);
        detectMeldingstype.execute(MELDING_WITH_ILLEGAL_TRANSTYPE);
        verify(messageProvider).get(SKD_ILLEGAL_MELDINGSTYPE, ILLEGAL_TRANSTYPE);
    }

}