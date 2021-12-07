package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonTableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonTableDTO.class);
        LessonTableDTO lessonTableDTO1 = new LessonTableDTO();
        lessonTableDTO1.setId(1L);
        LessonTableDTO lessonTableDTO2 = new LessonTableDTO();
        assertThat(lessonTableDTO1).isNotEqualTo(lessonTableDTO2);
        lessonTableDTO2.setId(lessonTableDTO1.getId());
        assertThat(lessonTableDTO1).isEqualTo(lessonTableDTO2);
        lessonTableDTO2.setId(2L);
        assertThat(lessonTableDTO1).isNotEqualTo(lessonTableDTO2);
        lessonTableDTO1.setId(null);
        assertThat(lessonTableDTO1).isNotEqualTo(lessonTableDTO2);
    }
}
