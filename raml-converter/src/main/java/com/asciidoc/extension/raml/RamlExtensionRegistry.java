package com.asciidoc.extension.raml;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.spi.ExtensionRegistry;

public class RamlExtensionRegistry implements ExtensionRegistry {
  @Override
  public void register(Asciidoctor asciidoctor) {
    asciidoctor.javaExtensionRegistry().blockMacro("raml_resource", RamlResourceMacro.class);
    asciidoctor.javaExtensionRegistry().blockMacro("raml_headers", RamlHeadersMacro.class);
    asciidoctor.javaExtensionRegistry().blockMacro("raml_example", RamlExampleMacro.class);
    asciidoctor.javaExtensionRegistry().blockMacro("raml_schema", RamlSchemaMacro.class);
    asciidoctor.javaExtensionRegistry().blockMacro("raml_statuses", RamlStatusesMacro.class);
  }
}
