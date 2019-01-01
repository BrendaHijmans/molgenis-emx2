package org.molgenis.emx2.database.internal;

import org.molgenis.emx2.*;
import org.molgenis.sql.*;

import java.util.List;

public class ColumnMetadataTable {
  final String COLUMN_METADATA_TABLE = "MOLGENIS_COLUMN_METADATA";
  final String COLUMN_TABLE = "table";
  final String COLUMN_NAME = "name";
  final String COLUMN_TYPE = "type";

  SqlDatabase backend;

  public ColumnMetadataTable(SqlDatabase backend) throws SqlDatabaseException {
    this.backend = backend;
    this.verifyBackend();
  }

  private void verifyBackend() throws SqlDatabaseException {
    SqlTable columnTable = backend.getTable(COLUMN_METADATA_TABLE);
    if (columnTable == null) columnTable = backend.createTable(COLUMN_METADATA_TABLE);
    if (columnTable.getColumn(COLUMN_TABLE) == null) {
      columnTable.addColumn(COLUMN_TABLE, SqlType.STRING);
    }
    if (columnTable.getColumn(COLUMN_NAME) == null) {
      columnTable.addColumn(COLUMN_NAME, SqlType.STRING);
    }
    columnTable.addUnique(COLUMN_TABLE, COLUMN_NAME);
  }

  public void reload(EmxModel model) throws SqlDatabaseException, EmxException {
    for (SqlRow row : backend.getQuery().from(COLUMN_METADATA_TABLE).retrieve()) {
      EmxTable t = model.getTable(row.getString(COLUMN_TABLE));
      String name = row.getString(COLUMN_NAME);
      EmxColumn c = t.getColumn(name);
      if (c == null) c = t.addColumn(name, EmxType.valueOf(row.getString(COLUMN_TYPE)));
      // TODO other attributes
    }
  }

  public void save(EmxColumn column) throws SqlDatabaseException, SqlDatabaseException {
    SqlRow tableMetadata = find(column);
    if (tableMetadata == null) tableMetadata = new SqlRow();
    tableMetadata.setString(COLUMN_TABLE, column.getTable().getName());
    tableMetadata.setString(COLUMN_NAME, column.getName());
    tableMetadata.setString(COLUMN_TYPE, column.getType().toString());
    backend.getTable(COLUMN_METADATA_TABLE).update(tableMetadata);
  }

  public void removeColumn(EmxColumn column) throws SqlDatabaseException {
    backend.getTable(COLUMN_METADATA_TABLE).delete(find(column));
  }

  public void deleteColumnsForTable(String tableName) {}

  private SqlRow find(EmxColumn column) throws SqlDatabaseException {
    List<SqlRow> rows =
        backend
            .getQuery()
            .from(COLUMN_METADATA_TABLE)
            .eq(COLUMN_METADATA_TABLE, COLUMN_NAME, column.getName())
            .eq(COLUMN_METADATA_TABLE, COLUMN_TABLE, column.getTable().getName())
            .retrieve();
    if (rows.isEmpty()) return null;
    return rows.get(0);
  }
}
