package raml.tools.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import java.io.IOException;
import java.util.*;

public class SchemaContext {
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

    for (Map.Entry<String, Object> propertyEntry :  propertiesMap.entrySet()) {
      PropertyDefinition propertyDefinition = readPropertyDefinitionFromMap(
        propertyEntry.getKey(),
        (Map<String, Object>) propertyEntry.getValue()
      );
      properties.add(new Property(propertyEntry.getKey(), propertyDefinition));
    }

    requiredProperties.addAll(getRequiredProperties(schemaMap));

    return properties;
  }

  private List getRequiredProperties(Map<String, Object> schemaMap) {
    if (schemaMap.get("required") != null) {
      return (List) schemaMap.get("required");
    }
    return Collections.emptyList();
  }

  private PropertyDefinition readPropertyDefinitionFromMap(String propertyName, Map<String, Object> propertyDefinitionMap) {
    String type = (String) propertyDefinitionMap.get("type");
    String description = (String) propertyDefinitionMap.get("description");
    String reference = (String) propertyDefinitionMap.get("$ref");
    String targetType = (String) propertyDefinitionMap.get("targetType");

    return new PropertyDefinition(description, type)
      .withName(propertyName)
      .withReference(reference)
      .withTargetType(targetType);
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
    String type;
    int minimum;
    int maximum;
    String reference;
    String targetType;
    String name;

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

    public boolean getRequired() {
      return requiredProperties.contains(name);
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
