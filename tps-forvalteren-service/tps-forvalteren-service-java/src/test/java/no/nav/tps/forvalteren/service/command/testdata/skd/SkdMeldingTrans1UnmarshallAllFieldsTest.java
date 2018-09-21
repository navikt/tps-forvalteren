package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SkdMeldingTrans1UnmarshallAllFieldsTest {
    
    private String skdEndringsmeldingT1WithAllFieldsSupplied_NoHeader;
    private String skdEndringsmeldingT1WithAllFieldsSupplied_WithHeader;
    
    @Before
    public void setupTestdata() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        skdEndringsmeldingT1WithAllFieldsSupplied_NoHeader = FileUtils.fileRead(new File(classLoader.getResource("melding-t1-alle-felter-utfylt.txt")
                .getFile()));
        skdEndringsmeldingT1WithAllFieldsSupplied_WithHeader = FileUtils.fileRead(new File(classLoader.getResource("melding-t1-alle-felter-utfylt-inkludert-header.txt")
                .getFile()));
        
    }
    
    @Test
    public void shouldUnmarshalWithNoNulls() throws InvocationTargetException, IllegalAccessException {
        SkdMeldingTrans1 skdMeldingTrans1 = SkdMeldingTrans1.unmarshal(skdEndringsmeldingT1WithAllFieldsSupplied_NoHeader);
        Assert.assertNull(skdMeldingTrans1.getHeader());
        skdMeldingTrans1.setHeader("header not null");
        assertNoNullFields(skdMeldingTrans1);
    }
    
    @Test
    public void shouldUnmarshalWithNoNulls_WithHeader() throws InvocationTargetException, IllegalAccessException {
        assertNoNullFields(SkdMeldingTrans1.unmarshal(skdEndringsmeldingT1WithAllFieldsSupplied_WithHeader));
    }
    
    private void assertNoNullFields(SkdMeldingTrans1 melding) throws InvocationTargetException, IllegalAccessException {
        for (Method f : melding.getClass().getMethods()) {
            if (f.getName().length() > 2 && "get".equals(f.getName().substring(0, 3)) && f.getParameterCount() == 0) {
                Assert.assertNotNull(f.getName(), f.invoke(melding));
            }
        }
    }
    
}