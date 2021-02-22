package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;

public class Util {
    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T ldt, @Nullable T start,@Nullable T end){
        return  (start == null || ldt.compareTo(start) >= 0) && (end == null || ldt.compareTo(end) < 0);
    }

}
