package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StudentTable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentTable entity.
 */
@Repository
public interface StudentTableRepository extends JpaRepository<StudentTable, Long> {
    @Query(
        value = "select distinct studentTable from StudentTable studentTable left join fetch studentTable.lessonTables",
        countQuery = "select count(distinct studentTable) from StudentTable studentTable"
    )
    Page<StudentTable> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct studentTable from StudentTable studentTable left join fetch studentTable.lessonTables")
    List<StudentTable> findAllWithEagerRelationships();

    @Query("select studentTable from StudentTable studentTable left join fetch studentTable.lessonTables where studentTable.id =:id")
    Optional<StudentTable> findOneWithEagerRelationships(@Param("id") Long id);
}
