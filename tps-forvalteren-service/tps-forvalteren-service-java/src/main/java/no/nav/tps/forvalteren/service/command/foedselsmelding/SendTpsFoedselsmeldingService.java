package no.nav.tps.forvalteren.service.command.foedselsmelding;

import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.FAR;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.LAGNY;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.MOR;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmeldingRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.UppercaseDataInPerson;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonhistorikkService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import no.nav.tps.xjc.ctg.domain.s018.PersonStatus;
import no.nav.tps.xjc.ctg.domain.s018.PersonstatusType;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@Service
public class SendTpsFoedselsmeldingService {

    private static final String NAVN_FOEDSELSMELDING = "Foedselsmelding";

    @Autowired
    private PersonhistorikkService personhistorikkService;

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private PersonAdresseService personAdresseService;

    @Autowired
    private OpprettPersonMedEksisterendeForeldreService opprettPersonMedEksisterendeForeldreService;

    @Autowired
    private UppercaseDataInPerson uppercaseDataInPerson;

    @Autowired
    private SkdMeldingResolver foedselsmelding;

    public SendSkdMeldingTilTpsResponse sendFoedselsmelding(RsTpsFoedselsmeldingRequest request) {

        validate(request);
        S018PersonType persondataMor = getPersonhistorikk(request.getIdentMor(), request.getFoedselsdato(), request.getMiljoe());
        checkBosatt("Mor", persondataMor.getPersonStatus(), request.getFoedselsdato());

        S018PersonType persondataFar = null;
        if (StringUtils.isNotBlank(request.getIdentFar())) {
            persondataFar = getPersonhistorikk(request.getIdentFar(), request.getFoedselsdato(), request.getMiljoe());
            if (FAR == request.getAdresseFra()) {
                checkBosatt("Far", persondataFar.getPersonStatus(), request.getFoedselsdato());
            }
        }

        Person person = opprettPersonMedEksisterendeForeldreService.execute(request);
        if (LAGNY != request.getAdresseFra()) {
            person.setBoadresse(findAdresse(request, persondataMor, persondataFar));
        }

        uppercaseDataInPerson.execute(person);
        return sendMeldingToTps(person, request.getMiljoe());
    }

    private void validate(RsTpsFoedselsmeldingRequest request) {
        if (!request.validatesOk()) {
            throw new TpsfFunctionalException("Påkrevet parameter mangler.");
        }
        if (isBlank(request.getIdentFar()) && request.getAdresseFra() == FAR) {
            throw new TpsfFunctionalException("Suppler ident fra far for å kunne hente adresse fra TPS.");
        }
    }

    private Adresse findAdresse(RsTpsFoedselsmeldingRequest request, S018PersonType persondataMor, S018PersonType persondataFar) {

        if (request.getAdresseFra() == null || request.getAdresseFra() == MOR) {
            return personAdresseService.getBoAdresse(persondataMor.getBostedsAdresse(), request.getFoedselsdato());
        } else if (request.getAdresseFra() == FAR && persondataFar != null) {
            return personAdresseService.getBoAdresse(persondataFar.getBostedsAdresse(), request.getFoedselsdato());
        }
        return null;
    }

    private S018PersonType getPersonhistorikk(String ident, LocalDateTime date, String env) {
        try {
            return personhistorikkService.hentPersonhistorikk(ident, date, env);
        } catch (TpsfTechnicalException e) {
            throw new TpsfFunctionalException(String.format("Person med ident %s finnes ikke i miljø %s.", ident, env), e);
        }
    }

    private void checkBosatt(String forelder, List<PersonstatusType> personStatuses, LocalDateTime foedselsdag) {
        for (PersonstatusType personStatus : personStatuses) {
            if (PersonStatus.BOSA == personStatus.getKodePersonstatus() &&
                    foedselsdag.compareTo(ConvertStringToDate.yyyysMMsdd(personStatus.getDatoFom())) >= 0 &&
                    (isBlank(personStatus.getDatoTom()) ||
                            foedselsdag.compareTo(ConvertStringToDate.yyyysMMsdd(personStatus.getDatoTom())) <= 0)) {
                return;
            }
        }
        throw new TpsfFunctionalException(String.format("%s var ikke bosatt på gitt dato.", forelder));
    }

    private SendSkdMeldingTilTpsResponse sendMeldingToTps(Person personSomSkalFoedes, String miljoe) {

        SkdMeldingTrans1 melding = skdMessageCreatorTrans1.execute(NAVN_FOEDSELSMELDING, personSomSkalFoedes, true);
        Map<String, String> sentStatus = sendSkdMeldingTilGitteMiljoer.execute(melding.toString(), foedselsmelding.resolve(), Sets.newHashSet(miljoe));

        sentStatus.replaceAll((env, status) -> status.matches("^00.*") ? "OK" : status);
        return SendSkdMeldingTilTpsResponse.builder()
                .personId(melding.getFodselsnummer())
                .skdmeldingstype(NAVN_FOEDSELSMELDING)
                .status(sentStatus)
                .build();
    }
}