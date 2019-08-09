package org.molgenis.sql;

import org.junit.BeforeClass;
import org.junit.Test;
import org.molgenis.Database;
import org.molgenis.MolgenisException;
import org.molgenis.Schema;
import org.molgenis.emx2.examples.*;
import org.molgenis.emx2.examples.synthetic.*;

import static org.molgenis.emx2.examples.CompareTools.reloadAndCompare;

public class TestRoundTripMetadataDatabase {

  static final String SCHEMA_NAME = "TestRoundTripMetadataDatabase";

  static Database database;

  @BeforeClass
  public static void setup() throws MolgenisException {
    database = DatabaseFactory.getTestDatabase("molgenis", "molgenis");
  }

  @Test
  public void testProductComponentsPartsModel() throws MolgenisException {
    Schema schema = database.createSchema(SCHEMA_NAME + "1");
    ProductComponentPartsExample.create(schema);
    reloadAndCompare(database, schema);
  }

  @Test
  public void testSimpleTypesTest() throws MolgenisException {
    Schema schema = database.createSchema(SCHEMA_NAME + "2");
    SimpleTypeTestExample.createSimpleTypeTest(schema);
    reloadAndCompare(database, schema);
  }

  @Test
  public void testArrayTypesTest() throws MolgenisException {
    Schema schema = database.createSchema(SCHEMA_NAME + "3");
    ArrayTypeTestExample.createSimpleTypeTest(schema);
    reloadAndCompare(database, schema);
  }

  @Test
  public void testRefAndRefArrayTypesTest() throws MolgenisException {
    Schema schema = database.createSchema(SCHEMA_NAME + "4");
    RefAndRefArrayTestExample.createRefAndRefArrayTestExample(schema);
    reloadAndCompare(database, schema);
  }

  @Test
  public void testCompsiteRefs() throws MolgenisException {
    Schema schema = database.createSchema(SCHEMA_NAME + "5");
    CompositeRefExample.createCompositeRefExample(schema);
    reloadAndCompare(database, schema);
  }

  @Test
  public void testCompsitePrimaryKeys() throws MolgenisException {
    Schema schema = database.createSchema(SCHEMA_NAME + "6");
    CompositePrimaryKeyExample.createCompositePrimaryExample(schema);
    reloadAndCompare(database, schema);
  }
}
