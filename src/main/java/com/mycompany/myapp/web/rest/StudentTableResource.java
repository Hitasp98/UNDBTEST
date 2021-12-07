package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.StudentTableRepository;
import com.mycompany.myapp.service.StudentTableService;
import com.mycompany.myapp.service.dto.StudentTableDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.StudentTable}.
 */
@RestController
@RequestMapping("/api")
public class StudentTableResource {

    private final Logger log = LoggerFactory.getLogger(StudentTableResource.class);

    private static final String ENTITY_NAME = "studentTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentTableService studentTableService;

    private final StudentTableRepository studentTableRepository;

    public StudentTableResource(StudentTableService studentTableService, StudentTableRepository studentTableRepository) {
        this.studentTableService = studentTableService;
        this.studentTableRepository = studentTableRepository;
    }

    /**
     * {@code POST  /student-tables} : Create a new studentTable.
     *
     * @param studentTableDTO the studentTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentTableDTO, or with status {@code 400 (Bad Request)} if the studentTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-tables")
    public ResponseEntity<StudentTableDTO> createStudentTable(@RequestBody StudentTableDTO studentTableDTO) throws URISyntaxException {
        log.debug("REST request to save StudentTable : {}", studentTableDTO);
        if (studentTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentTableDTO result = studentTableService.save(studentTableDTO);
        return ResponseEntity
            .created(new URI("/api/student-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-tables/:id} : Updates an existing studentTable.
     *
     * @param id the id of the studentTableDTO to save.
     * @param studentTableDTO the studentTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentTableDTO,
     * or with status {@code 400 (Bad Request)} if the studentTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-tables/{id}")
    public ResponseEntity<StudentTableDTO> updateStudentTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentTableDTO studentTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StudentTable : {}, {}", id, studentTableDTO);
        if (studentTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentTableDTO result = studentTableService.save(studentTableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-tables/:id} : Partial updates given fields of an existing studentTable, field will ignore if it is null
     *
     * @param id the id of the studentTableDTO to save.
     * @param studentTableDTO the studentTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentTableDTO,
     * or with status {@code 400 (Bad Request)} if the studentTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/student-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentTableDTO> partialUpdateStudentTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentTableDTO studentTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentTable partially : {}, {}", id, studentTableDTO);
        if (studentTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentTableDTO> result = studentTableService.partialUpdate(studentTableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentTableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-tables} : get all the studentTables.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentTables in body.
     */
    @GetMapping("/student-tables")
    public ResponseEntity<List<StudentTableDTO>> getAllStudentTables(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of StudentTables");
        Page<StudentTableDTO> page;
        if (eagerload) {
            page = studentTableService.findAllWithEagerRelationships(pageable);
        } else {
            page = studentTableService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-tables/:id} : get the "id" studentTable.
     *
     * @param id the id of the studentTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-tables/{id}")
    public ResponseEntity<StudentTableDTO> getStudentTable(@PathVariable Long id) {
        log.debug("REST request to get StudentTable : {}", id);
        Optional<StudentTableDTO> studentTableDTO = studentTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentTableDTO);
    }

    /**
     * {@code DELETE  /student-tables/:id} : delete the "id" studentTable.
     *
     * @param id the id of the studentTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-tables/{id}")
    public ResponseEntity<Void> deleteStudentTable(@PathVariable Long id) {
        log.debug("REST request to delete StudentTable : {}", id);
        studentTableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
