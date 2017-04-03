package support;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CSVDataPlotter {

    public static String csvRecordsToPgfPlots(String fileName, int abcissa, int axis) {
        return csvRecordsToPgfPlots(fileName,abcissa,axis,0,Integer.MAX_VALUE);
    }

    /**
     * Range is inclusive
     * @param fileName
     * @param ordinate
     * @param abcsissa
     * @param start
     * @param end
     * @return
     */
    public static String csvRecordsToPgfPlots(String fileName, int ordinate, int abcsissa, int start, int end) {
        String result = "";
        try {
            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> listRecords = Lists.newLinkedList(records);
//            listRecords.sort(Comparator.comparing(r -> r.get(ordinate)));
            for (CSVRecord record : listRecords) {
                int id = Integer.parseInt(record.get(0));
                if (id >= start && id <= end)
                    result += "("+record.get(ordinate)+","+record.get(abcsissa)+")";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String csvRecordsToPgfPlots(String fileName, int ordinate, int abcsissa, int applicate, int start, int end) {
        String result = "";
        try {
            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                int id = Integer.parseInt(record.get(0));
                if (id >= start && id <= end)
                    result += "("+record.get(ordinate)+","+record.get(abcsissa)+","+record.get(applicate)+")";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
