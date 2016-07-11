package no.nav.tps.vedlikehold.consumer.ws.fasit;


import no.nav.aura.envconfig.client.DomainDO;

public class FasitUtils {

    public static DomainDO domainFor(String environment) {
        switch(environment.charAt(0)) {
            case 'p':
                return DomainDO.Adeo;
            case 'q':
                return DomainDO.PreProd;
            case 't':
                return DomainDO.TestLocal;
            case 'r':
            case 's':
            case 'u':
            default:
                return DomainDO.Devillo;
        }
    }

}