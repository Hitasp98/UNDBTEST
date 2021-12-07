package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeacherTableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeacherTableDTO.class);
        TeacherTableDTO teacherTableDTO1 = new TeacherTableDTO();
        teacherTableDTO1.setId(1L);
        TeacherTableDTO teacherTableDTO2 = new TeacherTableDTO();
        assertThat(teacherTableDTO1).isNotEqualTo(teacherTableDTO2);
        teacherTableDTO2.setId(teacherTableDTO1.getId());
        assertThat(teacherTableDTO1).isEqualTo(teacherTableDTO2);
        teacherTableDTO2.setId(2L);
        assertThat(teacherTableDTO1).isNotEqualTo(teacherTableDTO2);
        teacherTableDTO1.setId(null);
        assertThat(teacherTableDTO1).isNotEqualTo(teacherTableDTO2);
    }
}
