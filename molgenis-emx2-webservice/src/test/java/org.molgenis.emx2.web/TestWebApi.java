package org.molgenis.emx2.web;

import org.molgenis.*;
import org.molgenis.sql.DatabaseFactory;

import static org.molgenis.Type.*;

public class TestWebApi {

  public static void main(String[] args) throws MolgenisException {
    Database db = DatabaseFactory.getTestDatabase("molgenis", "molgenis");

    Schema schema = db.createSchema("store");

    Table categoryTable = schema.createTableIfNotExists("Category");
    categoryTable.addColumn("name").unique();

    Table tagTable = schema.createTableIfNotExists("Tag");
    tagTable.addColumn("name").unique();

    Table petTable = schema.createTableIfNotExists("Pet");
    petTable.addColumn("name").unique();
    petTable.addRef("category").to("Category").nullable(true);
    petTable.addColumn("photoUrls", STRING_ARRAY);
    petTable.addColumn("status"); // todo enum: available, pending, sold
    petTable.addRefArray("tags").to("Tag");

    Table userTable = schema.createTableIfNotExists("User");
    userTable.addColumn("username").unique();
    userTable.addColumn("firstName");
    userTable.addColumn("lastName");
    userTable.addColumn("email"); // todo: validation email
    userTable.addColumn("password"); // todo: password type?
    userTable.addColumn("phone"); // todo: validation phon
    userTable.addColumn("userStatus", INT);

    Table orderTable = schema.createTableIfNotExists("Order");
    orderTable.addRef("petId").to("Pet", "name");
    orderTable.addColumn("quantity", INT); // todo: validation >=1
    orderTable.addColumn("complete", BOOL); // todo: default false
    orderTable.addColumn("status"); // todo enum: placed, approved, delivered

    new WebApi(db);
  }
}
