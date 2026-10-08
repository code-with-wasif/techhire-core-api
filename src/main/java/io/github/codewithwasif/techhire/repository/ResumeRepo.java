package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResumeRepo extends JpaRepository<ResumeEntity, Long> {
    @Query(value = "SELECT * FROM resumes WHERE candidate_id = :id", nativeQuery = true)
    List<ResumeEntity> getAllResumesById(@Param("id") Long id);
}
