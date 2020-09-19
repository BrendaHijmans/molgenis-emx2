package org.molgenis.emx2.web;

import com.google.common.io.ByteStreams;
import spark.Request;
import spark.Response;
import spark.resource.ClassPathResource;
import spark.staticfiles.MimeType;

import java.io.InputStream;

import static spark.Spark.*;

/**
 * to allow for nice urls, and make it easier for 'schema' app developers we include the schema in
 * the path without need for a router. For future this allows also permission setting to completely
 * hide a schema and disallow apps to be viewed
 */
public class GroupPathMapper {

  private GroupPathMapper() {
    // hide constructor
  }

  public static void create() {

    // redirect graphql api in convenient ways
    get("/:schema/graphql", GraphqlApi::handleSchemaRequests);
    post("/:schema/graphql", GraphqlApi::handleSchemaRequests);

    get("/:schema/:appname/graphql", GraphqlApi::handleSchemaRequests);
    post("/:schema/:appname/graphql", GraphqlApi::handleSchemaRequests);

    // return index.html file when in root
    get("/*/:appname/", GroupPathMapper::returnIndexFile);

    // redirect  js/css assets so they get cached between schemas
    get("/:schema/:appname/*", GroupPathMapper::redirectAssets);
  }

  private static Object returnIndexFile(Request request, Response response) {
    try {
      InputStream in =
          GroupPathMapper.class.getResourceAsStream(
              "/public_html/apps/" + request.params("appname") + "/index.html");
      return new String(ByteStreams.toByteArray(in));
    } catch (Exception e) {
      response.status(404);
      return e.getMessage();
    }
  }

  private static String redirectAssets(Request request, Response response) {
    if (!request.pathInfo().startsWith("/public_html")) {
      response.redirect(
          "/public_html/apps"
              + request.pathInfo().substring(request.params("schema").length() + 1));
      return "";
    } else {
      try {
        InputStream in = GroupPathMapper.class.getResourceAsStream(request.pathInfo());
        response.header(
            "Content-Type", MimeType.fromResource(new ClassPathResource(request.pathInfo())));
        return new String(ByteStreams.toByteArray(in));
      } catch (Exception e) {
        response.status(404);
        return "File not found: " + request.pathInfo();
      }
    }
  }
}
