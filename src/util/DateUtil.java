package util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    public static long daysFromToday(LocalDate date) {
        if (date == null) return Integer.MAX_VALUE;
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
    public static boolean isWithinDays(LocalDate date, int days) {
        if (date == null) return false;
        long diff = daysFromToday(date);
        return diff >= 0 && diff <= days;
    }
    public static boolean isExpired(LocalDate date) {
        if (date == null) return false;
        return LocalDate.now().isAfter(date);
    }
}
