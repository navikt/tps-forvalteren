package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateMeldingWithMeldingstype;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingerFromText;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateDoedsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateRelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;

@Service
public class TestdataGruppeToSkdEndringsmeldingGruppe {

    private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private CreateRelasjoner createRelasjoner;

    @Autowired
    private CreateDoedsmeldinger createDoedsmeldinger;

    @Autowired
    private GruppeRepository gruppeRepository;

    @Autowired
    private CreateMeldingWithMeldingstype createMeldingWithMeldingstype;

    @Autowired
    private SaveSkdEndringsmeldingerFromText saveSkdEndringsmeldingerFromText;

    public SkdEndringsmeldingGruppe execute(Long gruppeId) {
        Gruppe testdataGruppe = gruppeRepository.findById(gruppeId);

        if (testdataGruppe != null) {
            SkdEndringsmeldingGruppe gruppe = new SkdEndringsmeldingGruppe();
            gruppe.setNavn(setNavnWithUniqueId(testdataGruppe.getNavn()));
            gruppe.setBeskrivelse(testdataGruppe.getBeskrivelse());

            List<String> skdMeldinger = new ArrayList<>();
            List<String> innvandringsMeldinger = skdMessageCreatorTrans1.execute(NAVN_INNVANDRINGSMELDING, testdataGruppe.getPersoner(), false);
            List<String> relasjonsMeldinger = createRelasjoner.execute(testdataGruppe.getPersoner(), false);
            List<String> doedsMeldinger = createDoedsmeldinger.execute(gruppeId, false);
            skdMeldinger.addAll(innvandringsMeldinger);
            skdMeldinger.addAll(relasjonsMeldinger);
            skdMeldinger.addAll(doedsMeldinger);

            gruppe = skdEndringsmeldingGruppeRepository.save(gruppe);
            List<RsMeldingstype> rsMeldinger = createMeldingWithMeldingstype.execute(skdMeldinger);
            saveSkdEndringsmeldingerFromText.execute(rsMeldinger, gruppe.getId());
            return gruppe;
        } else {
            throw new GruppeNotFoundException(messageProvider.get(GRUPPE_NOT_FOUND_KEY, gruppeId));
        }

    }

    private String setNavnWithUniqueId(String navn) {
        String identifier = " (" + SECURE_RANDOM.nextInt(9999) + ")";
        if (navn.length() + identifier.length() <= 50) {
            return navn + identifier;
        } else {
            return navn.substring(0, 50 - identifier.length()) + identifier;
        }
    }

}
