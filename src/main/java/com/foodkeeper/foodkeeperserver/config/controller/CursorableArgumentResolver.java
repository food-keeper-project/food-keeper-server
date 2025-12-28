package com.foodkeeper.foodkeeperserver.config.controller;

import com.foodkeeper.foodkeeperserver.common.controller.CursorDefault;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
public class CursorableArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CursorDefault.class)
                && parameter.getParameterType().equals(Cursorable.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory)
            throws Exception {
        CursorDefault annotation = parameter.getParameterAnnotation(CursorDefault.class);
        assert annotation != null;

        int defaultLimit = annotation.defaultLimit();

        String cursorParam = webRequest.getParameter("cursor");
        String limitParam = webRequest.getParameter("limit");

        Object cursor = null;
        if (cursorParam != null) {
            WebDataBinder binder = Objects.requireNonNull(binderFactory).createBinder(webRequest, null, "cursor");
            cursor = binder.convertIfNecessary(cursorParam, getCursorType(parameter));
        }
        Integer limit = (limitParam == null) ? defaultLimit : Integer.parseInt(limitParam);

        return new Cursorable<>(cursor, limit);
    }

    private Class<?> getCursorType(MethodParameter parameter) {
        return ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();
    }
}




