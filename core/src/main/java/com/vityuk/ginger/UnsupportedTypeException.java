package com.vityuk.ginger;

import java.lang.reflect.Method;

public class UnsupportedTypeException extends LocalizationException {
    private static final long serialVersionUID = 2970220813046561486L;

    public UnsupportedTypeException(Class<?> returnType, Method method) {
        super(createMessage(returnType, method));
    }

    private static String createMessage(Class<?> returnType, Method method) {
        String returnTypeName = returnType.getName();
        String methodName = method.getName();
        String declaringTypeName = method.getDeclaringClass().getName();
        // TODO: add detailed method name description
        return "Unsupported return type: " + returnTypeName + " for method: " + methodName + " in " + declaringTypeName;
    }
}
