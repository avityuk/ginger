package com.vityuk.ginger;

import java.lang.reflect.Method;

public class UnsupportedTypeException extends LocalizationException {
    public UnsupportedTypeException(Class<?> returnType, Method method) {
        super(createMessage(returnType, method));
    }

    private static String createMessage(Class<?> returnType, Method method) {
        String returnTypeName = returnType.getName();
        String methodName = method.getName();
        String declaringTypeName = method.getDeclaringClass().getName();
        return "Unsupported return type: " + returnTypeName + " for method: " + methodName + " in " + declaringTypeName;
    }
}
