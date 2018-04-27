package no.nav.tps.forvalteren.consumer.rs.environments.url;

import lombok.Getter;

@Getter
public enum FasitUrl {

    APPLICATIONINSTANCES_V2_GET("%s/api/v2/applicationinstances"),
    RESOURCES_V2_GET("%s/api/v2/resources");

    private final String url;

    FasitUrl (String url){
        this.url = url;
    }

    public static String createQueryPatternByParamName(String... paramNames){
        StringBuilder sb = new StringBuilder("?");
        for(int i = 0; i < paramNames.length; i++){
            if(i==0){
                sb.append(paramNames[i]).append("=%s");
            } else {
                sb.append("&").append(paramNames[i]).append("=%s");
            }
        }
        return sb.toString();
    }
}

