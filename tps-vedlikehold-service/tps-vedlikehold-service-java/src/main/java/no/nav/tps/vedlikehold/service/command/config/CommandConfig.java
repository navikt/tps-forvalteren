package no.nav.tps.vedlikehold.service.command.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers.*;
import no.nav.tps.vedlikehold.service.command.Command;

import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.xml.XmlMapper;

import static no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
\ */
@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    private static final String DEFAULT_ENV = "t4";

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    @Bean
    MessageQueueConsumer defaultMessageQueueService() throws Exception {
        return messageQueueServiceFactory.createMessageQueueService(DEFAULT_ENV, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);
    }

    @Bean
    XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return xmlMapper;
    }

    @Bean
    ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    @Bean
    @Order(1)
    ServiceRoutineResolver hentPersonServiceRoutineResolver() {
        return new S004HentPersonopplysningerServiceRoutineResolver();
    }

    @Bean
    @Order(2)
    ServiceRoutineResolver hentKontaktinformasjonRoutineResolver() {
        return new S600HentKontaktinformasjonServiceRoutineResolver();
    }

    @Bean
    @Order(3)
    ServiceRoutineResolver sokPersonRoutineResolver() {
        return new S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver();
    }

    @Bean
    @Order(3)
    ServiceRoutineResolver hentGironummerResolver() {
        return new S102HentGironummerServiceRoutineResolver();
    }

    @Bean
    @Order(4)
    ServiceRoutineResolver hentLinjeadresseResolver(){
        return new S103HentLinjeadresser();
    }

    @Bean
    @Order(5)
    ServiceRoutineResolver hentOppholdsArbeidsTillatlse(){
        return new S101HentOppholdArbeidTillatelseOgUtenlandskId();
    }

    @Bean
    @Order(6)
    ServiceRoutineResolver hentTKNrEndringshistorie(){
        return new S013HentTKNrEndringshistorie();
    }

    @Bean
    @Order(7)
    ServiceRoutineResolver hentBrukerprofil(){
        return new S120HentBrukerprofil();
    }

    @Bean
    @Order(7)
    ServiceRoutineResolver hentFnrEndringshistorie(){
        return new S011HentFnrEndringshistorie();
    }

    @Bean
    @Order(7)
    ServiceRoutineResolver sjekkFnrErDefinert(){
        return new S100SjekkOmFnrDnrBnrErDefinertITPS();
    }

    @Bean
    @Order(7)
    ServiceRoutineResolver endreNavnResolver() {
        return new EndreNavn();
    }

    @Bean
    @Order(8)
    ServiceRoutineResolver endreTIADResolver() {
        return new EndreTIAD();
    }

    @Bean
    @Order(9)
    ServiceRoutineResolver endreNorskGironummerResolver() {
        return new EndreNorskGironummer();
    }

    @Bean
    @Order(10)
    ServiceRoutineResolver endreUtlandskGironummerResolver() {
        return new EndreUtenlandskGironummer();
    }

    @Bean
    @Order(10)
    ServiceRoutineResolver hentMangeFnrHistorier(){
        return new M201HentFnrNavnDiskresjonPaFlerePersoner();
    }

    @Bean
    @Order(Integer.MAX_VALUE)
    ServiceRoutineResolver tilgangTilTpsServiceRoutineResolver() {
        return new S000TilgangTilTpsServiceRoutineResolver();
    }

}
