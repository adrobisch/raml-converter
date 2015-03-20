package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;
import raml.tools.model.RamlContext;
import raml.tools.util.RamlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RamlContextFactory {

  public RamlContext ramlContext(AbstractBlock parentBlock, String ramlFileName) {
    String docDir = getDocDirAttribute(parentBlock);

    if (docDir == null || docDir.isEmpty()) {
      return ramlContext(ramlFileName);
    } else {
      File docfile = new File(docDir + File.separatorChar + ramlFileName);
      return ramlContext(docfile.getParent(), docfile.getName());
    }
  }

  protected String getDocDirAttribute(AbstractBlock parentBlock) {
    try {
      return parentBlock.document().attributes().get("docdir").toString();
    } catch (Exception unableToGetAttribute) {
      return null;
    }
  }

  public RamlContext ramlContext(String docDir, String ramlFileName) {
    File docfile = new File(docDir + File.separatorChar + ramlFileName);
    try {
      return new RamlContext(new RamlParser().parseRaml(docfile.getParent(), new FileInputStream(docfile)));
    } catch (FileNotFoundException fileNotFound) {
      throw new RuntimeException(fileNotFound);
    }
  }

  public RamlContext ramlContext(String classPathResource) {
    return new RamlContext(new RamlParser().parseRaml(classPathStream(classPathResource)));
  }

  protected InputStream classPathStream(String path) {
    return RamlBlockMacro.class.getClassLoader().getResourceAsStream(path);
  }
}
