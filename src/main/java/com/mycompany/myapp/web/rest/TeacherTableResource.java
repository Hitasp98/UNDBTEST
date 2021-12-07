package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TeacherTableRepository;
import com.mycompany.myapp.service.TeacherTableService;
import com.mycompany.myapp.service.dto.TeacherTableDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TeacherTable}.
 */
@RestController
@RequestMapping("/api")
public class TeacherTableResource {

    private final Logger log = LoggerFactory.getLogger(TeacherTableResource.class);

    private static final String ENTITY_NAME = "teacherTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeacherTableService teacherTableService;

    private final TeacherTableRepository teacherTableRepository;

    public TeacherTableResource(TeacherTableService teacherTableService, TeacherTableRepository teacherTableRepository) {
        this.teacherTableService = teacherTableService;
        this.teacherTableRepository = teacherTableRepository;
    }

    /**
     * {@code POST  /teacher-tables} : Create a new teacherTable.
     *
     * @param teacherTableDTO the teacherTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teacherTableDTO, or with status {@code 400 (Bad Request)} if the teacherTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teacher-tables")
    public ResponseEntity<TeacherTableDTO> createTeacherTable(@RequestBody TeacherTableDTO teacherTableDTO) throws URISyntaxException {
        log.debug("REST request to save TeacherTable : {}", teacherTableDTO);
        if (teacherTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new teacherTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeacherTableDTO result = teacherTableService.save(teacherTableDTO);
        return ResponseEntity
            .created(new URI("/api/teacher-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /teacher-tables/:id} : Updates an existing teacherTable.
     *
     * @param id the id of the teacherTableDTO to save.
     * @param teacherTableDTO the teacherTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacherTableDTO,
     * or with status {@code 400 (Bad Request)} if the teacherTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teacherTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teacher-tables/{id}")
    public ResponseEntity<TeacherTableDTO> updateTeacherTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeacherTableDTO teacherTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TeacherTable : {}, {}", id, teacherTableDTO);
        if (teacherTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacherTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TeacherTableDTO result = teacherTableService.save(teacherTableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, teacherTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /teacher-tables/:id} : Partial updates given fields of an existing teacherTable, field will ignore if it is null
     *
     * @param id the id of the teacherTableDTO to save.
     * @param teacherTableDTO the teacherTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacherTableDTO,
     * or with status {@code 400 (Bad Request)} if the teacherTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teacherTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teacherTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/teacher-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeacherTableDTO> partialUpdateTeacherTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeacherTableDTO teacherTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TeacherTable partially : {}, {}", id, teacherTableDTO);
        if (teacherTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacherTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeacherTableDTO> result = teacherTableService.partialUpdate(teacherTableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, teacherTableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /teacher-tables} : get all the teacherTables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teacherTables in body.
     */
    @GetMapping("/teacher-tables")
    public ResponseEntity<List<TeacherTableDTO>> getAllTeacherTables(Pageable pageable) {
        log.debug("REST request to get a page of TeacherTables");
        Page<TeacherTableDTO> page = teacherTableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teacher-tables/:id} : get the "id" teacherTable.
     *
     * @param id the id of the teacherTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teacherTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teacher-tables/{id}")
    public ResponseEntity<TeacherTableDTO> getTeacherTable(@PathVariable Long id) {
        log.debug("REST request to get TeacherTable : {}", id);
        Optional<TeacherTableDTO> teacherTableDTO = teacherTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teacherTableDTO);
    }

    /**
     * {@code DELETE  /teacher-tables/:id} : delete the "id" teacherTable.
     *
     * @param id the id of the teacherTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teacher-tables/{id}")
    public ResponseEntity<Void> deleteTeacherTable(@PathVariable Long id) {
        log.debug("REST request to delete TeacherTable : {}", id);
        teacherTableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
