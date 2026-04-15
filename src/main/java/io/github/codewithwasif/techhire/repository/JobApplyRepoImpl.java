package io.github.codewithwasif.techhire.repository;

import ch.qos.logback.core.util.StringUtil;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class JobApplyRepoImpl {

    private final MongoTemplate mongoTemplate;

    public List<JobPostEntity> customJobsSearch(String techStack, Integer minSalary) {
        Query query = new Query();
        if (StringUtil.notNullNorEmpty(techStack)) {
            query.addCriteria(Criteria.where("techStack").in(techStack));
        }
        if (minSalary != null && minSalary > 0) {
            query.addCriteria(Criteria.where("minSalary").gte(minSalary));
        }
        query.addCriteria(Criteria.where("status").is("Active"));

        return mongoTemplate.find(query, JobPostEntity.class);
    }
}