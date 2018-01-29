package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.util.List;

import no.nav.tps.forvalteren.domain.rs.dodsmelding.RsDodsmeldingType;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.ConvertMeldingFromJsonToText;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CreateDodsmelding {

    private ConvertJsonToRsDodsmeldingType convertJsonToRsDodsmeldingType;

    public void execute(JSONObject dodsmelding) {

        //String skdDodsmelding = convertJsonToRsDodsmeldingType.execute(dodsmelding);

    }



    /*
    public void execute(Long gruppeId, String environment) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        if (gruppe != null) {
            List<SkdEndringsmelding> skdEndringsmeldinger = skdEndringsmeldingRepository.findAllByGruppe(gruppe);

            List<RsMeldingstype> rsMeldingstyper = skdEndringsmeldinger.stream()
                    .map(melding -> convertJsonToRsMeldingstype.execute(melding))
                    .collect(Collectors.toList());

            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
            for (RsMeldingstype melding : rsMeldingstyper) {
                String skdMelding = convertMeldingFromJsonToText.execute(melding);
                StringBuilder skdMeldingMedHeader = skdAddHeaderToSkdMelding.execute(new StringBuilder(skdMelding));
                sendSkdMeldingTilGitteMiljoer.execute(skdMeldingMedHeader.toString(), skdRequestMeldingDefinition, new HashSet<>(Arrays.asList(environment)));
                saveLogg(skdMelding, melding, gruppeId, environment);
            }
            skdStartAjourhold.execute(new HashSet<>(Arrays.asList(environment)));
        } else {
            throw new SkdEndringsmeldingGruppeNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, gruppeId));
        }

    }




     */




    }






