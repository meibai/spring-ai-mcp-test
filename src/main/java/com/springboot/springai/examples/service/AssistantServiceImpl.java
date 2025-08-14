package com.springboot.springai.examples.service;

import com.springboot.springai.examples.config.ChatConfig;
import com.springboot.springai.examples.dao.UserData;
import com.springboot.springai.examples.dto.RequestDTO;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Content;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AssistantServiceImpl implements AssistantService {

    private final OpenAiChatModel openAiChatModel;
    private final ChatConfig chatConfig;
    private final ChatMemory chatMemory;
    private final UserData userData;

    @Autowired
    public AssistantServiceImpl(OpenAiChatModel openAiChatModel, ChatConfig chatConfig, ChatMemory chatMemory, UserData userData) {
        this.openAiChatModel = openAiChatModel;
        this.chatConfig = chatConfig;
        this.chatMemory = chatMemory;
        this.userData = userData;
    }

    @Override
    public String chat(String input) {
        String difficultyLevel = "Beginner";
/*        String promptTemplate =
                """
                    You are a highly intelligent and patient teaching assistant.
                    Your role is to explain concepts clearly, answer questions concisely, and provide examples for better understanding.
                    Always adapt your response to the user's level of knowledge.

                    Query: %s
                    Difficulty level: %s

                    Provide a clear, structured explanation and use examples when helpful.
                """;*/
        String prompt = String.format(chatConfig.teacherAssistantPromptTemplate(), input, difficultyLevel); //Get template from Config file

/*        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.5)
                .withMaxTokens(750)
                .withFrequencyPenalty(0.2)
                .build();*/
        Prompt chatbotPrompt = new Prompt(prompt, chatConfig.teacherAssistantChatOptions()); //Get chatOption value from Config file
        ChatResponse chatResponse = openAiChatModel.call(chatbotPrompt);
        Generation result = chatResponse.getResult();
        return result != null ? result.getOutput().getText() : "No response received";
    }

    @Override
    public String chatMemory(String chatId, String input) {
        // Retrieve Previous Context from ChatMemory.
        List<Message> previousMessages = chatMemory.get(chatId, 100);
        String previousContext = previousMessages.isEmpty() ? "" : previousMessages
                .stream()
                .map(Content::getText)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        // Prompt with Query and System Instruction
        String difficultyLevel = "Beginner";
        String prompt = String.format(chatConfig.teacherAssistantPromptTemplate(), input, difficultyLevel); // Get template from Config file

        // Prompt with Previous Context
        if(!previousContext.isEmpty()) {
            prompt = previousContext + "\n" + prompt;
        }

        // Prompt with LLM configurations
        Prompt chatbotPrompt = new Prompt(prompt, chatConfig.teacherAssistantChatOptions()); // Get chatOption value from Config file

        // Call Open AI Model with customized Prompt
        ChatResponse chatResponse = openAiChatModel.call(chatbotPrompt);
        Generation result = chatResponse.getResult();

        // Store User input and AI response in ChatMemory
        UserMessage userMessage = new UserMessage(input); // It represents User's input
        SystemMessage systemMessage = new SystemMessage(result.getOutput().getText()); // It represents AI's response
        chatMemory.add(chatId, userMessage);
        chatMemory.add(chatId, systemMessage);

        // Return the result
        return result.getOutput().getText();
    }

    @Override
    public String chatMemoryWithFunctionCalling(String chatId, String input) {
        // Retrieve Previous Context from ChatMemory.
        List<Message> previousMessages = chatMemory.get(chatId, 100);
        String previousContext = previousMessages.isEmpty() ? "" : previousMessages
                .stream()
                .map(Content::getText)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        // Prompt with Query and System Instruction
        String difficultyLevel = "Beginner";
        String prompt = String.format(chatConfig.teacherAssistantPromptTemplateNew(), input, difficultyLevel); // Get template from Config file

        // Prompt with Previous Context
        if(!previousContext.isEmpty()) {
            prompt = previousContext + "\n" + prompt;
        }

        // Prompt with LLM configurations
        Prompt chatbotPrompt = new Prompt(prompt, chatConfig.teacherAssistantChatOptions()); // Get chatOption value from Config file

        // Call Open AI Model with customized Prompt
        ChatResponse chatResponse = openAiChatModel.call(chatbotPrompt);
        Generation result = chatResponse.getResult();

        // Store User input and AI response in ChatMemory
        UserMessage userMessage = new UserMessage(input); // It represents User's input
        SystemMessage systemMessage = new SystemMessage(result.getOutput().getText()); // It represents AI's response
        chatMemory.add(chatId, userMessage);
        chatMemory.add(chatId, systemMessage);

        // Return the result
        return result.getOutput().getText();
    }

    @Override
    public void clearChat(String chatId) {
        chatMemory.clear(chatId);
    }

    @Bean("operation1")
    @Description("Operation1")
    public Function<RequestDTO, String> operation1() {
        return requestDTO -> {
            System.out.println("Operation1 has been executed!!!");
            System.out.println(requestDTO.getName());
            userData.register(requestDTO.getName());
            return "Operation1 is done!!!";
        };
    }

    @Bean("operation2")
    @Description("Operation2")
    public Function<ReqDTO, String> operation2() {
        return requestDTO -> {
            System.out.println("Operation2 has been executed!!!");
            System.out.println(requestDTO.input());
            System.out.println(userData.getRegisteredNames());
            return "Operation2 is done!!!";
        };
    }

    public record ReqDTO(String input) {}
}