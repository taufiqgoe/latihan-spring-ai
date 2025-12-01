package id.taufiq.latihan.spring_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel, VectorStore vectorStore) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore).build();
        return ChatClient.builder(chatModel).defaultAdvisors(advisor).build();
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel chatModel, VectorStore vectorStore) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore).build();
        return ChatClient.builder(chatModel).defaultAdvisors(advisor).build();
    }
}
