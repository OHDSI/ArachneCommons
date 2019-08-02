package com.odysseusinc.arachne.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QuoteUtils {

  // single quoted string with escaped single quotes
  public static final String REGEX_ESCAPED_QUOTES = "('([^'']|'')*')";

  public static String dequote(String val) {

    return Objects.nonNull(val) ? val.replaceAll("(^\"|\"$|^'|'$)", "") : val;
  }

  public static String escapeSql(String val) {

    return Objects.nonNull(val) ? val.replaceAll("'", "''") : val;
  }

  public static List<String> splitAndKeep(String val, String regex) {

    List<String> result = new ArrayList<>();
    Matcher matcher = Pattern.compile(regex).matcher(val);
    int pos = 0;
    while (matcher.find()) {
      result.add(val.substring(pos, matcher.start()));
      result.add(matcher.group());
      pos = matcher.end();
    }
    if (pos < val.length()) {
      result.add(val.substring(pos));
    }
    return result;
  }

  /**
   * Splits and replaces string containing escaped single quotes with CONCAT function,
   * e.g. 'escaped '' string' would be modified as CONCAT('escaped ','\'',' string')
   * @param val source string to be modified
   * @return modified string or original string if no escapes
   */
  public static String replaceWithConcat(String val) {

    return replaceWith(val, t -> {
      if (!t.equals("''")) {
        String literals = QuoteUtils.splitAndKeep(t, "''").stream().map(s -> {
          StringBuilder sb = new StringBuilder().append("'");
          if (s.matches("''")) {
            sb.append("\\'");
          } else {
            sb.append(s.replaceAll("'", ""));
          }
          sb.append("'");
          return sb.toString();
        }).collect(Collectors.joining(","));
        return "CONCAT(" + literals + ")";
      } else {
        return t;
      }
    });
  }

  public static String escapeSingleQuotes(String val) {

    return replaceWith(val, t -> !t.equals("''") ? t.replaceAll("''", "\\\\'") : t);
  }

  private static String replaceWith(String val, Function<String, String> func) {

    List<String> tokens = QuoteUtils.splitAndKeep(val, REGEX_ESCAPED_QUOTES);
    return tokens.stream().map(t -> {
      if (t.matches(REGEX_ESCAPED_QUOTES) && t.contains("''")) {
        return func.apply(t);
      } else {
        return t;
      }
    }).collect(Collectors.joining());
  }
}
