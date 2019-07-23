package org.molgenis.emx2.io;

import org.molgenis.MolgenisException;
import org.molgenis.Schema;

import java.io.File;
import java.io.IOException;

public class ZipLoader {

  public static void load(Schema s, File file) throws IOException, MolgenisException {

    // get metadata from ZIP or DIRECTORY
    if (file.isDirectory()) {
      File molgenisFile = new File(file, "molgenis.csv");
      MolgenisMetadataFileReader.load(s, molgenisFile);
    }

    // get the molgenis.csv file with the data model, and load that first

    // load the data model into the schema

    // then take each of the data files and load those two

    // issue: should follow foreign key dependencies tree
    // issue: mrefs can only be added when all other data is loaded

  }
}
