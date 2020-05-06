package no.nav.tps.forvalteren.domain.rs.skd;

public enum KjoennType {

    K, M, U;

    public static KjoennType getMotsattKjoenn (KjoennType kjoenn) {

        if (M == kjoenn) {
            return K;
        } else if (K == kjoenn) {
            return M;
        } else {
            return kjoenn;
        }
    }
}