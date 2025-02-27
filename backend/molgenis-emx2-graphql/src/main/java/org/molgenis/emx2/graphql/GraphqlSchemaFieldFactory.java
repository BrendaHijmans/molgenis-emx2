package org.molgenis.emx2.graphql;

import static org.molgenis.emx2.Constants.*;
import static org.molgenis.emx2.graphql.GraphqlApiMutationResult.Status.SUCCESS;
import static org.molgenis.emx2.graphql.GraphqlApiMutationResult.typeForMutationResult;
import static org.molgenis.emx2.graphql.GraphqlConstants.*;
import static org.molgenis.emx2.graphql.GraphqlConstants.INHERITED;
import static org.molgenis.emx2.graphql.GraphqlConstants.KEY;
import static org.molgenis.emx2.json.JsonUtil.jsonToSchema;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.Scalars;
import graphql.schema.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.molgenis.emx2.*;
import org.molgenis.emx2.json.JsonUtil;

public class GraphqlSchemaFieldFactory {

  static final GraphQLInputObjectType inputSettingsMetadataType =
      new GraphQLInputObjectType.Builder()
          .name("MolgenisSettingsInput")
          .field(
              GraphQLInputObjectField.newInputObjectField().name(KEY).type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField().name(VALUE).type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField().name(TABLE).type(Scalars.GraphQLString))
          .build();
  static final GraphQLType outputSettingsMetadataType =
      new GraphQLObjectType.Builder()
          .name("MolgenisSettingsType")
          .field(GraphQLFieldDefinition.newFieldDefinition().name(KEY).type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.VALUE)
                  .type(Scalars.GraphQLString))
          .build();
  private static final GraphQLInputObjectType inputDropColumnType =
      new GraphQLInputObjectType.Builder()
          .name("DropColumnInput")
          .field(
              GraphQLInputObjectField.newInputObjectField().name(TABLE).type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(COLUMN)
                  .type(Scalars.GraphQLString))
          .build();
  private static final GraphQLInputObjectType inputDropSettingType =
      new GraphQLInputObjectType.Builder()
          .name("DropSettingsInput")
          .field(
              GraphQLInputObjectField.newInputObjectField().name(TABLE).type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField().name(KEY).type(Scalars.GraphQLString))
          .build();
  // medatadata
  private static final GraphQLType outputRolesMetadataType =
      new GraphQLObjectType.Builder()
          .name("MolgenisRolesType")
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.NAME)
                  .type(Scalars.GraphQLString))
          .build();
  private static final GraphQLType outputMembersMetadataType =
      new GraphQLObjectType.Builder()
          .name("MolgenisMembersType")
          .field(
              GraphQLFieldDefinition.newFieldDefinition().name(EMAIL).type(Scalars.GraphQLString))
          .field(GraphQLFieldDefinition.newFieldDefinition().name(ROLE).type(Scalars.GraphQLString))
          .build();
  private static final GraphQLObjectType outputColumnMetadataType =
      new GraphQLObjectType.Builder()
          .name("MolgenisColumnType")
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(DESCRIPTION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(COLUMN_POSITION)
                  .type(Scalars.GraphQLInt))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(COLUMN_TYPE)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(COLUMN_FORMAT)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(INHERITED)
                  .type(Scalars.GraphQLBoolean))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(Constants.KEY)
                  .type(Scalars.GraphQLInt))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(REQUIRED)
                  .type(Scalars.GraphQLBoolean))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(REF_SCHEMA_NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(REF_TABLE_NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(REF_LINK)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(REF_BACK)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(REF_LABEL)
                  .type(Scalars.GraphQLString))
          // TODO
          //          .field(
          //              GraphQLFieldDefinition.newFieldDefinition()
          //                  .name(CASCADE_DELETE)
          //                  .type(Scalars.GraphQLBoolean))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(VALIDATION_EXPRESSION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(VISIBLE_EXPRESSION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(SEMANTICS)
                  .type(GraphQLList.list(Scalars.GraphQLString)))
          .build();
  private static final GraphQLObjectType outputTableMetadataType =
      new GraphQLObjectType.Builder()
          .name("MolgenisTableType")
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.EXTERNAL_SCHEMA)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.INHERIT)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(DESCRIPTION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.COLUMNS)
                  .type(GraphQLList.list(outputColumnMetadataType)))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(SETTINGS)
                  .type(GraphQLList.list(outputSettingsMetadataType)))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(SEMANTICS)
                  .type(GraphQLList.list(Scalars.GraphQLString)))
          .build();
  private static final GraphQLObjectType outputMetadataType =
      new GraphQLObjectType.Builder()
          .name("MolgenisMetaType")
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.TABLES)
                  .type(GraphQLList.list(outputTableMetadataType)))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name(GraphqlConstants.MEMBERS)
                  .type(GraphQLList.list(outputMembersMetadataType)))
          .field(
              GraphQLFieldDefinition.newFieldDefinition()
                  .name("roles")
                  .type(GraphQLList.list(outputRolesMetadataType)))
          .build();
  private final GraphQLInputObjectType inputMembersMetadataType =
      new GraphQLInputObjectType.Builder()
          .name("MolgenisMembersInput")
          .field(
              GraphQLInputObjectField.newInputObjectField().name(EMAIL).type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField().name(ROLE).type(Scalars.GraphQLString))
          .build();
  private GraphQLInputObjectType inputColumnMetadataType =
      new GraphQLInputObjectType.Builder()
          .name("MolgenisColumnInput")
          .field(
              GraphQLInputObjectField.newInputObjectField().name(TABLE).type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(GraphqlConstants.NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(COLUMN_TYPE)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(COLUMN_POSITION)
                  .type(Scalars.GraphQLInt))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(COLUMN_FORMAT)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(Constants.KEY)
                  .type(Scalars.GraphQLInt))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(REQUIRED)
                  .type(Scalars.GraphQLBoolean))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(REF_SCHEMA_NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(REF_TABLE_NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(REF_LINK)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(REF_BACK)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(OLD_NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(INHERITED)
                  .type(Scalars.GraphQLBoolean))
          // TODO
          //          .field(
          //              GraphQLInputObjectField.newInputObjectField()
          //                  .name(CASCADE_DELETE)
          //                  .type(Scalars.GraphQLBoolean))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(DESCRIPTION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(VALIDATION_EXPRESSION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(VISIBLE_EXPRESSION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(SEMANTICS)
                  .type(GraphQLList.list(Scalars.GraphQLString)))
          .field(
              GraphQLInputObjectField.newInputObjectField().name(DROP).type(Scalars.GraphQLBoolean))
          .build();
  private final GraphQLInputObjectType inputTableMetadataType =
      new GraphQLInputObjectType.Builder()
          .name("MolgenisTableInput")
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(GraphqlConstants.NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(OLD_NAME)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField().name(DROP).type(Scalars.GraphQLBoolean))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(INHERIT)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(DESCRIPTION)
                  .type(Scalars.GraphQLString))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(SEMANTICS)
                  .type(GraphQLList.list(Scalars.GraphQLString)))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(GraphqlConstants.COLUMNS)
                  .type(GraphQLList.list(inputColumnMetadataType)))
          .field(
              GraphQLInputObjectField.newInputObjectField()
                  .name(SETTINGS)
                  .type(GraphQLList.list(inputSettingsMetadataType)))
          .build();

  public GraphqlSchemaFieldFactory() {
    // hide constructor
  }

  private static DataFetcher<?> queryFetcher(Schema schema) {
    return dataFetchingEnvironment -> {

      // add tables
      String json = JsonUtil.schemaToJson(schema.getMetadata());
      Map<String, Object> result = new ObjectMapper().readValue(json, Map.class);

      // add members
      List<Map<String, String>> members = new ArrayList<>();
      for (Member m : schema.getMembers()) {
        members.add(Map.of("email", m.getUser(), "role", m.getRole()));
      }
      result.put(MEMBERS, members);

      // add roles
      List<Map<String, String>> roles = new ArrayList<>();
      for (String role : schema.getRoles()) {
        roles.add(Map.of(GraphqlConstants.NAME, role));
      }
      result.put("roles", roles);

      result.put("name", schema.getMetadata().getName());
      return result;
    };
  }

  private static DataFetcher<?> dropFetcher(Schema schema) {
    return dataFetchingEnvironment -> {
      StringBuilder message = new StringBuilder();
      schema.tx(
          db -> {
            dropTables(schema, dataFetchingEnvironment, message);
            dropMembers(schema, dataFetchingEnvironment, message);
            dropColumns(schema, dataFetchingEnvironment, message);
            dropSettings(schema, dataFetchingEnvironment, message);
          });
      Map result = new LinkedHashMap<>();
      result.put(GraphqlConstants.DETAIL, message.toString());
      return result;
    };
  }

  private static void dropColumns(
      Schema schema, DataFetchingEnvironment dataFetchingEnvironment, StringBuilder message) {
    List<Map> columns = dataFetchingEnvironment.getArgument(GraphqlConstants.COLUMNS);
    if (columns != null) {
      for (Map col : columns) {
        schema
            .getMetadata()
            .getTableMetadata((String) col.get(TABLE))
            .dropColumn((String) col.get(COLUMN));
        message.append("Dropped column '" + col.get(TABLE) + "." + col.get(COLUMN) + "'\n");
      }
    }
  }

  private static void dropMembers(
      Schema schema, DataFetchingEnvironment dataFetchingEnvironment, StringBuilder message) {
    List<String> members = dataFetchingEnvironment.getArgument(GraphqlConstants.MEMBERS);
    if (members != null) {
      for (String name : members) {
        schema.removeMember(name);
        message.append("Dropped member '" + name + "'\n");
      }
    }
  }

  private static void dropSettings(
      Schema schema, DataFetchingEnvironment dataFetchingEnvironment, StringBuilder message) {
    List<Map<String, String>> settings = dataFetchingEnvironment.getArgument(SETTINGS);
    if (settings != null) {
      for (Map<String, String> setting : settings) {
        if (setting.get("table") != null) {
          Table table = schema.getTable(setting.get("table"));
          if (table == null) {
            throw new MolgenisException(
                "Cannot remove setting because table " + setting.get("table") + " does not exist");
          }
          table.getMetadata().removeSetting(setting.get("key"));
        } else {
          schema.getMetadata().removeSetting(setting.get("key"));
          message.append("Removed schema setting '" + (setting.get("key")) + "'\n");
        }
      }
    }
  }

  private static void dropTables(
      Schema schema, DataFetchingEnvironment dataFetchingEnvironment, StringBuilder message) {
    List<String> tables = dataFetchingEnvironment.getArgument(GraphqlConstants.TABLES);
    if (tables != null) {
      for (String tableName : tables) {
        schema.dropTable(tableName);
        message.append("Dropped table '" + tableName + "'\n");
      }
    }
  }

  public GraphQLFieldDefinition.Builder schemaQuery(Schema schema) {
    return GraphQLFieldDefinition.newFieldDefinition()
        .name("_schema")
        .type(outputMetadataType)
        .dataFetcher(GraphqlSchemaFieldFactory.queryFetcher(schema));
  }

  public GraphQLFieldDefinition.Builder settingsQuery(Schema schema) {
    return GraphQLFieldDefinition.newFieldDefinition()
        .name("_settings")
        .type(GraphQLList.list(outputSettingsMetadataType))
        .dataFetcher(
            dataFetchingEnvironment ->
                // add settings
                schema.getMetadata().getSettings().stream()
                    .map(entry -> Map.of("key", entry.getKey(), VALUE, entry.getValue()))
                    .collect(Collectors.toList()));
  }

  public GraphQLFieldDefinition changeMutation(Schema schema) {
    return GraphQLFieldDefinition.newFieldDefinition()
        .name("change")
        .type(typeForMutationResult)
        .dataFetcher(changeFetcher(schema))
        .argument(
            GraphQLArgument.newArgument()
                .name(GraphqlConstants.TABLES)
                .type(GraphQLList.list(inputTableMetadataType)))
        .argument(
            GraphQLArgument.newArgument()
                .name(GraphqlConstants.MEMBERS)
                .type(GraphQLList.list(inputMembersMetadataType)))
        .argument(
            GraphQLArgument.newArgument()
                .name(GraphqlConstants.SETTINGS)
                .type(GraphQLList.list(inputSettingsMetadataType)))
        .argument(
            GraphQLArgument.newArgument()
                .name(GraphqlConstants.COLUMNS)
                .type(GraphQLList.list(inputColumnMetadataType)))
        .build();
  }

  private DataFetcher<?> changeFetcher(Schema schema) {
    return dataFetchingEnvironment -> {
      schema.tx(
          db -> {
            try {
              changeTables(schema, dataFetchingEnvironment);
              changeMembers(schema, dataFetchingEnvironment);
              changeColumns(schema, dataFetchingEnvironment);
              changeSettings(schema, dataFetchingEnvironment);
            } catch (IOException e) {
              throw new GraphqlException("Save metadata failed", e);
            }
          });
      return new GraphqlApiMutationResult(SUCCESS, "Meta update success");
    };
  }

  private void changeColumns(Schema schema, DataFetchingEnvironment dataFetchingEnvironment)
      throws IOException {
    List<Map<String, String>> columns =
        dataFetchingEnvironment.getArgument(GraphqlConstants.COLUMNS);
    if (columns != null) {
      for (Map<String, String> c : columns) {
        String tableName = c.get(TABLE);
        TableMetadata tm = schema.getMetadata().getTableMetadata(tableName);
        if (tm == null) {
          throw new GraphqlException("Table '" + tableName + "' not found");
        }
        String json = JsonUtil.getWriter().writeValueAsString(c);
        Column column = JsonUtil.jsonToColumn(json);
        if (column.getOldName() != null) {
          tm.alterColumn(column.getOldName(), column);
        } else {
          tm.add(column);
        }
      }
    }
  }

  private void changeMembers(Schema schema, DataFetchingEnvironment dataFetchingEnvironment) {
    // members
    List<Map<String, String>> members =
        dataFetchingEnvironment.getArgument(GraphqlConstants.MEMBERS);
    if (members != null) {
      for (Map<String, String> m : members) {
        schema.addMember(m.get(EMAIL), m.get(ROLE));
      }
    }
  }

  private void changeTables(Schema schema, DataFetchingEnvironment dataFetchingEnvironment)
      throws IOException {
    Object tables = dataFetchingEnvironment.getArgument(GraphqlConstants.TABLES);
    // tables
    if (tables != null) {
      Map tableMap = Map.of("tables", tables);
      String json = JsonUtil.getWriter().writeValueAsString(tableMap);
      SchemaMetadata otherSchema = jsonToSchema(json);
      schema.migrate(otherSchema);
    }
  }

  private void changeSettings(Schema schema, DataFetchingEnvironment dataFetchingEnvironment) {
    List<Map<String, String>> settings = dataFetchingEnvironment.getArgument(SETTINGS);
    if (settings != null) {
      settings.forEach(
          entry -> {
            if (entry.get(TABLE) != null) {
              Table table = schema.getTable(entry.get(TABLE));
              if (table == null) {
                throw new MolgenisException("Table " + entry.get(TABLE) + " not found");
              }
              table.getMetadata().setSetting(entry.get(KEY), entry.get(VALUE));
            } else {
              schema.getMetadata().setSetting(entry.get(KEY), entry.get(VALUE));
            }
          });
    }
  }

  public GraphQLFieldDefinition dropMutation(Schema schema) {
    return GraphQLFieldDefinition.newFieldDefinition()
        .name("drop")
        .type(typeForMutationResult)
        .dataFetcher(dropFetcher(schema))
        .argument(
            GraphQLArgument.newArgument()
                .name(GraphqlConstants.TABLES)
                .type(GraphQLList.list(Scalars.GraphQLString)))
        .argument(
            GraphQLArgument.newArgument()
                .name(GraphqlConstants.MEMBERS)
                .type(GraphQLList.list(Scalars.GraphQLString)))
        .argument(
            GraphQLArgument.newArgument()
                .name(GraphqlConstants.COLUMNS)
                .type(GraphQLList.list(inputDropColumnType)))
        .argument(
            GraphQLArgument.newArgument()
                .name(SETTINGS)
                .type(GraphQLList.list(inputDropSettingType)))
        .build();
  }
}
