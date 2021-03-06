package no.nav.tps.forvalteren.service.command.foedselsmelding;

import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.FAR;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.LAGNY;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.MOR;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.FoedselsmeldingAarsakskode01.FOEDSEL_MLD_NAVN;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.codehaus.plexus.util.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmeldingRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.ExtractErrorStatus;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonhistorikkService;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@Service
public class SendTpsFoedselsmeldingService {

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
    private SkdMeldingResolver foedselsmelding;

    @SuppressWarnings("fb-contrib:WOC_WRITE_ONLY_COLLECTION_LOCAL")
    public SendSkdMeldingTilTpsResponse sendFoedselsmelding(RsTpsFoedselsmeldingRequest request) {

        validate(request);
        S018PersonType persondataMor = null;
        S018PersonType persondataFar = null;
        Map<String, String> sentStatus = new HashMap<>(request.getMiljoer().size());

        Iterator<String> miljoeIterator = request.getMiljoer().iterator();
        while (miljoeIterator.hasNext()) {
            String miljoe = miljoeIterator.next();
            try {
                persondataMor = getPersonhistorikk(request.getIdentMor(), request.getFoedselsdato(), miljoe);

                if (isNotBlank(request.getIdentFar())) {
                    persondataFar = getPersonhistorikk(request.getIdentFar(), request.getFoedselsdato(), miljoe);
                }
            } catch (TpsfTechnicalException | TpsfFunctionalException e) {
                sentStatus.put(miljoe, format("FEIL: %s", e.getMessage()));
                miljoeIterator.remove();
            }
        }

        Person person = null;
        if (!request.getMiljoer().isEmpty()) {
            person = opprettPersonMedEksisterendeForeldreService.execute(request);
            prepNyAdresse(request, persondataMor, persondataFar, person);
            person.toUppercase();

            for (String miljoe : request.getMiljoer()) {
                try {
                    sentStatus.putAll(sendMeldingToTps(person, miljoe));
                } catch (TpsfFunctionalException | TpsfTechnicalException e) {
                    sentStatus.put(miljoe, format("FEIL: %s", e.getMessage()));
                }
            }
        }
        return prepareStatus(sentStatus, nonNull(person) ? person.getIdent() :
                request.getFoedselsdato().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    private void prepNyAdresse(RsTpsFoedselsmeldingRequest request, S018PersonType persondataMor, S018PersonType persondataFar, Person person) {
        if (LAGNY != request.getAdresseFra()) {
            AdresserResponse adresser = findAdresse(request, persondataMor, persondataFar);
            if (nonNull(adresser)) {
                if (nonNull(adresser.getBoadresse())) {
                    person.setBoadresse(asList(adresser.getBoadresse()));
                }
                if (nonNull(adresser.getPostadresse())) {
                    person.setPostadresse(asList(adresser.getPostadresse()));
                }
            }
        }
    }

    private void validate(RsTpsFoedselsmeldingRequest request) {
        if (!request.validatesOk()) {
            throw new TpsfFunctionalException("Påkrevet parameter mangler.");
        }
        if (isBlank(request.getIdentFar()) && request.getAdresseFra() == FAR) {
            throw new TpsfFunctionalException("Suppler ident fra far for å kunne hente adresse fra TPS.");
        }
    }

    private AdresserResponse findAdresse(RsTpsFoedselsmeldingRequest request, S018PersonType persondataMor, S018PersonType persondataFar) {

        if (isNull(request.getAdresseFra()) || request.getAdresseFra() == MOR) {
            return personAdresseService.hentAdresserForDato(persondataMor, request.getFoedselsdato());

        } else if (request.getAdresseFra() == FAR && nonNull(persondataFar)) {
            return personAdresseService.hentAdresserForDato(persondataFar, request.getFoedselsdato());
        }

        return null;
    }

    private S018PersonType getPersonhistorikk(String ident, LocalDateTime date, String env) {
        try {
            return personhistorikkService.hentPersonhistorikk(ident, date, env);
        } catch (TpsfTechnicalException e) {
            throw new TpsfFunctionalException(format("Person med ident %s finnes ikke i miljø %s.", ident, env), e);
        }
    }

    private Map sendMeldingToTps(Person personSomSkalFoedes, String miljoe) {

        SkdMeldingTrans1 melding = skdMessageCreatorTrans1.execute(FOEDSEL_MLD_NAVN, personSomSkalFoedes, true);
        return sendSkdMeldingTilGitteMiljoer.execute(melding.toString(), foedselsmelding.resolve(), newHashSet(miljoe));
    }

    private SendSkdMeldingTilTpsResponse prepareStatus(Map<String, String> sentStatus, String ident) {
        sentStatus.replaceAll((env, status) -> status.matches("^00.*") ? "OK" : ExtractErrorStatus.extract(status));
        return SendSkdMeldingTilTpsResponse.builder()
                .personId(ident)
                .skdmeldingstype(FOEDSEL_MLD_NAVN)
                .status(sentStatus)
                .build();
    }
}