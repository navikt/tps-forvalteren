package no.nav.tps.forvalteren.provider.rs.security.authentication;

import org.springframework.security.core.AuthenticationException;

public final class ActiveDirectoryAuthenticationException extends AuthenticationException {
	private final String dataCode;

	ActiveDirectoryAuthenticationException(String dataCode, String message,
			Throwable cause) {
		super(message, cause);
		this.dataCode = dataCode;
	}

	public String getDataCode() {
		return dataCode;
	}
}
