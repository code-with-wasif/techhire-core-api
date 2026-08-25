package io.github.codewithwasif.techhire.repository;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepo extends JpaRepository<JobPostEntity, Long> {
    List<JobPostEntity> findByStatusIgnoreCase(String status);

    @Query("SELECT j FROM JobPostEntity j WHERE " +
            "(:techStack IS NULL OR :techStack MEMBER OF j.techStack) AND " +
            "(:minSalary IS NULL OR j.minSalary >= :minSalary)")
    List<JobPostEntity> customJobsSearch(@Param("techStack") String techStack,
                                                @Param("minSalary") Integer minSalary);
}
