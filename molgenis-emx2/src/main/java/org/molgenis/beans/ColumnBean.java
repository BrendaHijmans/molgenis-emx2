package org.molgenis.beans;

import org.molgenis.Column;
import org.molgenis.MolgenisException;
import org.molgenis.Table;

public class ColumnBean implements Column {
  private Table table;
  private String name;
  private Type type;
  private boolean nullable;
  private String refTable;
  private String refColumn;
  private String mrefTable;
  private String mrefBack;
  private boolean readonly;
  private String visible;
  private String description;
  private String validation;
  private String defaultValue;

  //    @Id private String name;
  //    private EmxTable table;
  //    private EmxType type = EmxType.STRING;
  //    private Boolean nillable = false;
  //    private Boolean readonly = false;
  //    private Boolean unique = false;
  //    private String defaultValue;
  //    private String description;
  //    private String validation;
  //    private String visible;
  //    private EmxTable ref;
  //    private EmxTable joinTable;
  //    private EmxColumn joinColumn;

  public ColumnBean(String name) {
    this.name = name;
    this.type = Type.STRING;
  }

  public ColumnBean(Table table, String name, Type type) {
    this.table = table;
    this.name = name;
    this.type = type;
  }

  public ColumnBean(Table table, String name, String otherTable, String otherColumn) {
    this.table = table;
    this.name = name;
    this.type = Type.REF;
    this.refTable = otherTable;
    this.refColumn = otherColumn;
  }

  public ColumnBean(
      Table table, String name, String otherTable, String mrefTable, String mrefBack) {
    this.table = table;
    this.name = name;
    this.type = Type.MREF;
    this.refTable = otherTable;
    this.mrefTable = mrefTable;
    this.mrefBack = mrefBack;
  }

  @Override
  public Column addColumn(String name, Type type) throws MolgenisException {
    return this.getTable().addColumn(name, type);
  }

  @Override
  public Table getTable() {
    return table;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Boolean isNullable() {
    return nullable;
  }

  @Override
  public String getRefTable() {
    return refTable;
  }

  @Override
  public Column setRefTable(String table) {
    this.refTable = table;
    return this;
  }

  @Override
  public String getRefColumn() {
    return refColumn;
  }

  @Override
  public Column setRefColumn(String columnName) {
    this.refColumn = columnName;
    return this;
  }

  @Override
  public Column setRef(String tableName, String columnName) {
    this.setRefTable(tableName);
    this.setRefColumn(columnName);
    return this;
  }

  @Override
  public String getMrefTable() {
    return this.mrefTable;
  }

  @Override
  public String getMrefBack() {
    return mrefBack;
  }

  @Override
  public Column setNullable(boolean nillable) throws MolgenisException {
    this.nullable = nillable;
    return this;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getName()).append(" ");
    if (Type.REF.equals(getType())) builder.append("ref(").append(refTable).append(")");
    else builder.append(getType().toString().toLowerCase());
    if (isNullable()) builder.append(" nullable");
    return builder.toString();
  }

  @Override
  public boolean isReadonly() {
    return readonly;
  }

  @Override
  public void setReadonly(boolean readonly) {
    this.readonly = readonly;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getVisible() {
    return visible;
  }

  @Override
  public void setVisible(String visible) {
    this.visible = visible;
  }

  @Override
  public String getValidation() {
    return validation;
  }

  @Override
  public void setValidation(String validation) {
    this.validation = validation;
  }

  @Override
  public boolean isUnique() {
    return getTable().isUnique(getName());
  }

  @Override
  public String getDefaultValue() {
    return defaultValue;
  }

  @Override
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
}
