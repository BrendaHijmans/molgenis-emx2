package org.molgenis.emx2.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.BeforeClass;
import org.junit.Test;
import org.molgenis.emx2.Database;
import org.molgenis.emx2.Schema;
import org.molgenis.emx2.SchemaMetadata;
import org.molgenis.emx2.Table;
import org.molgenis.emx2.io.emx2.Emx2;
import org.molgenis.emx2.io.emx2.Emx2Tables;
import org.molgenis.emx2.io.tablestore.TableStoreForXlsxFile;
import org.molgenis.emx2.sql.TestDatabaseFactory;
import org.molgenis.emx2.utils.StopWatch;

/** representative import file for testing */
public class TestCohortCatalogueMultipleSchemas {

  static Database database;
  static Schema cohortsSchema;
  static Schema conceptionSchema;

  ;

  @BeforeClass
  public static void setup() {
    database = TestDatabaseFactory.getTestDatabase();
    conceptionSchema = database.dropCreateSchema("Conception");
    cohortsSchema = database.dropCreateSchema("CohortNetwork");
  }

  @Test
  public void importTest() throws IOException {
    StopWatch.print("begin");

    // load data model 2
    loadSchema("../../data/datacatalogue/_datacatalogue_schema.xlsx", cohortsSchema);
    loadSchema("../../data/datacatalogue/Cohorts.xlsx", cohortsSchema);
    loadSchema("../../data/datacatalogue/Cohorts_CoreVariables.xlsx", cohortsSchema);
    // heap space error,
    // loadSchema("../../data/datacatalogue/Cohorts_SourceVariablesAndMappings.xlsx",
    // cohortsSchema);
    assertEquals(42, TestCohortCatalogueMultipleSchemas.cohortsSchema.getTableNames().size());
  }

  @Test
  public void importTest2() throws IOException {
    // load data model
    loadSchema("../../data/datacatalogue/_datacatalogue_schema.xlsx", conceptionSchema);
    loadSchema("../../data/datacatalogue/Conception.xlsx", conceptionSchema);
    assertEquals(42, TestCohortCatalogueMultipleSchemas.conceptionSchema.getTableNames().size());
  }

  private void loadSchema(String fileName, Schema schema) throws IOException {
    File f = new File(fileName);
    Path file = f.toPath();
    if (!f.exists()) {
      ClassLoader classLoader = getClass().getClassLoader();
      file = new File(classLoader.getResource(fileName).getFile()).toPath();
    }

    TableStoreForXlsxFile store = new TableStoreForXlsxFile(file);
    if (store.containsTable("molgenis")) {
      SchemaMetadata source = Emx2.fromRowList(store.readTable("molgenis"));
      source.setDatabase(schema.getDatabase()); // enable cross links to existing data
      System.out.println(source);
      StopWatch.print("schema loaded, now creating tables");

      database.tx(
          db -> {
            schema.migrate(source);
          });
    }

    SchemaMetadata source = schema.getMetadata();
    // don't put alter in same transaction as update
    database.tx(
        db -> {
          runImportProcedure(store, source, schema);
          StopWatch.print("import of data complete");
        });

    // repeat for idempotency test (should not change anything)
    database.tx(
        db -> {
          schema.migrate(source);
        });

    database.tx(
        db -> {
          runImportProcedure(store, source, schema);
          StopWatch.print("import of data complete");
        });

    // verify export doesn't throw exceptions
    Path tempDir =
        Files.createTempDirectory(TestCohortCatalogueMultipleSchemas.class.getSimpleName());
    tempDir.toFile().deleteOnExit();

    Path excelFile = tempDir.resolve("download.xlsx");
    MolgenisIO.toExcelFile(excelFile, schema);

    Path zipFile = tempDir.resolve("download.zip");
    MolgenisIO.toZipFile(zipFile, schema);
  }

  private void runImportProcedure(
      TableStoreForXlsxFile store, SchemaMetadata source, Schema target) {
    StopWatch.print("creation of tables complete, now starting import data");
    for (String tableName : target.getTableNames()) {
      if (store.containsTable(tableName)) {
        Table table = target.getTable(tableName);
        store.processTable(table.getName(), new Emx2Tables.PkeyValidator(table.getMetadata()));

        table.update(store.readTable(tableName)); // actually upsert
      }
    }
  }
}
