package no.nav.tps.forvalteren.consumer.rs.identpool.dao;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentpoolAcquireRequest {

    private List<String> acquireIdents;
}
