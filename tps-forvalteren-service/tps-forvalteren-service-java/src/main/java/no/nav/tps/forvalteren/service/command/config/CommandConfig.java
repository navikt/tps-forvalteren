package no.nav.tps.forvalteren.service.command.config;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreEgenAnsatt;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreSikkerhetsTiltak;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.OpphørEgenAnsatt;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.OpphørSikkerhetsTiltak;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.M201HentFnrNavnDiskresjonPaFlerePersoner;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.M201HentFnrNavnDiskresjonPaFlerePersonerTestdata;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S000SjekkTpsTilgjengelig;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S002HentTknr;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S004HentPersonopplysninger;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S005Relasjoner;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S010Adressehistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S011HentFnrDnrHistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S013HentTknrHistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S015HentAdresselinjehistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S016Utvandring;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S103HentAdresser;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S137HentVergemaal;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S600HentKontaktinformasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAnnulleringAarsakskode45;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VigselAarsakskode11;
import no.nav.tps.forvalteren.service.command.Command;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    private static final String DEFAULT_ENVIRONMENT_NUMBER = "6";

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    @Value("${FASIT_ENVIRONMENT_NAME}")
    private String deployedEnvironment;

    @Bean
    MessageQueueConsumer defaultMessageQueueService() throws Exception {
        return messageQueueServiceFactory.createMessageQueueConsumer(deployedEnvironment, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);
    }

    @Bean
    XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return xmlMapper;
    }

    //    @Bean
    //    ObjectMapper objectMapper() {
    //        ObjectMapper objectMapper = new ObjectMapper();
    //        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    //        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    //        return objectMapper;
    //    }

    @Bean
    ServiceRoutineResolver sjekkTps() {
        return new S000SjekkTpsTilgjengelig();
    }

    @Bean
    ServiceRoutineResolver hentGT() {
        return new S610HentGT();
    }

    @Bean
    ServiceRoutineResolver hentTknr() {
        return new S002HentTknr();
    }

    @Bean
    ServiceRoutineResolver hentAdresser() {
        return new S103HentAdresser();
    }

    @Bean
    ServiceRoutineResolver hentAdresseLinjehistorikk() {
        return new S015HentAdresselinjehistorikk();
    }

    @Bean
    ServiceRoutineResolver hentAdressehistorikk() {
        return new S010Adressehistorikk();
    }

    @Bean
    ServiceRoutineResolver hentUtvandring() {
        return new S016Utvandring();
    }

    @Bean
    ServiceRoutineResolver hentHistorieForFlereFnrTestdata() {
        return new M201HentFnrNavnDiskresjonPaFlerePersonerTestdata();
    }

    @Bean
    ServiceRoutineResolver hentHistorieForFlereFnr() {
        return new M201HentFnrNavnDiskresjonPaFlerePersoner();
    }

    @Bean
    ServiceRoutineResolver hentKontaktinformasjon() {
        return new S600HentKontaktinformasjon();
    }

    @Bean
    ServiceRoutineResolver hentPersonSok() {
        return new S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver();
    }

    @Bean
    ServiceRoutineResolver hentFnrHistorikk() {
        return new S011HentFnrDnrHistorikk();
    }

    @Bean
    ServiceRoutineResolver hentTknrHistorikk() {
        return new S013HentTknrHistorikk();
    }

    @Bean
    ServiceRoutineResolver hentRelasjoner() {
        return new S005Relasjoner();
    }

    @Bean
    ServiceRoutineResolver hentPersonopplysninger() {
        return new S004HentPersonopplysninger();
    }

    @Bean
    ServiceRoutineResolver hentVergemaal() {
        return new S137HentVergemaal();
    }

    @Bean
    ServiceRoutineResolver endreEgenAnsatt() {
        return new EndreEgenAnsatt();
    }

    @Bean
    ServiceRoutineResolver endreSikkerhetsTiltak() {
        return new EndreSikkerhetsTiltak();
    }

    @Bean
    ServiceRoutineResolver opphorSikkerhetsTiltak() {
        return new OpphørSikkerhetsTiltak();
    }

    @Bean
    ServiceRoutineResolver opphosEgenAnsatt() {
        return new OpphørEgenAnsatt();
    }

    @Bean
    SkdMeldingResolver innvandring() {
        return new InnvandringAarsakskode02();
    }

    @Bean
    SkdMeldingResolver vigsel() {
        return new VigselAarsakskode11();
    }

    @Bean
    SkdMeldingResolver doedsmelding() {
        return new DoedsmeldingAarsakskode43();
    }

    @Bean
    SkdMeldingResolver doedsmeldingAnnuller() {
        return new DoedsmeldingAnnulleringAarsakskode45();
    }

}
