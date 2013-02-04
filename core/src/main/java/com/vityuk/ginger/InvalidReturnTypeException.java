/*
 * Copyright 2013 Andriy Vityuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vityuk.ginger;

import java.lang.reflect.Method;

/**
 * @author Andriy Vityuk
 */
public class InvalidReturnTypeException extends LocalizationException {
    private static final long serialVersionUID = 2970220813046561486L;

    public InvalidReturnTypeException(Class<?> returnType, Method method) {
        super(createMessage(returnType, method));
    }

    private static String createMessage(Class<?> returnType, Method method) {
        String returnTypeName = returnType.getName();
        String methodName = method.getName();
        String declaringTypeName = method.getDeclaringClass().getName();
        // TODO: add detailed method name description
        return "Invalid return type: " + returnTypeName + " for method: " + methodName + " in " + declaringTypeName;
    }
}
