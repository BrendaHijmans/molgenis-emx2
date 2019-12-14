package org.molgenis.emx2.io.readers;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.molgenis.emx2.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RowReaderJackson {

  private RowReaderJackson() {
    // hide constructor
  }

  private static ObjectReader reader = new CsvMapper().readerFor(Map.class);

  public static Iterable<Row> read(File f, Character separator) throws IOException {
    return read(new FileReader(f), separator);
  }

  public static List<Row> readList(Reader in, Character separator) throws IOException {
    List<Row> result = new ArrayList<>();
    for (Row r : read(in, separator)) {
      result.add(r);
    }
    return result;
  }

  public static Iterable<Row> read(Reader in, Character seperator) throws IOException {
    CsvSchema schema = CsvSchema.emptySchema().withHeader().withNullValue("");
    MappingIterator<Map> iterator = reader.with(schema).readValues(in);

    // ... some reference to data
    return () ->
        new Iterator<>() {
          final Iterator<Map> it = iterator;

          @Override
          public boolean hasNext() {
            return it.hasNext();
          }

          @Deprecated
          public Row next() {
            return new Row(it.next());
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
  }
}
