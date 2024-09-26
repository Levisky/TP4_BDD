package bda;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import junit.framework.TestCase;
import myfs.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;


public class JsonSchemasGenerationTest extends TestCase {
  final static Logger logger = LoggerFactory.getLogger(JsonSchemasGenerationTest.class);

  // This creates a schema factory that will use Draft 2020-12 as the default if $schema is not specified
  // in the schema data. If $schema is specified in the schema data then that schema dialect will be used
  // instead and this version is ignored.
  final static JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012, builder ->
      // This creates a mapping from $id which starts with 'source' to the retrieval URI classpath:schemas/
      builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix("http://masterinfo.univ-lr.fr/bda/fs", "classpath:schemas/"))
  );

  // schema generation
  final static JsonSchema schema = JsonSchemaGeneration();

  private static JsonSchema JsonSchemaGeneration() {
    try {
      JacksonModule jacksonModule = new JacksonModule();
      SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
          .with(jacksonModule)
         .with(Option.DEFINITIONS_FOR_ALL_OBJECTS,
              Option.DEFINITIONS_FOR_MEMBER_SUPERTYPES,
              Option.DEFINITION_FOR_MAIN_SCHEMA,
              Option.FORBIDDEN_ADDITIONAL_PROPERTIES_BY_DEFAULT);
      SchemaGeneratorConfig config = configBuilder.build();
      SchemaGenerator generator = new SchemaGenerator(config);
      // root class from which the schema will be generated
      JsonNode jsonSchema = generator.generateSchema(Node.class);

      ObjectMapper om = new ObjectMapper();
      JsonFactory factory = new JsonFactory();
      String currentPath = System.getProperty("user.dir");
      Path schemasDirPath = Paths.get(currentPath + "/src/test/resources/schemas");
      if (Files.exists(schemasDirPath)) {
        String schemaDirPath = schemasDirPath.toString();
        File generatedSchema = new File(schemaDirPath + "/generatedSchema.json");
        JsonGenerator jsonGenerator = factory.createGenerator(generatedSchema, JsonEncoding.UTF8);
        om.writeTree(jsonGenerator, jsonSchema);
        //System.out.println(jsonSchema.toPrettyString());

        JsonSchema jsonGenSchema = jsonSchemaFactory.getSchema(new URI("http://masterinfo.univ-lr.fr/bda/fs"), jsonSchema);

        return jsonGenSchema;
      } else {
        throw new RuntimeException("schemas directory not found");
      }

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void testFile1() {
    TestCase.assertTrue(validation("file1.good.json"));
  }

  public void testFile2() {
    TestCase.assertFalse(validation( "file2.bad.json"));
  }

  public void testFile3() {
    TestCase.assertFalse(validation("file3.bad.json"));
  }

  public void testFile4() {
    TestCase.assertFalse(validation("file4.bad.json"));
  }

  public void testFile5() {
    TestCase.assertFalse(validation("file5.bad.json"));
  }

  public void testDir1() {
    TestCase.assertTrue(validation( "dir1.good.json"));
  }

  public void testDir2() {
    TestCase.assertTrue(validation("dir2.good.json"));
  }

  public void testDir3() {
    TestCase.assertTrue(validation( "dir3.good.json"));
  }

  public void testDir4() {
    TestCase.assertFalse(validation( "dir4.bad.json"));
  }

  public void testDir5() {
    TestCase.assertFalse(validation("dir5.bad.json"));
  }

  public void testDir6() {
    TestCase.assertFalse(validation("dir6.bad.json"));
  }

  public void testDir7() {
    TestCase.assertFalse(validation("dir7.bad.json"));
  }

  private boolean validation(String jsonFileName) {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + jsonFileName);
    // Initialize Jackson's ObjectMapper
    ObjectMapper mapper = new ObjectMapper();

    JsonNode jsonNode = null;
    try {
      jsonNode = mapper.readTree(jsonInputStream);
    } catch (IOException ioe) {
      logger.error(ioe.getMessage());
      if (jsonNode == null) {
        return false;
      }
    }
    // validate JSON content against schema
    Set<ValidationMessage> assertions = schema.validate(jsonNode);
    // checks the validation result
    return checkAssertions(assertions, jsonFileName);
  }

  private boolean checkAssertions(Set<ValidationMessage> assertions, String jsonFileName) {
    if (! assertions.isEmpty()) {
      for (ValidationMessage msg : assertions) {
        logger.error(jsonFileName + ": " + msg.getMessage());
      }
      logger.error(jsonFileName + ": invalid JSON content.");
      return false;
    } else {
      logger.info(jsonFileName + ": valid JSON content.");
      return true;
    }
  }

}