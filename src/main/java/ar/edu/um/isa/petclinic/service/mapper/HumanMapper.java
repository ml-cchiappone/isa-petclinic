package ar.edu.um.isa.petclinic.service.mapper;

import ar.edu.um.isa.petclinic.domain.Human;
import ar.edu.um.isa.petclinic.service.dto.HumanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Human} and its DTO {@link HumanDTO}.
 */
@Mapper(componentModel = "spring")
public interface HumanMapper extends EntityMapper<HumanDTO, Human> {}
