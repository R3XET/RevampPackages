package eu.revamp.packages.utils.time;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

public class TimeUtils
{
    private static long MINUTE = TimeUnit.MINUTES.toMillis(1L);
    private static long HOUR = TimeUnit.HOURS.toMillis(1L);

    public static String getRemaining(long millis, boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }

    public static String getRemainingg(long millis, boolean milliseconds) {
        return getRemainingg(millis, milliseconds, true);
    }

    public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < TimeUtils.MINUTE) {
            return String.valueOf((trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001)) + 's';
        }
        return DurationFormatUtils.formatDuration(duration, ((duration >= TimeUtils.HOUR) ? "HH:" : "") + "mm:ss");
    }

    public static String getRemainingg(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < TimeUtils.MINUTE) {
            return String.valueOf((trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001)) + 's';
        }
        return DurationFormatUtils.formatDuration(duration, ((duration >= TimeUtils.HOUR) ? "H:" : "") + "mm:ss");
    }

    public static String TimerFormat(double data) {
        int minutes = (int)(data / 60.0);
        int seconds = (int)(data % 60.0);
        String str = String.format("%02d:%02d", minutes, seconds);
        return str;
    }

    public static String formatSecondsToHours(double d) {
        int i = (int)(d / 3600.0);
        int j = (int)(d % 3600.0 / 60.0);
        int k = (int)(d % 60.0);
        String str = String.format("%02d:%02d:%02d", i, j, k);
        return str;
    }
}
