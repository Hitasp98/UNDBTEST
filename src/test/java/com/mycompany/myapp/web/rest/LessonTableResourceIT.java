package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonTable;
import com.mycompany.myapp.repository.LessonTableRepository;
import com.mycompany.myapp.service.dto.LessonTableDTO;
import com.mycompany.myapp.service.mapper.LessonTableMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LessonTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LessonTableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lesson-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LessonTableRepository lessonTableRepository;

    @Autowired
    private LessonTableMapper lessonTableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonTableMockMvc;

    private LessonTable lessonTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTable createEntity(EntityManager em) {
        LessonTable lessonTable = new LessonTable().name(DEFAULT_NAME).unit(DEFAULT_UNIT).address(DEFAULT_ADDRESS).tel(DEFAULT_TEL);
        return lessonTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTable createUpdatedEntity(EntityManager em) {
        LessonTable lessonTable = new LessonTable().name(UPDATED_NAME).unit(UPDATED_UNIT).address(UPDATED_ADDRESS).tel(UPDATED_TEL);
        return lessonTable;
    }

    @BeforeEach
    public void initTest() {
        lessonTable = createEntity(em);
    }

    @Test
    @Transactional
    void createLessonTable() throws Exception {
        int databaseSizeBeforeCreate = lessonTableRepository.findAll().size();
        // Create the LessonTable
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);
        restLessonTableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeCreate + 1);
        LessonTable testLessonTable = lessonTableList.get(lessonTableList.size() - 1);
        assertThat(testLessonTable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLessonTable.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testLessonTable.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLessonTable.getTel()).isEqualTo(DEFAULT_TEL);
    }

    @Test
    @Transactional
    void createLessonTableWithExistingId() throws Exception {
        // Create the LessonTable with an existing ID
        lessonTable.setId(1L);
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);

        int databaseSizeBeforeCreate = lessonTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonTableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLessonTables() throws Exception {
        // Initialize the database
        lessonTableRepository.saveAndFlush(lessonTable);

        // Get all the lessonTableList
        restLessonTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)));
    }

    @Test
    @Transactional
    void getLessonTable() throws Exception {
        // Initialize the database
        lessonTableRepository.saveAndFlush(lessonTable);

        // Get the lessonTable
        restLessonTableMockMvc
            .perform(get(ENTITY_API_URL_ID, lessonTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lessonTable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL));
    }

    @Test
    @Transactional
    void getNonExistingLessonTable() throws Exception {
        // Get the lessonTable
        restLessonTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLessonTable() throws Exception {
        // Initialize the database
        lessonTableRepository.saveAndFlush(lessonTable);

        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();

        // Update the lessonTable
        LessonTable updatedLessonTable = lessonTableRepository.findById(lessonTable.getId()).get();
        // Disconnect from session so that the updates on updatedLessonTable are not directly saved in db
        em.detach(updatedLessonTable);
        updatedLessonTable.name(UPDATED_NAME).unit(UPDATED_UNIT).address(UPDATED_ADDRESS).tel(UPDATED_TEL);
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(updatedLessonTable);

        restLessonTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonTableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
        LessonTable testLessonTable = lessonTableList.get(lessonTableList.size() - 1);
        assertThat(testLessonTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLessonTable.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testLessonTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLessonTable.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void putNonExistingLessonTable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();
        lessonTable.setId(count.incrementAndGet());

        // Create the LessonTable
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonTableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLessonTable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();
        lessonTable.setId(count.incrementAndGet());

        // Create the LessonTable
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLessonTable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();
        lessonTable.setId(count.incrementAndGet());

        // Create the LessonTable
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonTableWithPatch() throws Exception {
        // Initialize the database
        lessonTableRepository.saveAndFlush(lessonTable);

        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();

        // Update the lessonTable using partial update
        LessonTable partialUpdatedLessonTable = new LessonTable();
        partialUpdatedLessonTable.setId(lessonTable.getId());

        partialUpdatedLessonTable.name(UPDATED_NAME);

        restLessonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTable))
            )
            .andExpect(status().isOk());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
        LessonTable testLessonTable = lessonTableList.get(lessonTableList.size() - 1);
        assertThat(testLessonTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLessonTable.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testLessonTable.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLessonTable.getTel()).isEqualTo(DEFAULT_TEL);
    }

    @Test
    @Transactional
    void fullUpdateLessonTableWithPatch() throws Exception {
        // Initialize the database
        lessonTableRepository.saveAndFlush(lessonTable);

        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();

        // Update the lessonTable using partial update
        LessonTable partialUpdatedLessonTable = new LessonTable();
        partialUpdatedLessonTable.setId(lessonTable.getId());

        partialUpdatedLessonTable.name(UPDATED_NAME).unit(UPDATED_UNIT).address(UPDATED_ADDRESS).tel(UPDATED_TEL);

        restLessonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTable))
            )
            .andExpect(status().isOk());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
        LessonTable testLessonTable = lessonTableList.get(lessonTableList.size() - 1);
        assertThat(testLessonTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLessonTable.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testLessonTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLessonTable.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void patchNonExistingLessonTable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();
        lessonTable.setId(count.incrementAndGet());

        // Create the LessonTable
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonTableDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLessonTable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();
        lessonTable.setId(count.incrementAndGet());

        // Create the LessonTable
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLessonTable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTableRepository.findAll().size();
        lessonTable.setId(count.incrementAndGet());

        // Create the LessonTable
        LessonTableDTO lessonTableDTO = lessonTableMapper.toDto(lessonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTable in the database
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLessonTable() throws Exception {
        // Initialize the database
        lessonTableRepository.saveAndFlush(lessonTable);

        int databaseSizeBeforeDelete = lessonTableRepository.findAll().size();

        // Delete the lessonTable
        restLessonTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, lessonTable.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LessonTable> lessonTableList = lessonTableRepository.findAll();
        assertThat(lessonTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
