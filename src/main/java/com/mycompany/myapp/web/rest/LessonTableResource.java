package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LessonTableRepository;
import com.mycompany.myapp.service.LessonTableService;
import com.mycompany.myapp.service.dto.LessonTableDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LessonTable}.
 */
@RestController
@RequestMapping("/api")
public class LessonTableResource {

    private final Logger log = LoggerFactory.getLogger(LessonTableResource.class);

    private static final String ENTITY_NAME = "lessonTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonTableService lessonTableService;

    private final LessonTableRepository lessonTableRepository;

    public LessonTableResource(LessonTableService lessonTableService, LessonTableRepository lessonTableRepository) {
        this.lessonTableService = lessonTableService;
        this.lessonTableRepository = lessonTableRepository;
    }

    /**
     * {@code POST  /lesson-tables} : Create a new lessonTable.
     *
     * @param lessonTableDTO the lessonTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonTableDTO, or with status {@code 400 (Bad Request)} if the lessonTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lesson-tables")
    public ResponseEntity<LessonTableDTO> createLessonTable(@RequestBody LessonTableDTO lessonTableDTO) throws URISyntaxException {
        log.debug("REST request to save LessonTable : {}", lessonTableDTO);
        if (lessonTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new lessonTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonTableDTO result = lessonTableService.save(lessonTableDTO);
        return ResponseEntity
            .created(new URI("/api/lesson-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lesson-tables/:id} : Updates an existing lessonTable.
     *
     * @param id the id of the lessonTableDTO to save.
     * @param lessonTableDTO the lessonTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTableDTO,
     * or with status {@code 400 (Bad Request)} if the lessonTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lesson-tables/{id}")
    public ResponseEntity<LessonTableDTO> updateLessonTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LessonTableDTO lessonTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LessonTable : {}, {}", id, lessonTableDTO);
        if (lessonTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LessonTableDTO result = lessonTableService.save(lessonTableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lesson-tables/:id} : Partial updates given fields of an existing lessonTable, field will ignore if it is null
     *
     * @param id the id of the lessonTableDTO to save.
     * @param lessonTableDTO the lessonTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTableDTO,
     * or with status {@code 400 (Bad Request)} if the lessonTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lessonTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lessonTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lesson-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LessonTableDTO> partialUpdateLessonTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LessonTableDTO lessonTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LessonTable partially : {}, {}", id, lessonTableDTO);
        if (lessonTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LessonTableDTO> result = lessonTableService.partialUpdate(lessonTableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-tables} : get all the lessonTables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonTables in body.
     */
    @GetMapping("/lesson-tables")
    public ResponseEntity<List<LessonTableDTO>> getAllLessonTables(Pageable pageable) {
        log.debug("REST request to get a page of LessonTables");
        Page<LessonTableDTO> page = lessonTableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lesson-tables/:id} : get the "id" lessonTable.
     *
     * @param id the id of the lessonTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lesson-tables/{id}")
    public ResponseEntity<LessonTableDTO> getLessonTable(@PathVariable Long id) {
        log.debug("REST request to get LessonTable : {}", id);
        Optional<LessonTableDTO> lessonTableDTO = lessonTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lessonTableDTO);
    }

    /**
     * {@code DELETE  /lesson-tables/:id} : delete the "id" lessonTable.
     *
     * @param id the id of the lessonTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lesson-tables/{id}")
    public ResponseEntity<Void> deleteLessonTable(@PathVariable Long id) {
        log.debug("REST request to delete LessonTable : {}", id);
        lessonTableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
