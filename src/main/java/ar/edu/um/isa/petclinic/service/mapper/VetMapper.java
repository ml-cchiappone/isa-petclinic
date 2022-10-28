package ar.edu.um.isa.petclinic.service.mapper;

import ar.edu.um.isa.petclinic.domain.Vet;
import ar.edu.um.isa.petclinic.service.dto.VetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vet} and its DTO {@link VetDTO}.
 */
@Mapper(componentModel = "spring")
public interface VetMapper extends EntityMapper<VetDTO, Vet> {}
