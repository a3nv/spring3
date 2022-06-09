package com.tryexplain.spring3;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class Spring3Application {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        SpringApplication.run(Spring3Application.class, args);
    }

    @Bean
    TodoClient todoClient() {
        return proxyFactory().createClient(TodoClient.class);
    }

    @Bean
    HttpServiceProxyFactory proxyFactory() {
        WebClient webClient = WebClient.builder().baseUrl(BASE_URL).build();
        WebClientAdapter adapter = new WebClientAdapter(webClient);
        return new HttpServiceProxyFactory(adapter);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            System.out.println(todoClient().get(1l));
        };
    }
}

@HttpExchange("/todos")
interface TodoClient {

    @GetExchange
    List<Todo> todoList();

    @PostExchange
    Todo create(@RequestBody Todo todo);

    @GetExchange("/{todoId}")
    Todo get(@PathVariable("todoId") Long id);

}

record Todo(Long id, String title, boolean completed, Long userId) {
};
