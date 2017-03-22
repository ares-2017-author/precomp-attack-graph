package support;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Source from https://examples.javacodegeeks.com/core-java/apache/commons/csv-commons/writeread-csv-files-with-apache-commons-csv-example/
 */

public class CSVFileWriter {
    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final Object[] FILE_HEADER = {"id", "Inital Number of AttackSteps",
            "Number of AttackSteps After Reduction", "Reduction Ratio", "Execution time (ms)", "Number of EntrySteps", "Number of ExitSteps",
            "Maximum Nbr of Children per AttackStep", "Proportion of BinomialChildren", "Number of maxOldParents",
            "Proportion of BinomialOldParents", "Proportion of AttackStep OR", "Lowest Nbr of Children", "Highest Nbr of Children",
            "Mean Nbr of Children", "Lowest Nbr of Parents", "Highest Nbr of Parents", "Mean Nbr of Parents"
    };

    //Create the CSVFormat object with "\n" as a record delimiter
    private static final CSVFormat CSV_FILE_FORMAT = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

    private int idCount;
    FileWriter fileWriter;
    CSVPrinter csvFilePrinter;

    public CSVFileWriter(String fileName) {
        reset(fileName);
    }

    public void reset(String fileName) {
        try {
            fileWriter = new FileWriter(fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, CSV_FILE_FORMAT);
            csvFilePrinter.printRecord(FILE_HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        idCount = 1;
    }

    public void close() {
        try {
            fileWriter.close();
            csvFilePrinter.close();
        } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter/csvPrinter.");
            e.printStackTrace();
        }
    }

    public void newRecord(int nEntrySteps, int nExitSteps, int nbAttackStepsUnreduced, double reduction_ratio, int maxChildren,
                          double pBinomialChildren, int maxOldParents, double pBinomialOldParents,
                          double pMinAttackSteps, int nbAttackStepsReduced, double execTime, int minChildrenNb,
                          int maxChildrenNb, float meanChildrenNb, int minParentsNb, int maxParentsNb, float meanParentsNb) {
        List<String> record = new LinkedList<>();
        record.add(Integer.toString(idCount));
        record.add(Integer.toString(nbAttackStepsUnreduced));
        record.add(Integer.toString(nbAttackStepsReduced));
        record.add(Double.toString(reduction_ratio));
        record.add(Double.toString(execTime));
        record.add(Integer.toString(nEntrySteps));
        record.add(Integer.toString(nExitSteps));
        record.add(Integer.toString(maxChildren));
        record.add(Double.toString(pBinomialChildren));
        record.add(Integer.toString(maxOldParents));
        record.add(Double.toString(pBinomialOldParents));
        record.add(Double.toString(pMinAttackSteps));
        record.add(Integer.toString(minChildrenNb));
        record.add(Integer.toString(maxChildrenNb));
        record.add(Float.toString(meanChildrenNb));
        record.add(Integer.toString(minParentsNb));
        record.add(Integer.toString(maxParentsNb));
        record.add(Float.toString(meanParentsNb));
        if (idCount > 26) {
            System.out.println(record.stream().collect(Collectors.joining("; ")));
        }
        try {
            csvFilePrinter.printRecord(record);
            System.out.println("CSV file was created successfully.");
            csvFilePrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        idCount++;
    }


}
