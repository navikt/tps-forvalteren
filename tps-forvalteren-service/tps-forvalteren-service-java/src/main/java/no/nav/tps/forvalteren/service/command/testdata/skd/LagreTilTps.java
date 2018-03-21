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
		
		sendInnvandringsMeldinger(listTpsResponsene,personerSomIkkeEksitererITpsMiljoe,environmentsSet);
		sendRelasjonsmeldinger(listTpsResponsene,personerSomIkkeEksitererITpsMiljoe,environmentsSet);
		sendDoedsmeldinger(listTpsResponsene, gruppeId, environmentsSet);
		
		return new RsSkdMeldingResponse(gruppeId, listTpsResponsene);
	}
	
	private void sendDoedsmeldinger(List<SendSkdMeldingTilTpsResponse> listTpsResponsene, Long gruppeId, Set<String> environmentsSet) {
		List<String> doedsMeldinger = createDoedsmeldinger.execute(gruppeId, true);
		doedsMeldinger.forEach(skdMelding -> {
			SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Doedsmelding", skdMelding, environmentsSet);
			listTpsResponsene.add(tpsResponse);
		});
	}
	
	private void sendRelasjonsmeldinger(List<SendSkdMeldingTilTpsResponse> listTpsResponsene, List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
		List<String> relasjonsMeldinger = createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, true);
		relasjonsMeldinger.forEach(skdMelding -> {
			SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Relasjonsmelding", skdMelding, environmentsSet);
			listTpsResponsene.add(tpsResponse);
		});
	}
	
	private void sendInnvandringsMeldinger(List<SendSkdMeldingTilTpsResponse> listTpsResponsene, List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
		List<String> innvandringsMeldinger = skdMessageCreatorTrans1.execute(NAVN_INNVANDRINGSMELDING, personerSomIkkeEksitererITpsMiljoe, true);
		innvandringsMeldinger.forEach(skdMelding -> {
			SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Innvandringsmelding", skdMelding, environmentsSet);
			listTpsResponsene.add(tpsResponse);
		});
	}
	
	private Map<String, String> mapStatus(Map<String, String> responseSkdMeldingerPerEnv, Set<String> environmentsSet) {
		responseSkdMeldingerPerEnv.replaceAll((env,status)->status.equals("00") ? "OK" : status);
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
