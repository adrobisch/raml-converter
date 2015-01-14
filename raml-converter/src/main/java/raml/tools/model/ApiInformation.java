package raml.tools.model;

public class ApiInformation {
  String title;
  String version;
  String baseUri;

  public ApiInformation(String title, String version, String baseUri) {
    this.title = title;
    this.version = version;
    this.baseUri = baseUri;
  }

  public String getTitle() {
    return title;
  }

  public String getVersion() {
    return version;
  }

  public String getBaseUri() {
    return baseUri;
  }

  @Override
  public String toString() {
    return "ApiInformation{" +
        "title='" + title + '\'' +
        ", version='" + version + '\'' +
        ", baseUri='" + baseUri + '\'' +
        '}';
  }
}
