package io.github.codewithwasif.techhire.ai.service;

import io.github.codewithwasif.techhire.ai.prompt.JobAiPromptBuilder;
import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobEmbeddingSvc {
    private final EmbeddingModel embeddingModel;
    private final JobAiPromptBuilder jobAiPromptBuilder;


    public float[] generateEmbedding(JobPostDto.CreateJobPostRequest request){
        String embeddingText = jobAiPromptBuilder.buildJobEmbeddingText(request);
        return embeddingModel.embed(embeddingText);
    }

    public Optional<float[]> generateEmbeddingIfDirty(JobPostDto.UpdateJobPostRequest requestPost,
                                                      JobPostEntity existingPost){
        if (requestPost == null || existingPost == null) {
            return Optional.empty();
        }

        boolean hasTitleUpdate = StringUtils.hasText(requestPost.title());
        boolean hasDescUpdate = StringUtils.hasText(requestPost.description());
        boolean hasTechStackUpdate = requestPost.techStack() != null && !requestPost.techStack().isEmpty();

        if (hasTitleUpdate || hasDescUpdate || hasTechStackUpdate) {
            String embeddingText = jobAiPromptBuilder.buildJobEmbeddingText(existingPost, requestPost);
            return Optional.of(embeddingModel.embed(embeddingText));
        }
        return Optional.empty();
    }
}
