package com.asciidoc.extension.raml;

import java.util.Map;

import static raml.tools.util.LogUtil.loggedException;

public class RamlMacroAttributes {
  private static final String PATH_ATTRIBUTE = "path";
  private static final String METHOD_ATTRIBUTE = "method";
  private static final String STATUS_ATTRIBUTE = "status";
  private static final String MIME_TYPE_ATTRIBUTE = "mimeType";

  Map<String, Object> attributes;

  public RamlMacroAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public String status() {
    return getAttribute(attributes.get(STATUS_ATTRIBUTE), String.class);
  }

  public String method() {
    return requireAttribute(METHOD_ATTRIBUTE, attributes.get(METHOD_ATTRIBUTE), String.class);
  }

  public String path() {
    return requireAttribute(PATH_ATTRIBUTE, attributes.get(PATH_ATTRIBUTE), String.class);
  }

  public String mimeType() {
    return requireAttribute(MIME_TYPE_ATTRIBUTE, attributes.get(MIME_TYPE_ATTRIBUTE), String.class);
  }

  public <T> T requireAttribute(String name, Object value, Class<T> clazz) {
    if (value == null) {
      throw loggedException(new IllegalArgumentException("value is required for " + name));
    }
    return getAttribute(value, clazz);
  }

  public <T> T getAttribute(Object value, Class<T> clazz) {
    return clazz.cast(value);
  }

}
