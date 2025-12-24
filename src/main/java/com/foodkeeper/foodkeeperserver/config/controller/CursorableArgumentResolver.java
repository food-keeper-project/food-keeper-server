package com.foodkeeper.foodkeeperserver.config.controller;

import com.foodkeeper.foodkeeperserver.common.controller.CursorDefault;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CursorableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CursorDefault.class)
                && parameter.getParameterType().equals(Cursorable.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        CursorDefault annotation = parameter.getParameterAnnotation(CursorDefault.class);
        int defaultLimit = annotation.defaultLimit();

        String limitParam = webRequest.getParameter("limit");
        String cursorParam = webRequest.getParameter("cursor");
        Integer limit = (limitParam == null) ? defaultLimit : Integer.parseInt(limitParam);
        Long cursor = (cursorParam == null) ? null : Long.parseLong(cursorParam);

        return new Cursorable(cursor, limit);
    }
}




