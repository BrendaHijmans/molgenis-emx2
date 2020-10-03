package org.molgenis.emx2.web;

import org.molgenis.emx2.MolgenisException;
import org.molgenis.emx2.Schema;
import org.molgenis.emx2.Table;
import org.molgenis.emx2.io.SchemaExport;
import org.molgenis.emx2.io.SchemaImport;
import org.molgenis.emx2.io.TableExport;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.molgenis.emx2.web.Constants.TABLE;
import static org.molgenis.emx2.web.MolgenisWebservice.getSchema;
import static org.molgenis.emx2.web.MolgenisWebservice.getTable;
import static spark.Spark.get;
import static spark.Spark.post;

public class ZipApi {
  private ZipApi() {
    // hide constructor
  }

  public static void create() {
    // schema level operations
    final String schemaPath = "/:schema/api/zip"; // NOSONAR
    get(schemaPath, ZipApi::getZip);
    post(schemaPath, ZipApi::postZip);

    // table level operations
    final String tablePath = "/:schema/api/zip/:table"; // NOSONAR
    get(tablePath, ZipApi::getZipTable);
  }

  static String getZip(Request request, Response response) throws IOException {

    Path tempDir = Files.createTempDirectory(MolgenisWebservice.TEMPFILES_DELETE_ON_EXIT);
    tempDir.toFile().deleteOnExit();
    try (OutputStream outputStream = response.raw().getOutputStream()) {
      Schema schema = getSchema(request);
      Path zipFile = tempDir.resolve("download.zip");
      SchemaExport.toZipFile(zipFile, schema);
      outputStream.write(Files.readAllBytes(zipFile));
      response.type("application/zip");
      response.header(
          "Content-Disposition",
          "attachment; filename="
              + schema.getMetadata().getName()
              + System.currentTimeMillis()
              + ".zip");
      return "Export success";
    } finally {
      try (Stream<Path> files = Files.walk(tempDir)) {
        files.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      }
    }
  }

  static String postZip(Request request, Response response) throws IOException, ServletException {
    Schema schema = getSchema(request);
    // get uploaded file
    File tempFile = File.createTempFile(MolgenisWebservice.TEMPFILES_DELETE_ON_EXIT, ".tmp");
    tempFile.deleteOnExit();
    request.attribute(
        "org.eclipse.jetty.multipartConfig",
        new MultipartConfigElement(tempFile.getAbsolutePath()));
    try (InputStream input = request.raw().getPart("file").getInputStream()) {
      Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    // depending on file extension use proper importer
    String fileName = request.raw().getPart("file").getSubmittedFileName();

    if (fileName.endsWith(".zip")) {
      SchemaImport.fromZipFile(tempFile.toPath(), schema);
    } else if (fileName.endsWith(".xlsx")) {
      SchemaImport.fromExcelFile(tempFile.toPath(), schema);
    } else {
      throw new IOException(
          "File upload failed: extension "
              + fileName.substring(fileName.lastIndexOf('.'))
              + " not supported");
    }

    response.status(200);
    return "Import success";
  }

  static String getZipTable(Request request, Response response) throws IOException {
    Table table = getTable(request);
    if (table == null) throw new MolgenisException("Table " + request.params(TABLE) + " unknown");
    Path tempDir = Files.createTempDirectory(MolgenisWebservice.TEMPFILES_DELETE_ON_EXIT);
    tempDir.toFile().deleteOnExit();
    try (OutputStream outputStream = response.raw().getOutputStream()) {
      Path zipFile = tempDir.resolve("download.zip");
      TableExport.toZipFile(zipFile, table.query());
      outputStream.write(Files.readAllBytes(zipFile));
      response.type("application/zip");
      response.header(
          "Content-Disposition",
          "attachment; filename="
              + table.getSchema().getMetadata().getName()
              + "_"
              + table.getName()
              + System.currentTimeMillis()
              + ".zip");
      return "Export success";
    } finally {
      try (Stream<Path> files = Files.walk(tempDir)) {
        files.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      }
    }
  }
}
