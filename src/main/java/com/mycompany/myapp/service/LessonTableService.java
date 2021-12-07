package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LessonTable;
import com.mycompany.myapp.repository.LessonTableRepository;
import com.mycompany.myapp.service.dto.LessonTableDTO;
import com.mycompany.myapp.service.mapper.LessonTableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LessonTable}.
 */
@Service
@Transactional
public class LessonTableService {

    private final Logger log = LoggerFactory.getLogger(LessonTableService.class);

    private final LessonTableRepository lessonTableRepository;

    private final LessonTableMapper lessonTableMapper;

    public LessonTableService(LessonTableRepository lessonTableRepository, LessonTableMapper lessonTableMapper) {
        this.lessonTableRepository = lessonTableRepository;
        this.lessonTableMapper = lessonTableMapper;
    }

    /**
     * Save a lessonTable.
     *
     * @param lessonTableDTO the entity to save.
     * @return the persisted entity.
     */
    public LessonTableDTO save(LessonTableDTO lessonTableDTO) {
        log.debug("Request to save LessonTable : {}", lessonTableDTO);
        LessonTable lessonTable = lessonTableMapper.toEntity(lessonTableDTO);
        lessonTable = lessonTableRepository.save(lessonTable);
        return lessonTableMapper.toDto(lessonTable);
    }

    /**
     * Partially update a lessonTable.
     *
     * @param lessonTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LessonTableDTO> partialUpdate(LessonTableDTO lessonTableDTO) {
        log.debug("Request to partially update LessonTable : {}", lessonTableDTO);

        return lessonTableRepository
            .findById(lessonTableDTO.getId())
            .map(existingLessonTable -> {
                lessonTableMapper.partialUpdate(existingLessonTable, lessonTableDTO);

                return existingLessonTable;
            })
            .map(lessonTableRepository::save)
            .map(lessonTableMapper::toDto);
    }

    /**
     * Get all the lessonTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LessonTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LessonTables");
        return lessonTableRepository.findAll(pageable).map(lessonTableMapper::toDto);
    }

    /**
     * Get one lessonTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LessonTableDTO> findOne(Long id) {
        log.debug("Request to get LessonTable : {}", id);
        return lessonTableRepository.findById(id).map(lessonTableMapper::toDto);
    }

    /**
     * Delete the lessonTable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LessonTable : {}", id);
        lessonTableRepository.deleteById(id);
    }
}
