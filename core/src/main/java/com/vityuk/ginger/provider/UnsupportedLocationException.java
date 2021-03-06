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

package com.vityuk.ginger.provider;

import com.vityuk.ginger.LocalizationException;

/**
 * @author Andriy Vityuk
 */
public class UnsupportedLocationException extends LocalizationException {
    private static final long serialVersionUID = 4479019149604087339L;

    public UnsupportedLocationException(String location) {
        super(createMessage(location));
    }

    private static String createMessage(String location) {
        return "Unsupported location: '" + location + "'";
    }
}
