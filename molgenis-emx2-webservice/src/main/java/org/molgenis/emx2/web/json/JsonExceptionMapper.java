package org.molgenis.emx2.web.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.molgenis.emx2.MolgenisException;
import org.molgenis.emx2.web.JsonApi;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonExceptionMapper {

  private JsonExceptionMapper() {
    // hide public constructor
  }

  public static String molgenisExceptionToJson(MolgenisException e) {
    Map map = new LinkedHashMap();
    map.put("title", e.getTitle());
    map.put("message", e.getMessage());
    try {
      return JsonApi.getWriter().writeValueAsString(map);
    } catch (JsonProcessingException ex) {
      return "ERROR CONVERSION FAILED " + ex;
    }
  }
}
