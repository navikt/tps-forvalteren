package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LagreTilTps {
	
	private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";
	
	@Autowired
	private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;
	
	@Autowired
	private FindPersonsNotInEnvironments findPersonsNotInEnvironments;
	
	@Autowired
	private CreateRelasjoner createRelasjoner;
	
	@Autowired
	private CreateDoedsmeldinger createDoedsmeldinger;
	
	@Autowired
	private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;
	
	@Autowired
	private SkdMeldingResolver innvandring;
	private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;
	
	public RsSkdMeldingResponse execute(Long gruppeId, List<String> environments) {
		skdRequestMeldingDefinition = innvandring.resolve();
		List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
		Set<String> environmentsSet = new HashSet<>(environments);
		List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(gruppeId, environments);
		
		listTpsResponsene.addAll( sendInnvandringsMeldinger(personerSomIkkeEksitererITpsMiljoe,environmentsSet));
		listTpsResponsene.addAll( sendRelasjonsmeldinger(personerSomIkkeEksitererITpsMiljoe,environmentsSet));
		listTpsResponsene.addAll( sendDoedsmeldinger( gruppeId, environmentsSet));
		
		return new RsSkdMeldingResponse(gruppeId, listTpsResponsene);
	}
	
	private List<SendSkdMeldingTilTpsResponse> sendDoedsmeldinger( Long gruppeId, Set<String> environmentsSet) {
		List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
		List<String> doedsMeldinger = createDoedsmeldinger.execute(gruppeId, true);
		doedsMeldinger.forEach(skdMelding -> {
			SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Doedsmelding", skdMelding, environmentsSet);
			listTpsResponsene.add(tpsResponse);
		});
		return listTpsResponsene;
	}
	
	private List<SendSkdMeldingTilTpsResponse> sendRelasjonsmeldinger(List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
		List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
		List<String> relasjonsMeldinger = createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, true);
		relasjonsMeldinger.forEach(skdMelding -> {
			SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Relasjonsmelding", skdMelding, environmentsSet);
			listTpsResponsene.add(tpsResponse);
		});
		return listTpsResponsene;
	}
	
	private List<SendSkdMeldingTilTpsResponse> sendInnvandringsMeldinger( List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
		List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
		List<String> innvandringsMeldinger = skdMessageCreatorTrans1.execute(NAVN_INNVANDRINGSMELDING, personerSomIkkeEksitererITpsMiljoe, true);
		innvandringsMeldinger.forEach(skdMelding -> {
			SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Innvandringsmelding", skdMelding, environmentsSet);
			listTpsResponsene.add(tpsResponse);
		});
		return listTpsResponsene;
	}
	
	private Map<String, String> mapStatus(Map<String, String> responseSkdMeldingerPerEnv, Set<String> environmentsSet) {
		responseSkdMeldingerPerEnv.replaceAll((env,status)->"00".equals(status) ? "OK" : status);
		environmentsSet.forEach(env -> responseSkdMeldingerPerEnv.putIfAbsent(env, "Environment is not deployed"));
		return responseSkdMeldingerPerEnv;
	}
	
	private SendSkdMeldingTilTpsResponse sendSkdMeldingTilGitteMiljoer(String skdmeldingstype, String skdMelding, Set<String> environmentsSet) {
		Map<String, String> responseSkdMeldingerPerEnv = sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, environmentsSet);
		
		return SendSkdMeldingTilTpsResponse.builder()
				.personId(skdMelding.substring(0,11))//TODO Dette er rotete og midlertidig, inntil vi får forbedret skdFeltContainer -> skdMelding javaklasse. Her bør det lages enum eller Java-klasse for de ulike feltene i skdmeldingen
				.skdmeldingstype(skdmeldingstype)
				.status(mapStatus(responseSkdMeldingerPerEnv, environmentsSet))
				.build();
	}
	
}
