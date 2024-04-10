package gov.va.octo.vista.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class LogSanitizer {

  public static String sanitize(String x) {
    if (StringUtils.isBlank(x)) {
      return "";
    }
    return Jsoup.clean(x, Safelist.basic());
  }
}
