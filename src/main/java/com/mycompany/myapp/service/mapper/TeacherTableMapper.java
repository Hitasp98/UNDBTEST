package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.TeacherTableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeacherTable} and its DTO {@link TeacherTableDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TeacherTableMapper extends EntityMapper<TeacherTableDTO, TeacherTable> {}
