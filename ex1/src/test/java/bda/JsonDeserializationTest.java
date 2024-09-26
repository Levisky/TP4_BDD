package bda;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import junit.framework.TestCase;
import myfs.Directory;
import myfs.File;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class JsonDeserializationTest extends TestCase {
  final static Logger logger = LoggerFactory.getLogger(JsonDeserializationTest.class);
  final static ObjectMapper mapper = new ObjectMapper();

  @Before
  public void setUp() {
    mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
  }

  public void testFile1() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "file1.good.json");
    File file = mapper.readValue(jsonInputStream, File.class);
    assertEquals("file1", file.getName());
    assertEquals(120, file.getSize());
    logger.info("file1.good.json: deserialization OK");
  }

  public void testFile2() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "file2.bad.json");
    try {
      mapper.readValue(jsonInputStream, File.class);
      fail();
    } catch (InvalidTypeIdException ite) {
      assertTrue(true);
    }
    logger.info("file2.bad.json: deserialization problem");
  }

  public void testFile3() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "file3.bad.json");
    try {
      mapper.readValue(jsonInputStream, File.class);
      fail();
    } catch (MismatchedInputException mie) {
      assertTrue(true);
    }
    logger.info("file3.bad.json: deserialization problem");
  }

  public void testFile4() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "file4.bad.json");
    try {
      mapper.readValue(jsonInputStream, File.class);
      fail();
    } catch (UnrecognizedPropertyException upe) {
      assertTrue(true);
    }
    logger.info("file4.bad.json: deserialization problem");
  }

  public void testFile5() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "file5.bad.json");
    try {
      mapper.readValue(jsonInputStream, File.class);
      fail();
    } catch (JsonParseException jpe) {
      assertTrue(true);
    }
    logger.info("file5.bad.json: deserialization problem");
  }

  public void testDir1() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "dir1.good.json");
    Directory dir = mapper.readValue(jsonInputStream, Directory.class);
    assertEquals("dir1", dir.getName());
    assertEquals(1, dir.getNodes().size());
    assertEquals(File.class, dir.getNodes().get(0).getClass());
    File file = (File) dir.getNodes().get(0);
    assertEquals("file1", file.getName());
    assertEquals(120, file.getSize());
    logger.info("dir1.good.json: deserialization OK");
  }

  public void testDir2() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "dir2.good.json");
    Directory dir1 = mapper.readValue(jsonInputStream, Directory.class);
    assertEquals("dir1", dir1.getName());
    assertEquals(1, dir1.getNodes().size());
    assertEquals(Directory.class, dir1.getNodes().get(0).getClass());
    Directory dir2 = (Directory) dir1.getNodes().get(0);
    assertEquals("dir2", dir2.getName());
    assertEquals(0, dir2.getNodes().size());
    logger.info("dir2.good.json: deserialization OK");
  }

  public void testDir3() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "dir3.good.json");
    Directory dir1 = mapper.readValue(jsonInputStream, Directory.class);
    assertEquals("dir1", dir1.getName());
    assertEquals(2, dir1.getNodes().size());
    // first node
    assertEquals(Directory.class, dir1.getNodes().get(0).getClass());
    Directory dir2 = (Directory) dir1.getNodes().get(0);
    assertEquals("dir2", dir2.getName());
    assertEquals(1, dir2.getNodes().size());
    assertEquals(File.class, dir2.getNodes().get(0).getClass());
    File file2 = (File) dir2.getNodes().get(0);
    assertEquals("file2", file2.getName());
    assertEquals(120, file2.getSize());
    // second node
    assertEquals(File.class, dir1.getNodes().get(1).getClass());
    File file1 = (File) dir1.getNodes().get(1);
    assertEquals("file1", file1.getName());
    assertEquals(100, file1.getSize());

    logger.info("dir3.good.json: deserialization OK");
  }

  public void testDir4() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "dir4.bad.json");
    try {
      mapper.readValue(jsonInputStream, Directory.class);
      fail();
    } catch (UnrecognizedPropertyException upe) {
      assertTrue(true);
    }
    logger.info("dir4.bad.json: deserialization problem");
  }

  public void testDir5() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "dir5.bad.json");
    try {
      mapper.readValue(jsonInputStream, Directory.class);
      fail();
    } catch (UnrecognizedPropertyException upe) {
      assertTrue(true);
    }
    logger.info("dir5.bad.json: deserialization problem");
  }

  public void testDir6() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "dir6.bad.json");
    try {
      mapper.readValue(jsonInputStream, Directory.class);
      fail();
    } catch (InvalidTypeIdException ite) {
      assertTrue(true);
    }
    logger.info("dir6.bad.json: deserialization problem");
  }

  public void testDir7() throws IOException {
    InputStream jsonInputStream = this.getClass().getResourceAsStream("/files/" + "dir7.bad.json");
    try {
      mapper.readValue(jsonInputStream, Directory.class);
      fail();
    } catch (MismatchedInputException mie) {
      assertTrue(true);
    }
    logger.info("dir7.bad.json: deserialization problem");
  }
}
