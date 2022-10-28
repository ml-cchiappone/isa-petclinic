package ar.edu.um.isa.petclinic.service.mapper;

import ar.edu.um.isa.petclinic.domain.Types;
import ar.edu.um.isa.petclinic.service.dto.TypesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Types} and its DTO {@link TypesDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypesMapper extends EntityMapper<TypesDTO, Types> {}
