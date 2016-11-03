package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import ch.qos.logback.core.util.SystemInfo;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsSystemInfo;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.response.EndringsmeldingResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.TpsResponseMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by f148888 on 29.09.2016.
 */

@RestController
@RequestMapping(value = "api/v1")
public class EndringsmeldingController {

    private static final String TPS_ENDRINGSMLD_PARAM = "endringsmeldingNavn";

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private TpsRequestService tpsRequestService;

    @Autowired
    private RsRequestMappingUtils mappingUtils;

    @Autowired
    private TpsResponseMappingUtils tpsResponseMappingUtils;

    @Autowired
    private UserContextHolder userContextHolder;

    @LogExceptions
    @RequestMapping(value = "/endring/{" + TPS_ENDRINGSMLD_PARAM + "}", method = RequestMethod.GET)
    public EndringsmeldingResponse getEndring(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String endringsmeldingNavn) {
        tpsRequestParameters.put(TPS_ENDRINGSMLD_PARAM, endringsmeldingNavn);
        return getEndringsmelding(tpsRequestParameters);
    }

    @LogExceptions
    @RequestMapping(value = "/endring", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public EndringsmeldingResponse getEndringsmelding(@RequestBody Map<String, Object> param) {
        validateRequest(param);
        return sendRequest(param);
    }

    private EndringsmeldingResponse sendRequest(Map<String, Object> param){
        String endringsmeldingNavn = param.get(TPS_ENDRINGSMLD_PARAM).toString();
        TpsRequestEndringsmelding request = mappingUtils.convertToTpsRequestEndringsmelding(endringsmeldingNavn, param);
        return sendTpsRequest(request, param.get("kilde").toString());
    }

    private EndringsmeldingResponse sendTpsRequest(TpsRequestEndringsmelding request, String kilde){
       try{
           TpsSystemInfo systemInfo = new TpsSystemInfo(kilde, userContextHolder.getUsername());
           String xmlresponse = tpsRequestService.executeEndringsmeldingRequest(request, systemInfo);
           EndringsmeldingResponse response = tpsResponseMappingUtils.xmlResponseToEndringsmeldingResponse(xmlresponse);
           return response;
       } catch (Exception exception){
           throw new HttpInternalServerErrorException(exception, "api/v1/endring");
       }
    }

    private void validateRequest(Map<String, Object> param) {
        if (param.get("environment") == null || param.get("environment").toString().equals("") ||
                param.get("kilde") == null) {
            throw new HttpBadRequestException(messageProvider.get("rest.service.request.exception.MissingRequiredParams"), "api/v1/endring");
        }
    }

}