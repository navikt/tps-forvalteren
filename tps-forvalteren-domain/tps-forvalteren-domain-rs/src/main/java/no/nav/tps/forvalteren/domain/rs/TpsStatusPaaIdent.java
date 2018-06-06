package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TpsStatusPaaIdent {
	private String ident;
	private List<String> env;
	
	public TpsStatusPaaIdent() {
		env = new ArrayList<>();
	}
	
	public void add(String environment) {
		env.add(environment);
	}
}
