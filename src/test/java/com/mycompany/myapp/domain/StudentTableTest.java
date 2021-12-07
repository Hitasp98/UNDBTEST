package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentTable.class);
        StudentTable studentTable1 = new StudentTable();
        studentTable1.setId(1L);
        StudentTable studentTable2 = new StudentTable();
        studentTable2.setId(studentTable1.getId());
        assertThat(studentTable1).isEqualTo(studentTable2);
        studentTable2.setId(2L);
        assertThat(studentTable1).isNotEqualTo(studentTable2);
        studentTable1.setId(null);
        assertThat(studentTable1).isNotEqualTo(studentTable2);
    }
}
