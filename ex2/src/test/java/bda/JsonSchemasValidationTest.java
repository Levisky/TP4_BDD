package bda;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import junit.framework.TestCase;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;




public class JsonSchemasValidationTest extends TestCase {
  final static Logger logger = LoggerFactory.getLogger(JsonSchemasValidationTest.class);
  // This creates a schema factory that will use Draft 2020-12 as the default if $schema is not specified
  // in the schema data. If $schema is specified in the schema data then that schema dialect will be used
  // instead and this version is ignored.
  final static JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012, builder ->
      // This creates a mapping from $id which starts with 'source' to the retrieval URI classpath:schema/
      builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix("http://masterinfo.univ-lr.fr/bda/fs", "classpath:schemas/"))
      
  
  );

  private ObjectMapper mapper;

 


  

  public void testFile1() {
    TestCase.assertTrue(validation("http://masterinfo.univ-lr.fr/bda/fs/file.schema.json", "file1.good.json"));
  }

  public void testFile2() {
    TestCase.assertFalse(validation("http://masterinfo.univ-lr.fr/bda/fs/file.schema.json", "file2.bad.json"));
  }

  public void testFile3() {
    TestCase.assertFalse(validation("http://masterinfo.univ-lr.fr/bda/fs/file.schema.json", "file3.bad.json"));
  }

  public void testFile4() {
    TestCase.assertFalse(validation("http://masterinfo.univ-lr.fr/bda/fs/file.schema.json", "file4.bad.json"));
  }

  public void testFile5() {
    TestCase.assertFalse(validation("http://masterinfo.univ-lr.fr/bda/fs/file.schema.json", "file5.bad.json"));
  }

  public void testDir1() {
    TestCase.assertTrue(validation("http://masterinfo.univ-lr.fr/bda/fs/directory.schema.json", "dir1.good.json"));
  }

  public void testDir2() {
    TestCase.assertTrue(validation("http://masterinfo.univ-lr.fr/bda/fs/directory.schema.json", "dir2.good.json"));
  }

  public void testDir3() {
    TestCase.assertTrue(validation("http://masterinfo.univ-lr.fr/bda/fs/directory.schema.json", "dir3.good.json"));
  }

  public void testDir4() {
    TestCase.assertFalse(validation("http://masterinfo.univ-lr.fr/bda/fs/directory.schema.json", "dir4.bad.json"));
  }

  public void testDir5() {

    TestCase.assertFalse(validation("http://masterinfo.univ-lr.fr/bda/fs/directory.schema.json", "dir5.bad.json"));
  }

  public void testDir6() {
    TestCase.assertFalse(validation("http://masterinfo.univ-lr.fr/bda/fs/directory.schema.json", "dir6.bad.json"));
  }
  private boolean validation(String schemaName, String jsonFileName) {
    // Due to the mapping the schema will be retrieved from the classpath at classpath:schema/example-main.json.
    // If the schema data does not specify an $id the absolute IRI of the schema location will be used as the $id.
    JsonSchema schema = jsonSchemaFactory.getSchema(SchemaLocation.of(schemaName));

    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + jsonFileName);
    // Initialize Jackson's ObjectMapper
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
   

    JsonNode jsonNode = null;
    try {
      // read JSON file
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