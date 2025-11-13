package com.tutomato.delivery.common.resolver;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tutomato.delivery.common.annotation.FromAuthHeader;
import com.tutomato.delivery.common.exception.ForbiddenException;
import com.tutomato.delivery.common.extractor.AuthMemberExtractor;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@ExtendWith(MockitoExtension.class)
class FromAuthHeaderArgumentResolverTest {

    @Mock
    AuthMemberExtractor authMemberExtractor;

    HandlerMethodArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new FromAuthHeaderArgumentResolver(authMemberExtractor);
    }

    // ---------------------------------------
    // resolveArgument 테스트
    // ---------------------------------------
    @Nested
    @DisplayName("resolveArgument")
    class ResolveArgumentTests {

        @Test
        @DisplayName("AuthMemberExtractor 가 반환한 AuthMemberRequest 를 그대로 반환한다")
        void resolveArgument_success() throws Exception {
            // given
            MockHttpServletRequest servletRequest = new MockHttpServletRequest();
            NativeWebRequest webRequest = new ServletWebRequest(servletRequest);
            MethodParameter parameter = authMemberParameter();

            AuthMemberRequest expected = new AuthMemberRequest(1L, "testAccount");
            when(authMemberExtractor.extract()).thenReturn(expected);

            // when
            Object result = resolver.resolveArgument(parameter, null, webRequest, null);

            // then
            assertThat(result).isInstanceOf(AuthMemberRequest.class);
            AuthMemberRequest auth = (AuthMemberRequest) result;
            assertThat(auth.id()).isEqualTo(1L);
            assertThat(auth.account()).isEqualTo("testAccount");

            verify(authMemberExtractor).extract();
        }

        @Test
        @DisplayName("AuthMemberExtractor 가 ForbiddenException 을 던지면 그대로 전파된다")
        void resolveArgument_extractorThrowsForbidden() throws Exception {
            // given
            MockHttpServletRequest servletRequest = new MockHttpServletRequest();
            NativeWebRequest webRequest = new ServletWebRequest(servletRequest);
            MethodParameter parameter = authMemberParameter();

            when(authMemberExtractor.extract())
                .thenThrow(new ForbiddenException("테스트용 예외"));

            // when & then
            assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, webRequest, null))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("테스트용 예외");

            verify(authMemberExtractor).extract();
        }
    }

    // ---------------------------------------
    // MethodParameter 생성 유틸
    // ---------------------------------------

    private MethodParameter authMemberParameter() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("test", AuthMemberRequest.class);
        return new MethodParameter(method, 0);
    }

    private MethodParameter authMemberParameterWithoutAnnotation() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("testWithoutAnnotation", AuthMemberRequest.class);
        return new MethodParameter(method, 0);
    }

    private MethodParameter stringParameterWithAnnotation() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("testWithString", String.class);
        return new MethodParameter(method, 0);
    }

    static class TestController {

        // supportsParameter: true 인 케이스
        public void test(@FromAuthHeader AuthMemberRequest authMemberRequest) {
        }

        // annotation 없음 → false
        public void testWithoutAnnotation(AuthMemberRequest authMemberRequest) {
        }

        // 타입이 다름 → false
        public void testWithString(@FromAuthHeader String value) {
        }
    }
}