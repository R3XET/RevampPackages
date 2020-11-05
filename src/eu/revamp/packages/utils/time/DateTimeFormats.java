package eu.revamp.packages.utils.time;

import org.apache.commons.lang.time.FastDateFormat;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeFormats
{
    public static TimeZone SERVER_TIME_ZONE = TimeZone.getTimeZone("CET");
    public static ZoneId SERVER_ZONE_ID = DateTimeFormats.SERVER_TIME_ZONE.toZoneId();
    public static FastDateFormat DAY_MTH_HR_MIN_SECS = FastDateFormat.getInstance("dd/MM HH:mm:ss", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static FastDateFormat DAY_MTH_YR_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM/yy hh:mma", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static FastDateFormat DAY_MTH_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM hh:mma", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static FastDateFormat HR_MIN_AMPM = FastDateFormat.getInstance("hh:mma", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static FastDateFormat HR_MIN_AMPM_TIMEZONE = FastDateFormat.getInstance("hh:mma z", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static FastDateFormat HR_MIN = FastDateFormat.getInstance("hh:mm", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static FastDateFormat MIN_SECS = FastDateFormat.getInstance("mm:ss", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static FastDateFormat KOTH_FORMAT = FastDateFormat.getInstance("m:ss", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
    public static ThreadLocal<DecimalFormat> REMAINING_SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
    public static ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));
}
