package com.vityuk.ginger;

import java.lang.reflect.Method;

public class InvalidParameterTypeException extends LocalizationException {
    private static final long serialVersionUID = 9109152766439462504L;

    public InvalidParameterTypeException(Class<?> parameterType, Method method) {
        super(createMessage(parameterType, method));
    }

    private static String createMessage(Class<?> parameterType, Method method) {
        String parameterTypeName = parameterType.getName();
        String methodName = method.getName();
        String declaringTypeName = method.getDeclaringClass().getName();
        // TODO: add detailed method name description
        return "Invalid parameter type: " + parameterTypeName + " for method: " + methodName + " in " + declaringTypeName;
    }
}
