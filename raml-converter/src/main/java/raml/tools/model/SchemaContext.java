package raml.tools.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import com.google.common.base.Optional;

import java.io.IOException;
import java.util.*;

public class SchemaContext {
  private final static String JSON_SCHEMA_DRAFT_3 = "http://json-schema.org/draft-03/schema";
  private final static String REQUIRED_KEY = "required";

  String schemaUri;
  String schemaType;
  private final Map<String, Object> schemaMap;
  String schemaName;
  String schemaDescription;
  String schemaContent;
  List<Property> properties;

  List<String> requiredProperties = new ArrayList<>();

  public SchemaContext(String schemaName, String schemaContent) {
    this.schemaName = schemaName;
    this.schemaContent = schemaContent;
    this.schemaMap = schemaAsMap(schemaContent);

    this.schemaDescription = (String) schemaMap.get("description");
    this.schemaUri = (String) schemaMap.get("$schema");
    this.schemaType = (String) schemaMap.get("type");
    this.properties = readProperties(schemaMap);
  }

  private List<Property> readProperties(Map<String, Object> schemaMap) {
    List<Property> properties = new ArrayList<>();

    Map<String, Object> propertiesMap = (Map<String, Object>) schemaMap.get("properties");

    if (propertiesMap == null) {
      return properties;
    }

    requiredProperties.addAll(getRequiredProperties(schemaMap));

    for (Map.Entry<String, Object> propertyEntry :  propertiesMap.entrySet()) {
      PropertyDefinition propertyDefinition = readPropertyDefinitionFromMap(
        propertyEntry.getKey(),
        (Map<String, Object>) propertyEntry.getValue(),
        requiredProperties
      );
      properties.add(new Property(propertyEntry.getKey(), propertyDefinition));
    }

    return properties;
  }

  private List<String> getRequiredProperties(Map<String, Object> schemaMap) {
    if (!isSchemaVersionDraft3() &&
            schemaMap.get(REQUIRED_KEY) != null &&
            List.class.isAssignableFrom(schemaMap.get(REQUIRED_KEY).getClass())) {
      return (List<String>) schemaMap.get(REQUIRED_KEY);
    } else {
      return Collections.emptyList();
    }
  }

  private boolean isSchemaVersionDraft3() {
    return schemaUri != null && schemaUri.equalsIgnoreCase(JSON_SCHEMA_DRAFT_3);
  }

  private Boolean isRequired(Map<String, Object> schemaMap) {
    if (schemaMap.get(REQUIRED_KEY) != null &&
            Boolean.class.isAssignableFrom(schemaMap.get(REQUIRED_KEY).getClass())) {
      return (Boolean) schemaMap.get(REQUIRED_KEY);
    } else {
      return false;
    }
  }

  private PropertyDefinition readPropertyDefinitionFromMap(String propertyName, Map<String, Object> propertyDefinitionMap, List<String> requiredProperties) {
    String type = (String) propertyDefinitionMap.get("type");
    String description = createDescription(propertyDefinitionMap);
    String reference = (String) propertyDefinitionMap.get("$ref");
    String targetType = (String) propertyDefinitionMap.get("targetType");

    List<String> localRequiredProperties = getRequiredProperties(propertyDefinitionMap);

    return new PropertyDefinition(description, type)
      .withName(propertyName)
      .withReference(reference)
      .withTargetType(targetType)
      .withRequiredProperties(localRequiredProperties)
      .withRequired(requiredProperties.contains(propertyName) || isRequired(propertyDefinitionMap));
  }

  private String createDescription(Map<String, Object> propertyDefinitionMap) {
    Object description = propertyDefinitionMap.get("description");
    if (description == null) {
      return null;
    }
    if (description instanceof String) {
      return (String) description;
    } else if (description instanceof List){
      return joinDescription((List) description);
    }
    throw new IllegalArgumentException("unknown type of description found");
  }

  private String joinDescription(List<String> description) {
    StringBuilder descriptionBuffer = new StringBuilder();
    for (String line: description) {
      descriptionBuffer.append(line).append("\n");
    }
    return descriptionBuffer.toString();
  }

  Map schemaAsMap(String schemaContent) {
    try {
      return new ObjectMapper().readValue(schemaContent, Map.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getSchemaName() {
    return schemaName;
  }

  public String getSchemaContent() {
    return Optional.of(schemaContent).or("").trim();
  }

  public Map getSchemaMap() {
    return schemaMap;
  }

  public String getSchemaDescription() {
    return schemaDescription;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public String getSchemaUri() {
    return schemaUri;
  }

  public String getSchemaType() {
    return schemaType;
  }

  static class Property {
    String name;
    PropertyDefinition definition;

    public Property(String name, PropertyDefinition definition) {
      this.name = name;
      this.definition = definition;
    }

    public String getName() {
      return name;
    }

    public PropertyDefinition getDefinition() {
      return definition;
    }

  }

  class PropertyDefinition {
    String description;
    List<String> requiredProperties;
    String type;
    int minimum;
    int maximum;
    String reference;
    String targetType;
    String name;
    Boolean required = false;

    public PropertyDefinition(String description, String type) {
      this.description = description;
      this.type = type;
    }

    public String getDescription() {
      return description;
    }

    public String getType() {
      return type;
    }

    public String getReference() {
      return reference;
    }

    public PropertyDefinition withReference(String reference) {
      this.reference = reference;
      return this;
    }

    public String getTargetType() {
      return targetType;
    }

    public PropertyDefinition withTargetType(String targetType) {
      this.targetType = targetType;
      return this;
    }

    public PropertyDefinition withName(String propertyName) {
      this.name = propertyName;
      return this;
    }

    public PropertyDefinition withRequiredProperties(List<String> requiredProperties) {
      this.requiredProperties = requiredProperties;
      return this;
    }

    public List<String> getRequiredProperties() {
      return requiredProperties;
    }

    public Boolean getRequired() {
      return required;
    }

    public PropertyDefinition withRequired(Boolean required) {
      this.required = required;
      return this;
    }
  }

  @Override
  public String toString() {
    return "SchemaContext{" +
      "schemaName='" + schemaName + '\'' +
      ", schemaContent='" + schemaContent + '\'' +
      '}';
  }
}
