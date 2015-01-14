package raml.tools.html;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.StringTemplateSource;
import org.raml.model.*;
import raml.tools.AbstractGenerator;
import raml.tools.IoUtil;
import raml.tools.model.RamlContext;

import java.io.IOException;

public class HtmlGenerator extends AbstractGenerator {
  final Handlebars handlebars;
  private final IoUtil io;
  private String templateLocation;
  private String output = "";

  public HtmlGenerator(String templateLocation, IoUtil io) {
    this.io = io;
    this.templateLocation = templateLocation;
    this.handlebars = withHelpers(createHandleBars());
  }

  Handlebars createHandleBars() {
    Handlebars handlebars = new Handlebars();
    handlebars.setInfiniteLoops(true);
    return handlebars;
  }

  private Handlebars withHelpers(Handlebars handlebars) {
    handlebars.registerHelper("md", new MarkdownHelper());
    handlebars.registerHelper("lower", HandlebarsHelper.lowerCaseHelper());
    handlebars.registerHelper("lock", HandlebarsHelper.lockHelper());
    handlebars.registerHelper("highlight", HandlebarsHelper.highlitghHelper());
    handlebars.registerHelper("preOrLink", HandlebarsHelper.preOrLink());
    return handlebars;
  }

  @Override
  public void startTraversal(Raml raml) {
    try {
      Template template = handlebars.compile(new StringTemplateSource(
              templateLocation,
              io.contentFromFile(templateLocation))
      );
      this.output = template.apply(new RamlContext(raml));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public static HtmlGeneratorBuilder build() {
    return new HtmlGeneratorBuilder();
  }

  public String getOutput() {
    return output;
  }

}
