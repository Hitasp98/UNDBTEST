package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeacherTableMapperTest {

    private TeacherTableMapper teacherTableMapper;

    @BeforeEach
    public void setUp() {
        teacherTableMapper = new TeacherTableMapperImpl();
    }
}
