package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonTable.class);
        LessonTable lessonTable1 = new LessonTable();
        lessonTable1.setId(1L);
        LessonTable lessonTable2 = new LessonTable();
        lessonTable2.setId(lessonTable1.getId());
        assertThat(lessonTable1).isEqualTo(lessonTable2);
        lessonTable2.setId(2L);
        assertThat(lessonTable1).isNotEqualTo(lessonTable2);
        lessonTable1.setId(null);
        assertThat(lessonTable1).isNotEqualTo(lessonTable2);
    }
}
