package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.LessonTableDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LessonTable} and its DTO {@link LessonTableDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LessonTableMapper extends EntityMapper<LessonTableDTO, LessonTable> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<LessonTableDTO> toDtoIdSet(Set<LessonTable> lessonTable);
}
