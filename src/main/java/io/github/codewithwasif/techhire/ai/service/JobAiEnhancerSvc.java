package io.github.codewithwasif.techhire.ai.service;

import io.github.codewithwasif.techhire.ai.dto.EnhanceJobDescriptionRequest;
import io.github.codewithwasif.techhire.ai.prompt.JobAiPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class JobAiEnhancerSvc {

    private final ChatClient chatClient;
    private final JobAiPromptBuilder jobAiPromptBuilder;


    public Flux<String> jobDescriptionEnhancer(EnhanceJobDescriptionRequest descriptionRequest){

        String enhancementPrompt = jobAiPromptBuilder.buildJobEnhancementPrompt(descriptionRequest);

        return chatClient
                .prompt()
                .system(JobAiPromptBuilder.JOB_ENHANCER_SYSTEM_PROMPT)
                .user(enhancementPrompt)
                .stream()
                .content();
    }
}

