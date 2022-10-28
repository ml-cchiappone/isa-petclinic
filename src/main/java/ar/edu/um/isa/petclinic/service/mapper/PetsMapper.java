package ar.edu.um.isa.petclinic.service.mapper;

import ar.edu.um.isa.petclinic.domain.Human;
import ar.edu.um.isa.petclinic.domain.Pets;
import ar.edu.um.isa.petclinic.domain.Types;
import ar.edu.um.isa.petclinic.domain.Vet;
import ar.edu.um.isa.petclinic.service.dto.HumanDTO;
import ar.edu.um.isa.petclinic.service.dto.PetsDTO;
import ar.edu.um.isa.petclinic.service.dto.TypesDTO;
import ar.edu.um.isa.petclinic.service.dto.VetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pets} and its DTO {@link PetsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PetsMapper extends EntityMapper<PetsDTO, Pets> {
    @Mapping(target = "type", source = "type", qualifiedByName = "typesId")
    @Mapping(target = "human", source = "human", qualifiedByName = "humanId")
    @Mapping(target = "vet", source = "vet", qualifiedByName = "vetId")
    PetsDTO toDto(Pets s);

    @Named("typesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypesDTO toDtoTypesId(Types types);

    @Named("humanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HumanDTO toDtoHumanId(Human human);

    @Named("vetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VetDTO toDtoVetId(Vet vet);
}
