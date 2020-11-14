package org.molgenis.emx2.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.molgenis.emx2.SelectColumn.s;

import org.junit.BeforeClass;
import org.junit.Test;
import org.molgenis.emx2.Database;
import org.molgenis.emx2.Query;
import org.molgenis.emx2.Schema;
import org.molgenis.emx2.examples.CrossSchemaReferenceExample;

public class TestCrossSchemaForeignKeys {
  static Schema schema1;
  static Schema schema2;
  static Database db;

  @BeforeClass
  public static void setUp() {
    db = TestDatabaseFactory.getTestDatabase();
    schema1 = db.dropCreateSchema(TestCrossSchemaForeignKeys.class.getSimpleName() + "1");
    schema2 = db.dropCreateSchema(TestCrossSchemaForeignKeys.class.getSimpleName() + "2");

    CrossSchemaReferenceExample.create(schema1, schema2);
  }

  @Test
  public void testRef() {
    Query q = schema1.getTable("Child").select(s("name"), s("parent", s("name"), s("hobby")));
    assertTrue(q.retrieveJSON().contains("stamps"));
    assertEquals("stamps", q.retrieveRows().get(0).getString("parent-hobby"));
  }

  @Test
  public void testRefArray() {
    Query q = schema2.getTable("PetLover").select(s("name"), s("pets", s("name"), s("species")));
    assertEquals("dog", q.retrieveRows().get(0).getStringArray("pets-species")[1]);

    System.out.println(q.retrieveJSON());
    assertTrue(q.retrieveJSON().contains("dog"));
  }
}
