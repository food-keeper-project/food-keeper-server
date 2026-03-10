package com.foodkeeper.foodkeeperserver.ai.implement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.client.ResourceAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClovaRetryExecutorTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ChatClient chatClient;

    ClovaRetryExecutor clovaRetryExecutor;

    @BeforeEach
    void setUp() {
        clovaRetryExecutor = new ClovaRetryExecutor(chatClient);
    }

    @Test
    @DisplayName("ChatClient 정상 호출 시 응답 문자열을 반환한다")
    void call_returnsContentFromChatClient() {
        String expected = "{\"menuName\": \"테스트\"}";
        given(chatClient.prompt().system(any(String.class)).user(any(String.class)).call().content())
                .willReturn(expected);

        String result = clovaRetryExecutor.call("system prompt", "user prompt");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("ChatClient 예외 발생 시 예외가 전파된다 (@Retryable이 인터셉트할 수 있도록)")
    void call_propagatesExceptionForRetry() {
        given(chatClient.prompt().system(any(String.class)).user(any(String.class)).call().content())
                .willThrow(new ResourceAccessException("연결 오류"));

        assertThatThrownBy(() -> clovaRetryExecutor.call("system", "user"))
                .isInstanceOf(ResourceAccessException.class)
                .hasMessageContaining("연결 오류");
    }
}
