package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeacherTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeacherTable.class);
        TeacherTable teacherTable1 = new TeacherTable();
        teacherTable1.setId(1L);
        TeacherTable teacherTable2 = new TeacherTable();
        teacherTable2.setId(teacherTable1.getId());
        assertThat(teacherTable1).isEqualTo(teacherTable2);
        teacherTable2.setId(2L);
        assertThat(teacherTable1).isNotEqualTo(teacherTable2);
        teacherTable1.setId(null);
        assertThat(teacherTable1).isNotEqualTo(teacherTable2);
    }
}
