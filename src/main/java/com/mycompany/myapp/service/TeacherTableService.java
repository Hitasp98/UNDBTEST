package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TeacherTable;
import com.mycompany.myapp.repository.TeacherTableRepository;
import com.mycompany.myapp.service.dto.TeacherTableDTO;
import com.mycompany.myapp.service.mapper.TeacherTableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TeacherTable}.
 */
@Service
@Transactional
public class TeacherTableService {

    private final Logger log = LoggerFactory.getLogger(TeacherTableService.class);

    private final TeacherTableRepository teacherTableRepository;

    private final TeacherTableMapper teacherTableMapper;

    public TeacherTableService(TeacherTableRepository teacherTableRepository, TeacherTableMapper teacherTableMapper) {
        this.teacherTableRepository = teacherTableRepository;
        this.teacherTableMapper = teacherTableMapper;
    }

    /**
     * Save a teacherTable.
     *
     * @param teacherTableDTO the entity to save.
     * @return the persisted entity.
     */
    public TeacherTableDTO save(TeacherTableDTO teacherTableDTO) {
        log.debug("Request to save TeacherTable : {}", teacherTableDTO);
        TeacherTable teacherTable = teacherTableMapper.toEntity(teacherTableDTO);
        teacherTable = teacherTableRepository.save(teacherTable);
        return teacherTableMapper.toDto(teacherTable);
    }

    /**
     * Partially update a teacherTable.
     *
     * @param teacherTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TeacherTableDTO> partialUpdate(TeacherTableDTO teacherTableDTO) {
        log.debug("Request to partially update TeacherTable : {}", teacherTableDTO);

        return teacherTableRepository
            .findById(teacherTableDTO.getId())
            .map(existingTeacherTable -> {
                teacherTableMapper.partialUpdate(existingTeacherTable, teacherTableDTO);

                return existingTeacherTable;
            })
            .map(teacherTableRepository::save)
            .map(teacherTableMapper::toDto);
    }

    /**
     * Get all the teacherTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TeacherTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TeacherTables");
        return teacherTableRepository.findAll(pageable).map(teacherTableMapper::toDto);
    }

    /**
     * Get one teacherTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeacherTableDTO> findOne(Long id) {
        log.debug("Request to get TeacherTable : {}", id);
        return teacherTableRepository.findById(id).map(teacherTableMapper::toDto);
    }

    /**
     * Delete the teacherTable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TeacherTable : {}", id);
        teacherTableRepository.deleteById(id);
    }
}
