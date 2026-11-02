package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.entity.DeveloperProfileEntity;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.exception.BadRequestException;
import io.github.codewithwasif.techhire.exception.ResourceNotFoundException;
import io.github.codewithwasif.techhire.mapper.JobPostMapper;
import io.github.codewithwasif.techhire.repository.DeveloperProfileRepo;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import io.github.codewithwasif.techhire.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class JobSearchSvc {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 30;

    private final JobPostRepo jobPostRepo;
    private final JobPostMapper jobPostMapper;
    private final DeveloperProfileRepo developerProfileRepo;
    private final SecurityUtils securityUtils;


    public List<JobPostDto.ShowAllPostsResponse> customSearch(String techStack, Integer minSalary) {
        List<JobPostEntity> jobs = jobPostRepo.customJobsSearch(techStack, minSalary);
        return jobPostMapper.toShowAllPostsResponse(jobs);
    }

    public List<JobPostDto.ShowAllPostsResponse> recommendedJobs(int requestedLimit){
        int safeLimit = sanitizeLimit(requestedLimit);

        UserEntity currentUser = securityUtils.getCurrentUser();

        DeveloperProfileEntity developerProfile = developerProfileRepo.findByDeveloperId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer profile not found. Please create your profile first."));

        if (developerProfile.getEmbedding() == null || developerProfile.getEmbedding().length == 0) {
            log.error("Missing vector embedding for Developer Profile ID: {}, User ID: {}", developerProfile.getId(), currentUser.getId());
            throw new BadRequestException("Your profile information is incomplete. Please update your profile to view recommendations.");
        }

        List<JobPostEntity> jobPostsList = jobPostRepo.findAllByStatus("ACTIVE");

        if (jobPostsList.isEmpty()){
            return Collections.emptyList();
        }

        List<JobPostDto.RecommendedPosts> toSort = new ArrayList<>();

        for (JobPostEntity post : jobPostsList){
            double cosineSimilarity = cosineSimilarity(post.getEmbedding(), developerProfile.getEmbedding());

            JobPostDto.RecommendedPosts posts = jobPostMapper.toRecommendedPosts(post, cosineSimilarity);

            toSort.add(posts);
        }

        sortBySimilarityInPlace(toSort);

        List<JobPostDto.RecommendedPosts> top10 = topKMatches(toSort, safeLimit);

        return jobPostMapper.toShowAllRecommendedPostsResponse(top10);
    }

    private double cosineSimilarity(float[] a, float[] b) {
        if (a == null || b == null || a.length != b.length || a.length == 0) {
            return 0.0;
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dotProduct += (a[i] * b[i]);
            normA += (a[i] * a[i]);
            normB += (b[i] * b[i]);
        }

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public void sortBySimilarityInPlace(List<JobPostDto.RecommendedPosts> jobPost) {
        if (jobPost == null || jobPost.isEmpty()) {
            return;
        }
        jobPost.sort(Comparator.comparing(JobPostDto.RecommendedPosts::similarity, Comparator.nullsLast(Comparator.reverseOrder())));
    }

    private List<JobPostDto.RecommendedPosts> topKMatches(
            List<JobPostDto.RecommendedPosts> matches, int limit) {

        if (matches == null || matches.isEmpty()) {
            return Collections.emptyList();
        }

        List<JobPostDto.RecommendedPosts> topMatches = new ArrayList<>();

        int numberOfMatches = Math.min(limit, matches.size());

        for (int i = 0; i < numberOfMatches; i++) {
            topMatches.add(matches.get(i));
        }
        return topMatches;
    }

    private int sanitizeLimit(int limit) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}
