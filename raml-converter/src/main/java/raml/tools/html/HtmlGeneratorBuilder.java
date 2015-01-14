package raml.tools.html;

import raml.tools.IoUtil;

public class HtmlGeneratorBuilder {
  String templateName;
  IoUtil ioUtil;

  public HtmlGeneratorBuilder withTemplate(String templateName) {
    this.templateName = templateName;
    return this;
  }

  public HtmlGeneratorBuilder withIo(IoUtil ioUtil) {
    this.ioUtil = ioUtil;
    return this;
  }

  public HtmlGenerator build() {
    if (ioUtil == null) {
      ioUtil = new IoUtil();
    }

    if (templateName == null) {
      templateName = "template.hbs";
    }

    return new HtmlGenerator(templateName, ioUtil);
  }
}
