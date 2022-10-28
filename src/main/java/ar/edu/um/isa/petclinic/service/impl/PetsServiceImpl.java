package ar.edu.um.isa.petclinic.service.impl;

import ar.edu.um.isa.petclinic.domain.Pets;
import ar.edu.um.isa.petclinic.repository.PetsRepository;
import ar.edu.um.isa.petclinic.service.PetsService;
import ar.edu.um.isa.petclinic.service.dto.PetsDTO;
import ar.edu.um.isa.petclinic.service.mapper.PetsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pets}.
 */
@Service
@Transactional
public class PetsServiceImpl implements PetsService {

    private final Logger log = LoggerFactory.getLogger(PetsServiceImpl.class);

    private final PetsRepository petsRepository;

    private final PetsMapper petsMapper;

    public PetsServiceImpl(PetsRepository petsRepository, PetsMapper petsMapper) {
        this.petsRepository = petsRepository;
        this.petsMapper = petsMapper;
    }

    @Override
    public PetsDTO save(PetsDTO petsDTO) {
        log.debug("Request to save Pets : {}", petsDTO);
        Pets pets = petsMapper.toEntity(petsDTO);
        pets = petsRepository.save(pets);
        return petsMapper.toDto(pets);
    }

    @Override
    public PetsDTO update(PetsDTO petsDTO) {
        log.debug("Request to save Pets : {}", petsDTO);
        Pets pets = petsMapper.toEntity(petsDTO);
        pets = petsRepository.save(pets);
        return petsMapper.toDto(pets);
    }

    @Override
    public Optional<PetsDTO> partialUpdate(PetsDTO petsDTO) {
        log.debug("Request to partially update Pets : {}", petsDTO);

        return petsRepository
            .findById(petsDTO.getId())
            .map(existingPets -> {
                petsMapper.partialUpdate(existingPets, petsDTO);

                return existingPets;
            })
            .map(petsRepository::save)
            .map(petsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pets");
        return petsRepository.findAll(pageable).map(petsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetsDTO> findOne(Long id) {
        log.debug("Request to get Pets : {}", id);
        return petsRepository.findById(id).map(petsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pets : {}", id);
        petsRepository.deleteById(id);
    }
}
