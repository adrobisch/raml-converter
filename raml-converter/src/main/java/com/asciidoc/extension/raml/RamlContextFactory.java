package com.asciidoc.extension.raml;

import raml.tools.model.RamlContext;
import raml.tools.util.RamlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class RamlContextFactory {
  public RamlContext ramlContext(String docDir, String ramlFileName) {
    try {
      File docfile = new File(docDir + File.separatorChar + ramlFileName);
      return new RamlContext(new RamlParser().parseRaml(docfile.getParent(), new FileInputStream(docfile)));
    } catch (Exception e) {
      return new RamlContext(new RamlParser().parseRaml(classPathStream(ramlFileName)));
    }
  }

  private InputStream classPathStream(String path) {
    return RamlBlockMacro.class.getClassLoader().getResourceAsStream(path);
  }
}
