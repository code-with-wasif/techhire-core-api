package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.DeveloperProfileDto;
import io.github.codewithwasif.techhire.entity.DeveloperProfileEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.exception.DuplicateResourceException;
import io.github.codewithwasif.techhire.mapper.DeveloperProfileMapper;
import io.github.codewithwasif.techhire.repository.DeveloperProfileRepo;
import io.github.codewithwasif.techhire.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DeveloperProfileSvc {

    private final DeveloperProfileRepo developerProfileRepo;
    private final DeveloperProfileMapper developerProfileMapper;
    private final SecurityUtils securityUtils;
    private final EmbeddingModel embeddingModel;


    @Transactional
    public DeveloperProfileDto.DeveloperProfileResponse createProfile(
            DeveloperProfileDto.CreateDeveloperProfileRequest request
    ) {
        UserEntity developer = securityUtils.getCurrentUser();

        if (developerProfileRepo.existsByDeveloperId(developer.getId())) {
            throw new DuplicateResourceException("A developer profile already exists for this account.");
        }

        String embeddingText = developerProfileMapper.buildEmbeddingText(request);

        float[] embeddings = embeddingModel.embed(embeddingText);

        DeveloperProfileEntity profile = developerProfileMapper.toEntity(request, developer, embeddings);
        DeveloperProfileEntity savedProfile = developerProfileRepo.save(profile);

        return developerProfileMapper.toResponse(savedProfile);
    }
}
