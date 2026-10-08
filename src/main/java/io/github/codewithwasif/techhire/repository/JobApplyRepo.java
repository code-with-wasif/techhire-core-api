package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplyRepo extends JpaRepository<JobApplyEntity, Long> {
}
