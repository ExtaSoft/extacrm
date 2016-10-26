package ru.extas.model.info;

import ru.extas.model.common.FileContainer;

import javax.persistence.*;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Информационный материал
 *
 * Created by valery on 24.10.16.
 */
@Entity
@Table(name = "INFO_FILE")
public class InfoFile extends FileContainer {

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "INFO_FILE_BRAND",
            joinColumns = {@JoinColumn(name = "FILE_ID", referencedColumnName = "ID")})
    private Set<String> permitBrands = newHashSet();

    public Set<String> getPermitBrands() {
        return permitBrands;
    }

    public void setPermitBrands(Set<String> permitBrands) {
        this.permitBrands = permitBrands;
    }
}
