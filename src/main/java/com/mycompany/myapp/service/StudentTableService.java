package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.StudentTable;
import com.mycompany.myapp.repository.StudentTableRepository;
import com.mycompany.myapp.service.dto.StudentTableDTO;
import com.mycompany.myapp.service.mapper.StudentTableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentTable}.
 */
@Service
@Transactional
public class StudentTableService {

    private final Logger log = LoggerFactory.getLogger(StudentTableService.class);

    private final StudentTableRepository studentTableRepository;

    private final StudentTableMapper studentTableMapper;

    public StudentTableService(StudentTableRepository studentTableRepository, StudentTableMapper studentTableMapper) {
        this.studentTableRepository = studentTableRepository;
        this.studentTableMapper = studentTableMapper;
    }

    /**
     * Save a studentTable.
     *
     * @param studentTableDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentTableDTO save(StudentTableDTO studentTableDTO) {
        log.debug("Request to save StudentTable : {}", studentTableDTO);
        StudentTable studentTable = studentTableMapper.toEntity(studentTableDTO);
        studentTable = studentTableRepository.save(studentTable);
        return studentTableMapper.toDto(studentTable);
    }

    /**
     * Partially update a studentTable.
     *
     * @param studentTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentTableDTO> partialUpdate(StudentTableDTO studentTableDTO) {
        log.debug("Request to partially update StudentTable : {}", studentTableDTO);

        return studentTableRepository
            .findById(studentTableDTO.getId())
            .map(existingStudentTable -> {
                studentTableMapper.partialUpdate(existingStudentTable, studentTableDTO);

                return existingStudentTable;
            })
            .map(studentTableRepository::save)
            .map(studentTableMapper::toDto);
    }

    /**
     * Get all the studentTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentTables");
        return studentTableRepository.findAll(pageable).map(studentTableMapper::toDto);
    }

    /**
     * Get all the studentTables with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<StudentTableDTO> findAllWithEagerRelationships(Pageable pageable) {
        return studentTableRepository.findAllWithEagerRelationships(pageable).map(studentTableMapper::toDto);
    }

    /**
     * Get one studentTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentTableDTO> findOne(Long id) {
        log.debug("Request to get StudentTable : {}", id);
        return studentTableRepository.findOneWithEagerRelationships(id).map(studentTableMapper::toDto);
    }

    /**
     * Delete the studentTable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentTable : {}", id);
        studentTableRepository.deleteById(id);
    }
}
