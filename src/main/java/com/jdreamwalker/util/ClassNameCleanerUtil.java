package com.jdreamwalker.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ClassNameCleanerUtil {

    public static String cleanClassName(final String className) {
        return className.replace('/', '.');
    }

}
