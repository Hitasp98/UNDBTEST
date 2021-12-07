package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TeacherTable;
import com.mycompany.myapp.repository.TeacherTableRepository;
import com.mycompany.myapp.service.dto.TeacherTableDTO;
import com.mycompany.myapp.service.mapper.TeacherTableMapper;
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
 * Integration tests for the {@link TeacherTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeacherTableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_FIELD = "BBBBBBBBBB";

    private static final String DEFAULT_CERTIFICTION = "AAAAAAAAAA";
    private static final String UPDATED_CERTIFICTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teacher-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeacherTableRepository teacherTableRepository;

    @Autowired
    private TeacherTableMapper teacherTableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherTableMockMvc;

    private TeacherTable teacherTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeacherTable createEntity(EntityManager em) {
        TeacherTable teacherTable = new TeacherTable()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .field(DEFAULT_FIELD)
            .certifiction(DEFAULT_CERTIFICTION)
            .tel(DEFAULT_TEL)
            .address(DEFAULT_ADDRESS);
        return teacherTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeacherTable createUpdatedEntity(EntityManager em) {
        TeacherTable teacherTable = new TeacherTable()
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .field(UPDATED_FIELD)
            .certifiction(UPDATED_CERTIFICTION)
            .tel(UPDATED_TEL)
            .address(UPDATED_ADDRESS);
        return teacherTable;
    }

    @BeforeEach
    public void initTest() {
        teacherTable = createEntity(em);
    }

    @Test
    @Transactional
    void createTeacherTable() throws Exception {
        int databaseSizeBeforeCreate = teacherTableRepository.findAll().size();
        // Create the TeacherTable
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);
        restTeacherTableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeCreate + 1);
        TeacherTable testTeacherTable = teacherTableList.get(teacherTableList.size() - 1);
        assertThat(testTeacherTable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeacherTable.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTeacherTable.getField()).isEqualTo(DEFAULT_FIELD);
        assertThat(testTeacherTable.getCertifiction()).isEqualTo(DEFAULT_CERTIFICTION);
        assertThat(testTeacherTable.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testTeacherTable.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createTeacherTableWithExistingId() throws Exception {
        // Create the TeacherTable with an existing ID
        teacherTable.setId(1L);
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);

        int databaseSizeBeforeCreate = teacherTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherTableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTeacherTables() throws Exception {
        // Initialize the database
        teacherTableRepository.saveAndFlush(teacherTable);

        // Get all the teacherTableList
        restTeacherTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacherTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].field").value(hasItem(DEFAULT_FIELD)))
            .andExpect(jsonPath("$.[*].certifiction").value(hasItem(DEFAULT_CERTIFICTION)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getTeacherTable() throws Exception {
        // Initialize the database
        teacherTableRepository.saveAndFlush(teacherTable);

        // Get the teacherTable
        restTeacherTableMockMvc
            .perform(get(ENTITY_API_URL_ID, teacherTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacherTable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.field").value(DEFAULT_FIELD))
            .andExpect(jsonPath("$.certifiction").value(DEFAULT_CERTIFICTION))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingTeacherTable() throws Exception {
        // Get the teacherTable
        restTeacherTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTeacherTable() throws Exception {
        // Initialize the database
        teacherTableRepository.saveAndFlush(teacherTable);

        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();

        // Update the teacherTable
        TeacherTable updatedTeacherTable = teacherTableRepository.findById(teacherTable.getId()).get();
        // Disconnect from session so that the updates on updatedTeacherTable are not directly saved in db
        em.detach(updatedTeacherTable);
        updatedTeacherTable
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .field(UPDATED_FIELD)
            .certifiction(UPDATED_CERTIFICTION)
            .tel(UPDATED_TEL)
            .address(UPDATED_ADDRESS);
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(updatedTeacherTable);

        restTeacherTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teacherTableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
        TeacherTable testTeacherTable = teacherTableList.get(teacherTableList.size() - 1);
        assertThat(testTeacherTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacherTable.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTeacherTable.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testTeacherTable.getCertifiction()).isEqualTo(UPDATED_CERTIFICTION);
        assertThat(testTeacherTable.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testTeacherTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingTeacherTable() throws Exception {
        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();
        teacherTable.setId(count.incrementAndGet());

        // Create the TeacherTable
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teacherTableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeacherTable() throws Exception {
        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();
        teacherTable.setId(count.incrementAndGet());

        // Create the TeacherTable
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeacherTable() throws Exception {
        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();
        teacherTable.setId(count.incrementAndGet());

        // Create the TeacherTable
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherTableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeacherTableWithPatch() throws Exception {
        // Initialize the database
        teacherTableRepository.saveAndFlush(teacherTable);

        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();

        // Update the teacherTable using partial update
        TeacherTable partialUpdatedTeacherTable = new TeacherTable();
        partialUpdatedTeacherTable.setId(teacherTable.getId());

        partialUpdatedTeacherTable
            .name(UPDATED_NAME)
            .field(UPDATED_FIELD)
            .certifiction(UPDATED_CERTIFICTION)
            .tel(UPDATED_TEL)
            .address(UPDATED_ADDRESS);

        restTeacherTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacherTable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeacherTable))
            )
            .andExpect(status().isOk());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
        TeacherTable testTeacherTable = teacherTableList.get(teacherTableList.size() - 1);
        assertThat(testTeacherTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacherTable.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTeacherTable.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testTeacherTable.getCertifiction()).isEqualTo(UPDATED_CERTIFICTION);
        assertThat(testTeacherTable.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testTeacherTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateTeacherTableWithPatch() throws Exception {
        // Initialize the database
        teacherTableRepository.saveAndFlush(teacherTable);

        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();

        // Update the teacherTable using partial update
        TeacherTable partialUpdatedTeacherTable = new TeacherTable();
        partialUpdatedTeacherTable.setId(teacherTable.getId());

        partialUpdatedTeacherTable
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .field(UPDATED_FIELD)
            .certifiction(UPDATED_CERTIFICTION)
            .tel(UPDATED_TEL)
            .address(UPDATED_ADDRESS);

        restTeacherTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacherTable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeacherTable))
            )
            .andExpect(status().isOk());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
        TeacherTable testTeacherTable = teacherTableList.get(teacherTableList.size() - 1);
        assertThat(testTeacherTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacherTable.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTeacherTable.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testTeacherTable.getCertifiction()).isEqualTo(UPDATED_CERTIFICTION);
        assertThat(testTeacherTable.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testTeacherTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingTeacherTable() throws Exception {
        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();
        teacherTable.setId(count.incrementAndGet());

        // Create the TeacherTable
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teacherTableDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeacherTable() throws Exception {
        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();
        teacherTable.setId(count.incrementAndGet());

        // Create the TeacherTable
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeacherTable() throws Exception {
        int databaseSizeBeforeUpdate = teacherTableRepository.findAll().size();
        teacherTable.setId(count.incrementAndGet());

        // Create the TeacherTable
        TeacherTableDTO teacherTableDTO = teacherTableMapper.toDto(teacherTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherTableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teacherTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeacherTable in the database
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeacherTable() throws Exception {
        // Initialize the database
        teacherTableRepository.saveAndFlush(teacherTable);

        int databaseSizeBeforeDelete = teacherTableRepository.findAll().size();

        // Delete the teacherTable
        restTeacherTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, teacherTable.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeacherTable> teacherTableList = teacherTableRepository.findAll();
        assertThat(teacherTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
