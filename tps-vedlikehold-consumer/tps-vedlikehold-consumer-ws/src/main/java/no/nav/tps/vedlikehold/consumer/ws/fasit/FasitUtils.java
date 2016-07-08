package no.nav.tps.vedlikehold.consumer.ws.fasit;


import no.nav.aura.envconfig.client.DomainDO;

public class FasitUtils {
    public static final String FASIT_BASE_URL   = "https://fasit.adeo.no/conf/";
    public static final String DEFAULT_USER     = "admin";
    public static final String DEFAULT_PASSWORD = "admin";
    public static final String DEFAULT_ENV      = "u3";

    public FasitUtils() {
    }

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