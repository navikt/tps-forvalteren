package no.nav.tps.forvalteren.service.command.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreEgenAnsatt;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreKontaktopplysninger;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreRelasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreSikkerhetstiltak;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.OpphoerEgenAnsatt;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.OpphoerSikkerhetstiltak;
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
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S018PersonHistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S051FinnGyldigeAdresser;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S103HentAdresser;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S137HentVergemaal;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S300HentKortPersonopplysningInklGT;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S301HentGDPR;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S600HentKontaktinformasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAnnulleringAarsakskode45;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.FoedselsmeldingAarsakskode01;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02Tildelingskode2Update;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmAnnenAvgang;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmDubletter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmForsvunnetAarsakskode82;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmStatsborgerskap;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.NavneEndringsmeldingAarsakskode06;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.UtvandringAarsakskode32;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VergemaalAarsakskode37;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VigselAarsakskode11;
import no.nav.tps.forvalteren.service.command.Command;

@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    @Bean
    XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return xmlMapper;
    }

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
    ServiceRoutineResolver finnGyldigeAdresser() {
        return new S051FinnGyldigeAdresser();
    }

    @Bean
    ServiceRoutineResolver hentVergemaal() {
        return new S137HentVergemaal();
    }

    @Bean
    ServiceRoutineResolver personhistorikk() {
        return new S018PersonHistorikk();
    }

    @Bean
    ServiceRoutineResolver hentGDPRdata() {
        return new S301HentGDPR();
    }

    @Bean
    ServiceRoutineResolver hentPersonInfoKortGT() {
        return new S300HentKortPersonopplysningInklGT();
    }

    @Bean
    ServiceRoutineResolver endreEgenAnsatt() {
        return new EndreEgenAnsatt();
    }

    @Bean
    ServiceRoutineResolver endreSikkerhetsTiltak() {
        return new EndreSikkerhetstiltak();
    }

    @Bean
    ServiceRoutineResolver endreRelasjon() {
        return new EndreRelasjon();
    }

    @Bean
    ServiceRoutineResolver opphorSikkerhetsTiltak() {
        return new OpphoerSikkerhetstiltak();
    }

    @Bean
    ServiceRoutineResolver opphosEgenAnsatt() {
        return new OpphoerEgenAnsatt();
    }

    @Bean
    public SkdMeldingResolver innvandring() {
        return new InnvandringAarsakskode02();
    }

    @Bean
    SkdMeldingResolver innvandringUpdate() {
        return new InnvandringAarsakskode02Tildelingskode2Update();
    }

    @Bean
    SkdMeldingResolver navnEndringsmelding() {
        return new NavneEndringsmeldingAarsakskode06();
    }

    @Bean
    SkdMeldingResolver vigsel() {
        return new VigselAarsakskode11();
    }

    @Bean
    SkdMeldingResolver foedselsmelding() {
        return new FoedselsmeldingAarsakskode01();
    }

    @Bean
    SkdMeldingResolver doedsmelding() {
        return new DoedsmeldingAarsakskode43();
    }

    @Bean
    SkdMeldingResolver doedsmeldingAnnuller() {
        return new DoedsmeldingAnnulleringAarsakskode45();
    }

    @Bean
    SkdMeldingResolver utvandringsmelding() {
        return new UtvandringAarsakskode32();
    }

    @Bean
    SkdMeldingResolver meldingOmForsvunnet() {
        return new MeldingOmForsvunnetAarsakskode82();
    }

    @Bean
    SkdMeldingResolver meldingOmDubletter() {
        return new MeldingOmDubletter();
    }

    @Bean
    SkdMeldingResolver meldingOmStatsborgerskap() {
        return new MeldingOmStatsborgerskap();
    }

    @Bean
    MeldingOmAnnenAvgang meldingOmAnnenAvgang() {
        return new MeldingOmAnnenAvgang();
    }

    @Bean
    SkdMeldingResolver vergemaal() {
        return new VergemaalAarsakskode37();
    }

    @Bean EndreKontaktopplysninger kontaktopplysninger() {
        return new EndreKontaktopplysninger();
    }

}
