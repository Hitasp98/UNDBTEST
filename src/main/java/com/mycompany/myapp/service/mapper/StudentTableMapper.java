package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.StudentTableDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentTable} and its DTO {@link StudentTableDTO}.
 */
@Mapper(componentModel = "spring", uses = { LessonTableMapper.class })
public interface StudentTableMapper extends EntityMapper<StudentTableDTO, StudentTable> {
    @Mapping(target = "lessonTables", source = "lessonTables", qualifiedByName = "idSet")
    StudentTableDTO toDto(StudentTable s);

    @Mapping(target = "removeLessonTable", ignore = true)
    StudentTable toEntity(StudentTableDTO studentTableDTO);
}
