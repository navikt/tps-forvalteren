package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.response.EndringsmeldingResponse;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.function.ObjDoubleConsumer;

/**
 * Created by f148888 on 29.09.2016.
 */

@RestController
@RequestMapping(value = "api/v1")
public class EndringsmeldingController {

    private static final String TPS_ENDRINGSMELDING_PREFIX = "<endre";
    private static final String TPS_ENDINGSMLD_VALUE_TO_CHANGE = "valueToChange";

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private MessageProvider messageProvider;

    @LogExceptions
    @RequestMapping(value = "/endring/{" + TPS_ENDINGSMLD_VALUE_TO_CHANGE +"}", method = RequestMethod.GET)
    public EndringsmeldingResponse getEndring(@RequestParam(required = false)Map<String, Object> tpsRequestParameters, @PathVariable String valueToChange){
        tpsRequestParameters.put(TPS_ENDINGSMLD_VALUE_TO_CHANGE, valueToChange);

        //JsonNode jsonNode = mappingUtils
        return null;
    }
}