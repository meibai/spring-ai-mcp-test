package com.springboot.springai.mcpClient;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.springboot.springai.mcpClient.mapper")
public class SpringAiMCPApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiMCPApplication.class, args);
	}


/*	@Bean
	public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients,
												 ConfigurableApplicationContext context) {

		return args -> {

			var chatClient = chatClientBuilder
					.defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
					.build();

			String question = "1+4是多少？";

			System.out.println("QUESTION: " + question);
			System.out.println("ASSISTANT: " + chatClient.prompt(question).call().content());

			context.close();
		};
	}*/
}