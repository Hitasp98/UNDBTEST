package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TeacherTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TeacherTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeacherTableRepository extends JpaRepository<TeacherTable, Long> {}
