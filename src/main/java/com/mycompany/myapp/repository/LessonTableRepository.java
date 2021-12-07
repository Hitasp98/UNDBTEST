package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LessonTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LessonTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonTableRepository extends JpaRepository<LessonTable, Long> {}
