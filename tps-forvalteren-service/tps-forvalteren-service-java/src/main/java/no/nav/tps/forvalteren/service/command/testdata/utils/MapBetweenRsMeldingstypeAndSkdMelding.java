package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;

@Component
public class MapBetweenRsMeldingstypeAndSkdMelding {
    
    private BoundMapperFacade<SkdMeldingTrans1, RsMeldingstype1Felter> mapper = constructMapper();

    /**
     * Metoden mapper alle felter som eksisterer i skdMeldingTrans1. SkdMeldingTrans1 inneholder ikke RsMeldingstype sin "id" og "beskrivelse"
     */
    public RsMeldingstype1Felter map(SkdMeldingTrans1 skdMeldingTrans1) {
        return mapper.map(skdMeldingTrans1);
    }
    
    public SkdMeldingTrans1 mapReverse(RsMeldingstype1Felter rsMelding) {
        return mapper.mapReverse(rsMelding);
    }

    private BoundMapperFacade constructMapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(SkdMeldingTrans1.class, RsMeldingstype1Felter.class)
                .field("ektefellePartnerFoedselsdato", "ektefellePartnerFdato")
                .field("ektefellePartnerPersonnr", "ektefellePartnerPnr")
                .field("postadresse1", "adresse1")
                .field("postadresse2", "adresse2")
                .field("postadresse3", "adresse3")
                .field("postadresseLand", "postadrLand")
                .field("ekteskapPartnerskapNr", "ekteskPartnskNr")
                .field("ektefelleEkteskapPartnerskapNr", "ektfEkteskPartnskNr")
                .field("tidligereSivilstand", "tidlSivilstand")
                .field("ektefelleTidligereSivilstand", "ektfTidlSivilstand")
                .field("vigselskommune", "vigselskomm")
                .field("slektsnavnUgift","slekstnavnUgift")
                .field("regdatoStatsb","statsborgerskapRegdato")
                .field("farsNavn","farsFarsNavn")
                .field("kjoenn","kjonn")
                .field("morsSivilstand","morsSiviltilstand")
                .byDefault().register();
        return mapperFactory.getMapperFacade(SkdMeldingTrans1.class, RsMeldingstype1Felter.class);
    }
}
