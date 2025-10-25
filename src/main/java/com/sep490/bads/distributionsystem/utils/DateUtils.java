package com.sep490.bads.distributionsystem.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtils {

    public static final String ZONE_UTC = "UTC";

    public static final String ZONE_VN = "Asia/Ho_Chi_Minh";

    public static Long getNowMillisAtUtc() {
        return LocalDateTime.now(ZoneId.of(ZONE_UTC)).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDate nowDate() {
        return LocalDate.now(ZoneId.of(ZONE_VN));
    }

    public static LocalDateTime getNowDateTimeAtUtc() {
        return LocalDateTime.now(ZoneId.of(ZONE_UTC));
    }

    public static LocalDateTime getNowDateTimeAtVn() {
        return LocalDateTime.now(ZoneId.of(ZONE_VN));
    }

    public static LocalDate getNowDateAtVn() {
        return getNowDateTimeAtVn().toLocalDate();
    }

    public static LocalDate getNowDateAtUtc() {
        return getNowDateTimeAtUtc().toLocalDate();
    }

    public static LocalDateTime longToLocalDateTime(Long millisAtUtc) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisAtUtc), ZoneId.of(ZONE_UTC));
    }

    public static LocalDate longToLocalDate(Long millisAtUtc) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(millisAtUtc), ZoneId.of(ZONE_UTC));
    }

    public static Long timeToLongAtUtc(LocalDateTime dateTimeAtUtc) {
        return ZonedDateTime.of(dateTimeAtUtc, ZoneId.of(ZONE_UTC)).toInstant().toEpochMilli();
    }

    public static Long timeToLongAtVn(LocalDateTime dateTimeAtVn) {
        return ZonedDateTime.of(dateTimeAtVn, ZoneId.of(ZONE_VN)).toInstant().toEpochMilli();
    }

    public static Long getStartOfDay(LocalDate date) {
        LocalDateTime localDateTime = date.atStartOfDay();
        return localDateTime
                .atZone(ZoneId.of(ZONE_VN))
                .withZoneSameInstant(ZoneId.of(ZONE_UTC))
                .toInstant()
                .toEpochMilli();
    }

    public static Long getEndOfDay(LocalDate date) {
        LocalDateTime localDateTime = date.atStartOfDay().plusDays(1).minusSeconds(1);
        return localDateTime
                .atZone(ZoneId.of(ZONE_VN))
                .withZoneSameInstant(ZoneId.of(ZONE_UTC))
                .toInstant()
                .toEpochMilli();
    }

    public static void main(String[] args) {
        System.out.println(timeToLongAtVn(LocalDateTime.now()));
    }

    public static Long convertStringToLongUTC(String time){
        LocalDateTime localDateTime = convertFromStringToLocalDateTime(time);
        return localDateTime.atZone(ZoneId.of(ZONE_VN))
                .withZoneSameInstant(ZoneId.of(ZONE_UTC)).toInstant().toEpochMilli();
    }

    public static LocalDateTime convertFromStringToLocalDateTime(String time) {
        LocalDateTime localDateTime =
                LocalDateTime.parse(time, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return localDateTime;
    }

    public static LocalDate convertStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {

        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .collect(Collectors.toList());
    }

    public static LocalDate getFirstDayOfMonth(LocalDate dateNow) {
        return dateNow.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
        if(dateTime == null){
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(dateTimeFormatter);
    }

    public static int secondsUntilEndOfMonthAtVn() {
        LocalDateTime now = getNowDateTimeAtVn();
        LocalDateTime endOfMonth = now
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        long seconds = ChronoUnit.SECONDS.between(now, endOfMonth);
        return (int) seconds;
    }
}
