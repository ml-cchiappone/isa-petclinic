package ar.edu.um.isa.petclinic.service.mapper;

import ar.edu.um.isa.petclinic.domain.Pets;
import ar.edu.um.isa.petclinic.domain.Visits;
import ar.edu.um.isa.petclinic.service.dto.PetsDTO;
import ar.edu.um.isa.petclinic.service.dto.VisitsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Visits} and its DTO {@link VisitsDTO}.
 */
@Mapper(componentModel = "spring")
public interface VisitsMapper extends EntityMapper<VisitsDTO, Visits> {
    @Mapping(target = "pet", source = "pet", qualifiedByName = "petsId")
    VisitsDTO toDto(Visits s);

    @Named("petsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PetsDTO toDtoPetsId(Pets pets);
}
