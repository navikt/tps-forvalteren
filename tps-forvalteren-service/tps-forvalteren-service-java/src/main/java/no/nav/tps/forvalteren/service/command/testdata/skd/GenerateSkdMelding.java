package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.VergemaalSkdParameterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;

@Service
public class GenerateSkdMelding {

    private SkdParametersCreatorService skdParametersCreatorService;
    private SkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding;

    @Autowired
    private VergemaalSkdParameterStrategy vergemaalSkdParameterStrategy;

    @Autowired
	public GenerateSkdMelding(SkdParametersCreatorService skdParametersCreatorService, SkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding) {
		this.skdParametersCreatorService = skdParametersCreatorService;
		this.skdGetHeaderForSkdMelding = skdGetHeaderForSkdMelding;
	}
	
	public SkdMeldingTrans1 execute(TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, Person person, boolean addHeader) {
		SkdMeldingTrans1 skdMelding = skdParametersCreatorService.execute(skdRequestMeldingDefinition, person);
        if (addHeader) {
			skdMelding.setHeader( skdGetHeaderForSkdMelding.execute(skdMelding));
        }
        return skdMelding;
    }
    public SkdMeldingTrans1 execute(Vergemaal vergemaal, boolean addHeader){
        SkdMeldingTrans1 skdMelding = vergemaalSkdParameterStrategy.execute(vergemaal);
        if (addHeader){
            skdMelding.setHeader(skdGetHeaderForSkdMelding.execute(skdMelding));
        }
        return skdMelding;
    }
}