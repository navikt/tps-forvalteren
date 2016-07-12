package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.domain.rs.User;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.temp.AuthorisationService;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.temp.ResponseService;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.temp.ServiceResponse;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    UserContextHolder userContextHolder;

    private AuthorisationService authorisationService = new AuthorisationService();

    private final String responseXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData xmlns=\"http://www.rtv.no/NamespaceTPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.rtv.no/NamespaceTPS W:\\PSDAT~2.XSD\"> <tpsServiceRutine><serviceRutinenavn>FS03-FDNUMMER-PERSDATA-O.xml</serviceRutinenavn><fnr>12345678901</fnr><aksjonsDato/><aksjonsKode>A</aksjonsKode><aksjonsKode2>0</aksjonsKode2></tpsServiceRutine><tpsSvar><svarStatus><returStatus>00</returStatus><returMelding/><utfyllendeMelding/></svarStatus><personDataS004><fnr>12345678901</fnr><kortnavn>NORDMANN KARI</kortnavn><fornavn>KARI</fornavn><mellomnavn/><etternavn>NORDMANN</etternavn><spesregType/><boAdresse1>STEINRØYSA 10 C</boAdresse1><boAdresse2/><bolignr/><postAdresse1/><postAdresse2/><postAdresse3/><postnr>2322</postnr><kommunenr>0403</kommunenr><tknr>0403</tknr><tlfPrivat><tlfNummer/><tlfTidspunktReg/><tlfSystem/><tlfSaksbehandler/></tlfPrivat><tlfJobb><tlfNummer/><tlfTidspunktReg/><tlfSystem/><tlfSaksbehandler/></tlfJobb><tlfMobil><tlfNummer/><tlfTidspunktReg/><tlfSystem/><tlfSaksbehandler/></tlfMobil><epost><epostAdresse/><epostTidspunktReg/><epostSystem/><epostSaksbehandler/></epost><tidligereKommunenr>0412</tidligereKommunenr><datoFlyttet>2002-08-30</datoFlyttet><personStatus>Bosatt</personStatus><statsborger>NORGE</statsborger><datoStatsborger/><sivilstand>Ugift</sivilstand><datoSivilstand/><datoDo/><datoUmyndiggjort/><innvandretFra/><datoInnvandret/><utvandretTil/><datoUtvandret/><giroInfo><giroNummer>12345678901</giroNummer><giroTidspunktReg/><giroSystem/><giroSaksbehandler/></giroInfo></personDataS004></tpsSvar></tpsPersonData>";

    @RequestMapping(
            value = "/service/{serviceName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> getService(@ApiIgnore HttpSession session,
                                        @PathVariable("serviceName") String serviceName,
                                        @RequestParam Map<String, ?> parameters) throws Exception  {

        /* Verify authorisation */
        String identifier = (String) parameters.get("fnr");
        Set<String> roles = userContextHolder.getRoles()
                                             .stream()
                                             .map(GrantedAuthority::getAuthority)
                                             .collect(toSet());

        if ( !authorisationService.isAuthorisedToRetrievePerson(identifier, roles) ) {
            return ResponseService.unauthorized();
        }

        /* Build the message */
        String xmlMessage = messageForService(serviceName, parameters);

        if (xmlMessage == null) {
            return ResponseService.badRequest("'" + serviceName + "' is not defined");
        }

        /* Prepare the response object */
        ServiceResponse response = new ServiceResponse();

        response.setXml(xmlMessage);
        response.setObject(XML.toJSONObject(xmlMessage).toString());

        return ResponseService.ok(response);
    }

    private String messageForService(String serviceName, Map<String, ?> parameters) {
        IContext templateContext = contextForService(serviceName, parameters);

        return templateEngine.process(serviceName.toUpperCase(), templateContext);
    }

    private IContext contextForService(String serviceName, Map<String, ?> parameters) {
        Context context = new Context();

        context.setVariable("serviceName", serviceName);
        context.setVariables(parameters);

        return context;
    }

}
