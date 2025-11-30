package id.taufiq.latihan.spring_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel, PgVectorStore pgVectorStore) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(pgVectorStore).build();
        return ChatClient.builder(chatModel).defaultAdvisors(advisor).build();
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel chatModel, PgVectorStore pgVectorStore) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(pgVectorStore).build();
        return ChatClient.builder(chatModel).defaultAdvisors(advisor).build();
    }
}
