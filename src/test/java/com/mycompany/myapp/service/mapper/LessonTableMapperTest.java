package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LessonTableMapperTest {

    private LessonTableMapper lessonTableMapper;

    @BeforeEach
    public void setUp() {
        lessonTableMapper = new LessonTableMapperImpl();
    }
}
