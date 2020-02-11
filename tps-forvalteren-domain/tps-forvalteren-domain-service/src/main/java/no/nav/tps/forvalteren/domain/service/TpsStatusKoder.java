package no.nav.tps.forvalteren.domain.service;

public enum TpsStatusKoder {

    OK("00"),
    OK_WITH_NOTE("04"),
    ERROR("08"),
    SYSTEM_ERROR("12");

    private String kode;

    TpsStatusKoder(final String statusKode){
        kode = statusKode;
    }

    public String getKode() {
        return kode;
    }

    public static String getNameByKode(String kode){
        for(TpsStatusKoder k : TpsStatusKoder.values()){
            if(kode.equals(k.kode)) {
                return k.name();
            }
        }
        return "";
    }
}
