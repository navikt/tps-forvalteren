package no.nav.tps.forvalteren.service.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * System user roles
 *
 */
public enum UserRole implements GrantedAuthority {

    ROLE_ACCESS("0000-GA-NORG_Skriv"),
    ROLE_DISKRESJONESKODE_6_READ("0000-GA-GOSYS_KODE6"),
    ROLE_DISKRESJONESKODE_7_READ("0000-GA-GOSYS_KODE7"),
    ROLE_EGEN_ANSATT_READ("0000-GA-PIP_EGENANSATT");

    private final String name;

    UserRole(final String name){
        this.name = name;
    }

    public static UserRole getEnumFromName(String roleName){
        for(UserRole roleEnum : values()){
            if(roleEnum.toString().equals(roleName)){
                return roleEnum;
            }
        }
        return null;
    }

    @Override
    public String getAuthority() {
        return name();
    }

    @Override
    public String toString(){
        return name;
    }
}
