package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.ConvertMeldingFromJsonToText;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;


@Service
public class CreateDodsmelding {

    @Autowired
    private DeathRowRepository deathRowRepository;

    public void execute(RsDeathRow dodsmelding) {
        DeathRow death;

        List<String> identer = Arrays.asList(dodsmelding.getIdent());
        List<DeathRow> deathRows = new ArrayList<>();

        for(int i = 0; i < identer.size(); i++) {
            death = new DeathRow();

            death.setId(dodsmelding.getId()+1);
            death.setIdent(identer.get(i));
            death.setHandling(dodsmelding.getHandling());
            death.setMiljoe(dodsmelding.getMiljoe());
            death.setDoedsdato(dodsmelding.getDoedsdato());
            death.setStatus(dodsmelding.getStatus());
            death.setTilstand(dodsmelding.getTilstand());

            deathRows.add(death);
        }

        for(DeathRow deathRow : deathRows) {
            deathRowRepository.save(deathRow);
        }
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






