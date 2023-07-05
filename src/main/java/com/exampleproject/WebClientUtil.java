package com.exampleproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WebClientUtil {

	@Autowired
	private WebClient client;

	public Mono<ResultEntity> prepareClientForCreateEmpl(EmployeeDTO dto, String url) {
		return client.post().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(dto)
				.retrieve().bodyToMono(ResultEntity.class);
	}

	public Mono<ResultEntity> prepareClientForUpdateEmpl(EmployeeDTO dto2, String url) {
		return client.put().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(dto2)
				.retrieve().bodyToMono(ResultEntity.class);
	}

	public Mono<ResultEntity> prepareClientForDeleteEmpl(String url) {
		return client.delete().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.bodyToMono(ResultEntity.class);
	}
}
