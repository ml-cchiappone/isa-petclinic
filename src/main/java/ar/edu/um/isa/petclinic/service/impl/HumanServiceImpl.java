package ar.edu.um.isa.petclinic.service.impl;

import ar.edu.um.isa.petclinic.domain.Human;
import ar.edu.um.isa.petclinic.repository.HumanRepository;
import ar.edu.um.isa.petclinic.service.HumanService;
import ar.edu.um.isa.petclinic.service.dto.HumanDTO;
import ar.edu.um.isa.petclinic.service.mapper.HumanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Human}.
 */
@Service
@Transactional
public class HumanServiceImpl implements HumanService {

    private final Logger log = LoggerFactory.getLogger(HumanServiceImpl.class);

    private final HumanRepository humanRepository;

    private final HumanMapper humanMapper;

    public HumanServiceImpl(HumanRepository humanRepository, HumanMapper humanMapper) {
        this.humanRepository = humanRepository;
        this.humanMapper = humanMapper;
    }

    @Override
    public HumanDTO save(HumanDTO humanDTO) {
        log.debug("Request to save Human : {}", humanDTO);
        Human human = humanMapper.toEntity(humanDTO);
        human = humanRepository.save(human);
        return humanMapper.toDto(human);
    }

    @Override
    public HumanDTO update(HumanDTO humanDTO) {
        log.debug("Request to save Human : {}", humanDTO);
        Human human = humanMapper.toEntity(humanDTO);
        human = humanRepository.save(human);
        return humanMapper.toDto(human);
    }

    @Override
    public Optional<HumanDTO> partialUpdate(HumanDTO humanDTO) {
        log.debug("Request to partially update Human : {}", humanDTO);

        return humanRepository
            .findById(humanDTO.getId())
            .map(existingHuman -> {
                humanMapper.partialUpdate(existingHuman, humanDTO);

                return existingHuman;
            })
            .map(humanRepository::save)
            .map(humanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HumanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Humans");
        return humanRepository.findAll(pageable).map(humanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HumanDTO> findOne(Long id) {
        log.debug("Request to get Human : {}", id);
        return humanRepository.findById(id).map(humanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Human : {}", id);
        humanRepository.deleteById(id);
    }
}
