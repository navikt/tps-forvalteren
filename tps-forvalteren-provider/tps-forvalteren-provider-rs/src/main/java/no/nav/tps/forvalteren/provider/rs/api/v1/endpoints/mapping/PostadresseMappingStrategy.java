package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;

@Component
public class PostadresseMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsPostadresse.class, Postadresse.class)
                .customize(
                        new CustomMapper<RsPostadresse, Postadresse>() {
                            @Override
                            public void mapAtoB(RsPostadresse rsPostadresse, Postadresse postadresse, MappingContext context) {
                                if (isBlank(postadresse.getPostLinje1()) && isBlank(postadresse.getPostLinje2())) {
                                    postadresse.setPostLinje1(postadresse.getPostLinje3());
                                    postadresse.setPostLinje3(null);
                                } else if (isBlank(postadresse.getPostLinje1())) {
                                    postadresse.setPostLinje1(postadresse.getPostLinje2());
                                    postadresse.setPostLinje2(postadresse.getPostLinje3());
                                    postadresse.setPostLinje3(null);
                                } else if (isBlank(postadresse.getPostLinje2())) {
                                    postadresse.setPostLinje2(postadresse.getPostLinje3());
                                    postadresse.setPostLinje3(null);
                                }
                            }
                        })
                .byDefault()
                .register();
    }
}
