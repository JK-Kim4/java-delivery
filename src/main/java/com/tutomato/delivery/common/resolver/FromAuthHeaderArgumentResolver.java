package com.tutomato.delivery.common.resolver;

import com.tutomato.delivery.common.annotation.FromAuthHeader;
import com.tutomato.delivery.common.extractor.AuthMemberExtractor;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class FromAuthHeaderArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthMemberExtractor authMemberExtractor;

    public FromAuthHeaderArgumentResolver(
        AuthMemberExtractor authMemberExtractor
    ) {
        this.authMemberExtractor = authMemberExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FromAuthHeader.class)
            && AuthMemberRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        return authMemberExtractor.extract();

    }
}
