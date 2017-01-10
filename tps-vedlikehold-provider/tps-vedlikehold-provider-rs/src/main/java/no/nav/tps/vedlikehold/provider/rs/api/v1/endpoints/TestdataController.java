package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

//import no.nav.tps.vedlikehold.domain.rs.testdata.RsTestDataRequest;
import no.nav.tps.vedlikehold.service.command.testdata.SkdMeldingFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

        import java.util.HashMap;
        import java.util.Map;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
@RestController
@RequestMapping(value = "api/v1")
public class TestdataController {

    @Autowired
    private SkdMeldingFormatter skdMeldingFormatter;

    @RequestMapping(value = "/testdata/skdcreate", method = RequestMethod.GET)
    public String createTestData(@RequestParam(required = false) Map<String, Object> skdMeldingParameters){
        //TODO Bare for testing dette mappet som lages under.
        //TODO Husk å sette på CSFR
        HashMap<String,String> bla = new HashMap<>();
        bla.put("T1-FORNAVN", "Peter");
        bla.put("T1-SLEKTSNAVN", "Fløgstad");
        bla.put("T1-PERSONNUMMER", "33152");
        String skdMelding = skdMeldingFormatter.convertToSkdMelding(bla);
        return skdMelding;
    }

}
