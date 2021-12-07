package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentTableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentTableDTO.class);
        StudentTableDTO studentTableDTO1 = new StudentTableDTO();
        studentTableDTO1.setId(1L);
        StudentTableDTO studentTableDTO2 = new StudentTableDTO();
        assertThat(studentTableDTO1).isNotEqualTo(studentTableDTO2);
        studentTableDTO2.setId(studentTableDTO1.getId());
        assertThat(studentTableDTO1).isEqualTo(studentTableDTO2);
        studentTableDTO2.setId(2L);
        assertThat(studentTableDTO1).isNotEqualTo(studentTableDTO2);
        studentTableDTO1.setId(null);
        assertThat(studentTableDTO1).isNotEqualTo(studentTableDTO2);
    }
}
