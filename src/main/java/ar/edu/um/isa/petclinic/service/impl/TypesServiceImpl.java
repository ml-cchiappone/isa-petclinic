package ar.edu.um.isa.petclinic.service.impl;

import ar.edu.um.isa.petclinic.domain.Types;
import ar.edu.um.isa.petclinic.repository.TypesRepository;
import ar.edu.um.isa.petclinic.service.TypesService;
import ar.edu.um.isa.petclinic.service.dto.TypesDTO;
import ar.edu.um.isa.petclinic.service.mapper.TypesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Types}.
 */
@Service
@Transactional
public class TypesServiceImpl implements TypesService {

    private final Logger log = LoggerFactory.getLogger(TypesServiceImpl.class);

    private final TypesRepository typesRepository;

    private final TypesMapper typesMapper;

    public TypesServiceImpl(TypesRepository typesRepository, TypesMapper typesMapper) {
        this.typesRepository = typesRepository;
        this.typesMapper = typesMapper;
    }

    @Override
    public TypesDTO save(TypesDTO typesDTO) {
        log.debug("Request to save Types : {}", typesDTO);
        Types types = typesMapper.toEntity(typesDTO);
        types = typesRepository.save(types);
        return typesMapper.toDto(types);
    }

    @Override
    public TypesDTO update(TypesDTO typesDTO) {
        log.debug("Request to save Types : {}", typesDTO);
        Types types = typesMapper.toEntity(typesDTO);
        types = typesRepository.save(types);
        return typesMapper.toDto(types);
    }

    @Override
    public Optional<TypesDTO> partialUpdate(TypesDTO typesDTO) {
        log.debug("Request to partially update Types : {}", typesDTO);

        return typesRepository
            .findById(typesDTO.getId())
            .map(existingTypes -> {
                typesMapper.partialUpdate(existingTypes, typesDTO);

                return existingTypes;
            })
            .map(typesRepository::save)
            .map(typesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Types");
        return typesRepository.findAll(pageable).map(typesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypesDTO> findOne(Long id) {
        log.debug("Request to get Types : {}", id);
        return typesRepository.findById(id).map(typesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Types : {}", id);
        typesRepository.deleteById(id);
    }
}
