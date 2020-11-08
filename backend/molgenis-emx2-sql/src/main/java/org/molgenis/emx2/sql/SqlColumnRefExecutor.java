package org.molgenis.emx2.sql;

import org.jooq.ConstraintForeignKeyOnStep;
import org.jooq.DSLContext;
import org.jooq.Name;
import org.molgenis.emx2.Column;
import org.molgenis.emx2.MolgenisException;

import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.name;
import static org.molgenis.emx2.sql.SqlTableMetadataExecutor.getJooqTable;

/** Create ref constraints. Might be composite key so therefore using Column...column parameters. */
public class SqlColumnRefExecutor {
  private SqlColumnRefExecutor() {
    // hide
  }

  public static void removeRefConstraints(DSLContext jooq, Column column) {
    Column[] columns = new Column[] {column};
    Column column1 = columns[0];
    jooq.alterTable(getJooqTable(column1.getTable()))
        .dropConstraint(getRefConstraintName(columns))
        .execute();
    jooq.execute("DROP INDEX {0}", name(column1.getSchemaName(), getIndexName(columns)));
  }

  public static void createRefConstraints(DSLContext jooq, Column refColumn) {
    validateRef(refColumn);
    Name fkeyConstraintName = name(getRefConstraintName(refColumn));
    Name thisTable = getJooqTable(refColumn.getTable()).getQualifiedName();
    List<Name> thisColumns =
        refColumn.getReferences().stream().map(c -> name(c.getName())).collect(Collectors.toList());
    List<Name> otherColumns =
        refColumn.getReferences().stream()
            .map(c -> name(c.getRefTo()))
            .collect(Collectors.toList());

    Name fkeyTable = name(refColumn.getTable().getSchema().getName(), refColumn.getRefTableName());

    ConstraintForeignKeyOnStep constraint =
        constraint(fkeyConstraintName)
            .foreignKey(thisColumns.toArray(new Name[thisColumns.size()]))
            .references(fkeyTable, otherColumns.toArray(new Name[otherColumns.size()]))
            .onUpdateCascade();
    if (refColumn.isCascadeDelete()) {
      constraint = constraint.onDeleteCascade();
    }

    jooq.alterTable(getJooqTable(refColumn.getTable())).add(constraint).execute();

    jooq.execute(
        "ALTER TABLE {0} ALTER CONSTRAINT {1} DEFERRABLE INITIALLY IMMEDIATE",
        thisTable, fkeyConstraintName);

    jooq.createIndex(getIndexName(refColumn))
        .on(thisTable, thisColumns.toArray(new Name[thisColumns.size()]))
        .execute();
  }

  static void validateRef(Column... column) {
    Column column1 = column[0];
    String refTableName = column1.getRefTableName();
    String columnNames =
        List.of(column).stream().map(c -> c.getName()).collect(Collectors.joining(","));

    // check if refTable exists
    if (refTableName == null) {
      throw new MolgenisException(
          "Create column failed",
          "Create of column(s) '" + columnNames + "' failed because RefTableName was not set");
    }

    // check if other end has primary key
    if (column1.getRefTable().getPrimaryKeys().isEmpty()) {
      throw new MolgenisException(
          "Create column failed",
          "Create of column '"
              + columnNames
              + "' failed because other table has no primary key set");
    }
  }

  private static String getIndexName(Column... column) {
    Column column1 = column[0];
    return column1.getTable().getTableName()
        + "_"
        + List.of(column).stream().map(c -> c.getName()).collect(Collectors.joining(","))
        + "_FKINDEX";
  }

  private static String getRefConstraintName(Column... column) {
    Column column1 = column[0];
    return column1.getTable().getTableName()
        + "."
        + List.of(column).stream().map(c -> c.getName()).collect(Collectors.joining(","))
        + " REFERENCES "
        + column1.getRefTableName();
  }
}
