package org.molgenis.emx2.sql;

import org.junit.BeforeClass;
import org.junit.Test;
import org.molgenis.emx2.Database;
import org.molgenis.emx2.Row;
import org.molgenis.emx2.Table;
import org.molgenis.emx2.ColumnType;
import org.molgenis.emx2.Schema;

import java.util.Arrays;

import static junit.framework.TestCase.fail;
import static org.molgenis.emx2.Column.column;
import static org.molgenis.emx2.ColumnType.REF_ARRAY;
import static org.molgenis.emx2.TableMetadata.table;

public class TestCreateForeignKeysArrays {
  private static Database db;

  @BeforeClass
  public static void setup() {
    db = TestDatabaseFactory.getTestDatabase();
  }

  @Test
  public void testUUID() {
    executeTest(
        ColumnType.UUID,
        new java.util.UUID[] {
          java.util.UUID.fromString("f83133cc-aeaa-11e9-a2a3-2a2ae2dbcce4"),
          java.util.UUID.fromString("f83133cc-aeaa-11e9-a2a3-2a2ae2dbcce5"),
          java.util.UUID.fromString("f83133cc-aeaa-11e9-a2a3-2a2ae2dbcce6")
        });
  }

  @Test
  public void testStringRef() {
    executeTest(ColumnType.STRING, new String[] {"aap", "noot", "mies"});
  }

  @Test
  public void testIntRef() {
    executeTest(ColumnType.INT, new Integer[] {5, 6});
  }

  @Test
  public void testDateRef() {
    executeTest(ColumnType.DATE, new String[] {"2013-01-01", "2013-01-02", "2013-01-03"});
  }

  @Test
  public void testDateTimeRef() {
    executeTest(
        ColumnType.DATETIME,
        new String[] {"2013-01-01T18:00:00", "2013-01-01T18:00:01", "2013-01-01T18:00:02"});
  }

  @Test
  public void testDecimalRef() {
    executeTest(ColumnType.DECIMAL, new Double[] {5.0, 6.0, 7.0});
  }

  @Test
  public void testTextRef() {
    executeTest(
        ColumnType.TEXT,
        new String[] {
          "This is a hello world", "This is a hello back to you", "This is a hello some more"
        });
  }

  private void executeTest(ColumnType columnType, Object[] testValues) {

    Schema schema = db.createSchema("TestRefArray" + columnType.toString().toUpperCase());

    String aKey = "A" + columnType + "Key";
    Table aTable = schema.create(table("A").add(column(aKey).type(columnType)).pkey(aKey));

    Row aRow = new Row().set(aKey, testValues[0]);
    Row aRow2 = new Row().set(aKey, testValues[1]);
    aTable.insert(aRow, aRow2);

    String refToA = columnType + "RefToA";
    Table bTable =
        schema.create(
            table("B")
                .add(column(refToA).type(REF_ARRAY).refTable("A").refColumn(aKey))
                .add(
                    column(refToA + "Nullable")
                        .type(REF_ARRAY)
                        .refTable("A")
                        .refColumn(aKey)
                        .nullable(true)));

    // error on insert of faulty fkey
    Row bErrorRow = new Row().set(refToA, Arrays.copyOfRange(testValues, 1, 3));
    try {
      bTable.insert(bErrorRow);
      fail("insert should fail because value is missing");
    } catch (Exception e) {
      System.out.println("insert exception correct: \n" + e.getMessage());
    }

    // okay
    Row bRow = new Row().set(refToA, Arrays.copyOfRange(testValues, 0, 2));
    bTable.insert(bRow);

    // delete of A should fail
    try {
      aTable.delete(aRow);
      fail("delete should fail");
    } catch (Exception e) {
      System.out.println("delete exception correct: \n" + e.getMessage());
    }

    // should be okay
    bTable.delete(bRow);
    aTable.delete(aRow);
  }
}
