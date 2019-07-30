package org.molgenis;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

public class TypeUtils {

  public static UUID toUuid(Object v) {
    try {
      if (v == null) return null;
      if (v instanceof String) return UUID.fromString((String) v);
      return (UUID) v;
    } catch (Exception e) {
      throw new IllegalArgumentException("Value cannot be converted to UUID: " + v);
    }
  }

  public static UUID[] toUuidArray(Object v) {
    if (v == null) return null;
    if (v instanceof UUID[]) return (UUID[]) v;
    if (v instanceof Object[]) {
      return Stream.of((Object[]) v).map(TypeUtils::toUuid).toArray(UUID[]::new);
    }
    return new UUID[] {toUuid(v)};
  }

  public static String[] toStringArray(Object v) {
    if (v instanceof String[]) return (String[]) v;
    else if (v instanceof Object[])
      return Stream.of((Object[]) v).map(TypeUtils::toString).toArray(String[]::new);
    return new String[] {v.toString()};
  }

  public static String toString(Object v) {
    if (v == null) return null;
    if (v instanceof String) return (String) v;
    return v.toString();
  }

  public static Integer toInt(Object v) {
    try {
      if (v instanceof String) return Integer.parseInt((String) v);
      return (Integer) v;
    } catch (Exception e) {
      throw new UnsupportedOperationException("Value cannot be converted to Integer: " + v);
    }
  }

  public static Integer[] toIntArray(Object v) {
    if (v == null) return null;
    if (v instanceof Integer[]) return (Integer[]) v;
    else if (v instanceof Object[])
      return Stream.of((Object[]) v).map(TypeUtils::toInt).toArray(Integer[]::new);
    return new Integer[] {toInt(v)};
  }

  public static Boolean toBool(Object v) {
    try {
      if (v instanceof String) {
        if ("true".equalsIgnoreCase((String) v)) return true;
        if ("false".equalsIgnoreCase((String) v)) return false;
      }
      return (Boolean) v;
    } catch (Exception e) {
      throw new UnsupportedOperationException("Value cannot be converted to Boolean[]: " + v);
    }
  }

  public static Boolean[] toBoolArray(Object v) {
    if (v instanceof Boolean[]) return (Boolean[]) v;
    else if (v instanceof Object[])
      return Stream.of((Object[]) v).map(TypeUtils::toBool).toArray(Boolean[]::new);
    return new Boolean[] {toBool(v)};
  }

  public static Double toDecimal(Object v) {
    try {
      if (v instanceof String) return Double.parseDouble((String) v);
      return (Double) v;
    } catch (Exception e) {
      throw new UnsupportedOperationException("Value cannot be converted to Double: " + v);
    }
  }

  public static Double[] toDecimalArray(Object v) {
    if (v instanceof Double[]) return (Double[]) v;
    else if (v instanceof Object[])
      return Stream.of((Object[]) v).map(TypeUtils::toDecimal).toArray(Double[]::new);
    return new Double[] {toDecimal(v)};
  }

  public static LocalDate toDate(Object v) {
    try {
      if (v == null) return null;
      else if (v instanceof LocalDate) return (LocalDate) v;
      else if (v instanceof OffsetDateTime) {
        return ((OffsetDateTime) v).toLocalDate();
      } else {
        return LocalDate.parse(v.toString());
      }
    } catch (Exception e) {
      throw new UnsupportedOperationException("Value cannot be converted to LocalDate: " + v);
    }
  }

  public static LocalDate[] toDateArrray(Object v) {

    if (v == null) return null;
    else if (v instanceof LocalDate[]) return (LocalDate[]) v;
    else if (v instanceof Object[])
      return Stream.of((Object[]) v).map(TypeUtils::toDate).toArray(LocalDate[]::new);
    return new LocalDate[] {toDate(v)};
  }

  public static LocalDateTime toDateTime(Object v) {
    if (v == null) return null;
    if (v instanceof LocalDateTime) return (LocalDateTime) v;
    if (v instanceof OffsetDateTime) return ((OffsetDateTime) v).toLocalDateTime();
    if (v instanceof Timestamp) return ((Timestamp) v).toLocalDateTime();
    return LocalDateTime.parse(v.toString());
  }

  public static LocalDateTime[] toDateTimeArray(Object v) {
    if (v == null) return null;
    if (v instanceof LocalDateTime[]) return (LocalDateTime[]) v;
    if (v instanceof Object[]) {
      return Stream.of((Object[]) v).map(TypeUtils::toDateTime).toArray(LocalDateTime[]::new);
    }
    return new LocalDateTime[] {toDateTime(v)};
  }

  public static String toText(Object v) {
    return toString(v);
  }

  public static String[] toTextArray(Object v) {
    return toStringArray(v);
  }
}
