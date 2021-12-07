package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StudentTable;
import com.mycompany.myapp.repository.StudentTableRepository;
import com.mycompany.myapp.service.StudentTableService;
import com.mycompany.myapp.service.dto.StudentTableDTO;
import com.mycompany.myapp.service.mapper.StudentTableMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StudentTableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StudentTableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_FIELD = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/student-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentTableRepository studentTableRepository;

    @Mock
    private StudentTableRepository studentTableRepositoryMock;

    @Autowired
    private StudentTableMapper studentTableMapper;

    @Mock
    private StudentTableService studentTableServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentTableMockMvc;

    private StudentTable studentTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentTable createEntity(EntityManager em) {
        StudentTable studentTable = new StudentTable()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .field(DEFAULT_FIELD)
            .address(DEFAULT_ADDRESS)
            .tel(DEFAULT_TEL);
        return studentTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentTable createUpdatedEntity(EntityManager em) {
        StudentTable studentTable = new StudentTable()
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .field(UPDATED_FIELD)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL);
        return studentTable;
    }

    @BeforeEach
    public void initTest() {
        studentTable = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentTable() throws Exception {
        int databaseSizeBeforeCreate = studentTableRepository.findAll().size();
        // Create the StudentTable
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);
        restStudentTableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeCreate + 1);
        StudentTable testStudentTable = studentTableList.get(studentTableList.size() - 1);
        assertThat(testStudentTable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudentTable.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudentTable.getField()).isEqualTo(DEFAULT_FIELD);
        assertThat(testStudentTable.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testStudentTable.getTel()).isEqualTo(DEFAULT_TEL);
    }

    @Test
    @Transactional
    void createStudentTableWithExistingId() throws Exception {
        // Create the StudentTable with an existing ID
        studentTable.setId(1L);
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);

        int databaseSizeBeforeCreate = studentTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentTableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentTables() throws Exception {
        // Initialize the database
        studentTableRepository.saveAndFlush(studentTable);

        // Get all the studentTableList
        restStudentTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].field").value(hasItem(DEFAULT_FIELD)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudentTablesWithEagerRelationshipsIsEnabled() throws Exception {
        when(studentTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(studentTableServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudentTablesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(studentTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(studentTableServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getStudentTable() throws Exception {
        // Initialize the database
        studentTableRepository.saveAndFlush(studentTable);

        // Get the studentTable
        restStudentTableMockMvc
            .perform(get(ENTITY_API_URL_ID, studentTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentTable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.field").value(DEFAULT_FIELD))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL));
    }

    @Test
    @Transactional
    void getNonExistingStudentTable() throws Exception {
        // Get the studentTable
        restStudentTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentTable() throws Exception {
        // Initialize the database
        studentTableRepository.saveAndFlush(studentTable);

        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();

        // Update the studentTable
        StudentTable updatedStudentTable = studentTableRepository.findById(studentTable.getId()).get();
        // Disconnect from session so that the updates on updatedStudentTable are not directly saved in db
        em.detach(updatedStudentTable);
        updatedStudentTable.name(UPDATED_NAME).lastName(UPDATED_LAST_NAME).field(UPDATED_FIELD).address(UPDATED_ADDRESS).tel(UPDATED_TEL);
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(updatedStudentTable);

        restStudentTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentTableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
        StudentTable testStudentTable = studentTableList.get(studentTableList.size() - 1);
        assertThat(testStudentTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudentTable.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudentTable.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testStudentTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStudentTable.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void putNonExistingStudentTable() throws Exception {
        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();
        studentTable.setId(count.incrementAndGet());

        // Create the StudentTable
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentTableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentTable() throws Exception {
        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();
        studentTable.setId(count.incrementAndGet());

        // Create the StudentTable
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentTable() throws Exception {
        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();
        studentTable.setId(count.incrementAndGet());

        // Create the StudentTable
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentTableWithPatch() throws Exception {
        // Initialize the database
        studentTableRepository.saveAndFlush(studentTable);

        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();

        // Update the studentTable using partial update
        StudentTable partialUpdatedStudentTable = new StudentTable();
        partialUpdatedStudentTable.setId(studentTable.getId());

        partialUpdatedStudentTable.name(UPDATED_NAME).lastName(UPDATED_LAST_NAME).address(UPDATED_ADDRESS).tel(UPDATED_TEL);

        restStudentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentTable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentTable))
            )
            .andExpect(status().isOk());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
        StudentTable testStudentTable = studentTableList.get(studentTableList.size() - 1);
        assertThat(testStudentTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudentTable.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudentTable.getField()).isEqualTo(DEFAULT_FIELD);
        assertThat(testStudentTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStudentTable.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void fullUpdateStudentTableWithPatch() throws Exception {
        // Initialize the database
        studentTableRepository.saveAndFlush(studentTable);

        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();

        // Update the studentTable using partial update
        StudentTable partialUpdatedStudentTable = new StudentTable();
        partialUpdatedStudentTable.setId(studentTable.getId());

        partialUpdatedStudentTable
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .field(UPDATED_FIELD)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL);

        restStudentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentTable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentTable))
            )
            .andExpect(status().isOk());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
        StudentTable testStudentTable = studentTableList.get(studentTableList.size() - 1);
        assertThat(testStudentTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudentTable.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudentTable.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testStudentTable.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStudentTable.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void patchNonExistingStudentTable() throws Exception {
        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();
        studentTable.setId(count.incrementAndGet());

        // Create the StudentTable
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentTableDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentTable() throws Exception {
        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();
        studentTable.setId(count.incrementAndGet());

        // Create the StudentTable
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentTable() throws Exception {
        int databaseSizeBeforeUpdate = studentTableRepository.findAll().size();
        studentTable.setId(count.incrementAndGet());

        // Create the StudentTable
        StudentTableDTO studentTableDTO = studentTableMapper.toDto(studentTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentTable in the database
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentTable() throws Exception {
        // Initialize the database
        studentTableRepository.saveAndFlush(studentTable);

        int databaseSizeBeforeDelete = studentTableRepository.findAll().size();

        // Delete the studentTable
        restStudentTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentTable.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentTable> studentTableList = studentTableRepository.findAll();
        assertThat(studentTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
