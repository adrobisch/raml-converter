package raml.tools.html;

import org.apache.commons.cli.*;

public class Raml2HtmlConverterMain {
  public static void main(String[] args) {
    try {
      CommandLine parsedCommandLine = parseCommandLine(args);
      String ramlFilePath = parsedCommandLine.getOptionValue(GeneratorOptions.raml.name());
      String outputFilePath = parsedCommandLine.getOptionValue(GeneratorOptions.out.name());
      new Raml2HtmlConverter().convert(ramlFilePath, outputFilePath);
    } catch(Exception e) {
      new HelpFormatter().printHelp("raml2html", getCommandlineOptions());
    }
  }

  private static CommandLine parseCommandLine(String[] args) {
    try {
      CommandLineParser parser = new BasicParser();
      return parser.parse(getCommandlineOptions(), args);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Options getCommandlineOptions() {
    Options options = new Options();

    Option ramlPathOption = new Option(GeneratorOptions.raml.name(), true, "the raml file to generator the documentation for");
    ramlPathOption.setRequired(true);

    Option outputOptions = new Option(GeneratorOptions.out.name(), true, "the path for the output file");
    outputOptions.setRequired(true);

    options.addOption(ramlPathOption);
    options.addOption(outputOptions);
    return options;
  }

  enum GeneratorOptions {
    raml,
    out
  }

}
