// package org.molgenis.emx2.io;
//
// import org.junit.Test;
// import org.molgenis.Row;
// import org.molgenis.emx2.io.csv.RowReaderApacheCommons;
// import org.molgenis.emx2.io.csv.RowReaderFlatmapper;
// import org.molgenis.emx2.io.csv.RowReaderJackson;
// import org.molgenis.emx2.io.csv.RowReaderUnivocity;
// import org.molgenis.utils.StopWatch;
//
// import java.io.File;
// import java.io.IOException;
//
/// **
// * This test file was only for performance testing. Download data and put in test/resources to try
// * yourself from
// * <li>https://github.com/metmuseum/openaccess/blob/master/MetObjects.csv
// * <li>https://openflights.org/data.html
// */
// public class RowReaderCsvTest {
//
//  @Test
//  public void test1() throws IOException {
//    StopWatch.start("Univocity");
//
//    int count = 0;
//    Iterable<Row> rows = RowReaderUnivocity.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.data.txt first run", count);
//
//    count = 0;
//    rows = RowReaderUnivocity.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      if (count == 0) System.out.println(r);
//      count++;
//    }
//    StopWatch.print("airports.dat.txt second run", count);
//
//    count = 0;
//    rows = RowReaderUnivocity.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.dat.txt unbuf run", count);
//
//    //    count = 0;
//    //    rows = RowReaderUnivocity.read(getFile("MetObjects.csv"));
//    //    for (Row r : rows) {
//    //      count++;
//    //      // System.out.println(r);
//    //    }
//    //    StopWatch.print("MetObjects run", count);
//  }
//
//  @Test
//  public void test2() throws IOException {
//    StopWatch.start("Apache Commons");
//
//    int count = 0;
//    Iterable<Row> rows = RowReaderApacheCommons.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      if (count == 0) System.out.println(r);
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.data.txt first run", count);
//
//    count = 0;
//    rows = RowReaderApacheCommons.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.dat.txt second run", count);
//
//    count = 0;
//    rows = RowReaderApacheCommons.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.dat.txt unbuf run", count);
//
//    count = 0;
//    rows = RowReaderApacheCommons.read(getFile("MetObjects.csv"));
//    for (Row r : rows) {
//      if (count == 0) System.out.println(r);
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("MetObjects run", count);
//  }
//
//  @Test
//  public void test3() throws IOException {
//    StopWatch.start("Jackson");
//
//    int count = 0;
//    Iterable<Row> rows = RowReaderJackson.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      if (count == 0) System.out.println(r);
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.data.txt first run", count);
//
//    count = 0;
//    rows = RowReaderJackson.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.dat.txt second run", count);
//
//    count = 0;
//    rows = RowReaderJackson.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.dat.txt unbuf run", count);
//
//    count = 0;
//    rows = RowReaderJackson.read(getFile("MetObjects.csv"));
//    for (Row r : rows) {
//      if (count == 0) System.out.println(r);
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("MetObjects run", count);
//  }
//
//  @Test
//  public void test4() throws IOException {
//    StopWatch.start("Flatmapper");
//
//    int count = 0;
//    Iterable<Row> rows = RowReaderFlatmapper.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      if (count == 0) System.out.println(r);
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.data.txt first run", count);
//
//    count = 0;
//    rows = RowReaderFlatmapper.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.dat.txt second run", count);
//
//    count = 0;
//    rows = RowReaderFlatmapper.read(getFile("airports.dat.txt"));
//    for (Row r : rows) {
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("airports.dat.txt unbuf run", count);
//
//    count = 0;
//    rows = RowReaderFlatmapper.read(getFile("MetObjects.csv"));
//    for (Row r : rows) {
//      if (count == 0) System.out.println(r);
//      count++;
//      // System.out.println(r);
//    }
//    StopWatch.print("MetObjects run", count);
//  }
//
//  private File getFile(String name) {
//    String file = ClassLoader.getSystemResource(name).getFile();
//    return new File(file);
//  }
// }
