package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonPaginert {

    private Integer antallPages;
    private Integer pageNo;
    private Integer pageSize;
    private Long antallElementer;
    private List<RsPerson> contents;

    public List<RsPerson> getContents() {
        if (isNull(contents)) {
            contents = new ArrayList<>();
        }
        return contents;
    }
}