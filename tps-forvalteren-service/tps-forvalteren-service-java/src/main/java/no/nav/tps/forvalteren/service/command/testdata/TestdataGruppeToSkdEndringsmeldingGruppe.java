package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.common.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateMeldingWithMeldingstypeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingerService;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateDoedsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateFoedselsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateRelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateVergemaal;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.UtvandringOgInnvandring;

@Service
public class TestdataGruppeToSkdEndringsmeldingGruppe {

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
    private CreateVergemaal createVergemaal;

    @Autowired
    private UtvandringOgInnvandring utvandringOgInnvandring;

    @Autowired
    private GruppeRepository gruppeRepository;

    @Autowired
    private CreateFoedselsmeldinger createFoedselsmeldinger;

    @Autowired
    private CreateMeldingWithMeldingstypeService createMeldingWithMeldingstypeService;

    @Autowired
    private SaveSkdEndringsmeldingerService saveSkdEndringsmeldingerService;

    public SkdEndringsmeldingGruppe execute(Long gruppeId) {
        Gruppe testdataGruppe = gruppeRepository.findById(gruppeId);

        if (testdataGruppe != null) {
            SkdEndringsmeldingGruppe gruppe = new SkdEndringsmeldingGruppe();
            gruppe.setNavn(setNavnWithUniqueId(testdataGruppe.getNavn()));
            gruppe.setBeskrivelse(testdataGruppe.getBeskrivelse());

            List<SkdMelding> skdMeldinger = new ArrayList<>();
            List<SkdMeldingTrans1> foedselsMeldinger = createFoedselsmeldinger.executeFromPersons(testdataGruppe.getPersoner(), false);
            List<SkdMeldingTrans1> innvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_CREATE_MLD_NAVN, testdataGruppe.getPersoner(), false);
            List<SkdMelding> relasjonsMeldinger = createRelasjoner.execute(testdataGruppe.getPersoner(), false);
            List<SkdMeldingTrans1> doedsMeldinger = createDoedsmeldinger.execute(testdataGruppe.getPersoner(), null, false);
            List<SkdMeldingTrans1> vergemaalMeldinger = createVergemaal.execute(testdataGruppe.getPersoner(), false);
            List<SkdMeldingTrans1> utvandringsMeldinger = utvandringOgInnvandring.createMeldinger(testdataGruppe.getPersoner(), false);
            skdMeldinger.addAll(foedselsMeldinger);
            skdMeldinger.addAll(innvandringsMeldinger);
            skdMeldinger.addAll(relasjonsMeldinger);
            skdMeldinger.addAll(doedsMeldinger);
            skdMeldinger.addAll(utvandringsMeldinger);
            skdMeldinger.addAll(vergemaalMeldinger);

            gruppe = skdEndringsmeldingGruppeRepository.save(gruppe);
            List<RsMeldingstype> rsMeldinger = createMeldingWithMeldingstypeService.execute(skdMeldinger);
            saveSkdEndringsmeldingerService.save(rsMeldinger, gruppe.getId());
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
