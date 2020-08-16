package org.molgenis.emx2.sql;

import org.junit.BeforeClass;
import org.junit.Test;
import org.molgenis.emx2.*;

import java.io.File;

import static org.junit.Assert.*;
import static org.molgenis.emx2.Column.column;
import static org.molgenis.emx2.ColumnType.FILE;
import static org.molgenis.emx2.SelectColumn.s;
import static org.molgenis.emx2.TableMetadata.table;

public class TestFileType {
  private static Database db;
  private static Schema schema;

  @BeforeClass
  public static void setUp() {
    db = TestDatabaseFactory.getTestDatabase();
    schema = db.dropCreateSchema(TestFileType.class.getSimpleName());
  }

  @Test
  public void test3() {
    Table t = schema.create(table("test1", column("id").pkey(), column("image").type(FILE)));
    File image = getFile();
    t.insert(new Row("id", 1, "image", image));

    assertEquals(
        (Integer) 37458,
        t.query().select(s("image", s("size"))).retrieveRows().get(0).getInteger("image-size"));

    String result =
        t.query().select(s("image", s("size"), s("extension"), s("mimetype"))).retrieveJSON();
    System.out.println(result);
    assertTrue(result.contains("37458"));
  }

  @Test
  public void testBinaryFileWrapper() {
    BinaryFileWrapper w = new BinaryFileWrapper(getFile());
    assertEquals("image/png", w.getMimeType());
    assertEquals("png", w.getExtension());
    assertEquals(37458, w.getSize());
    assertNotNull(w.getContents());
    assertEquals(37458, w.getContents().length);
  }

  @Test
  public void testRowWithFileType() {
    Row r = new Row("image", getFile());
    assertEquals("png", r.getString("image-extension"));
  }

  private File getFile() {
    ClassLoader classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource("testfiles/molgenis.png").getFile());
  }
}
