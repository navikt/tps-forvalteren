package no.nav.tps.forvalteren.service.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * System user roles
 */
public enum UserRole implements GrantedAuthority {

    ROLE_ACCESS("0000-GA-TPSF-LES", "0000-GA-TPSF-SKRIV"),
    ROLE_SKRIV_TPSF("0000-GA-TPSF-SKRIV"),
    ROLE_LES_TPSF("0000-GA-TPSF-LES"),
    ROLE_DISKRESJONESKODE_6_READ("0000-GA-GOSYS_KODE6"),
    ROLE_DISKRESJONESKODE_7_READ("0000-GA-GOSYS_KODE7"),
    ROLE_EGEN_ANSATT_READ("0000-GA-PIP_EGENANSATT");

    private final ArrayList<String> AD_STRINGS = new ArrayList<>();

    UserRole(String... name){
        AD_STRINGS.addAll(Arrays.asList(name));
    }

    @Override
    public String getAuthority() {
        return name();
    }

    public List<String> getADRolesForEnum(){
        return AD_STRINGS;
    }
}
