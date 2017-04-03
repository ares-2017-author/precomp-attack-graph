package support;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
            "Graph Density",
            "N binomial Children", "P Binomial Children", "N Binomial Forward Edges",
            "P Binomial Forward Edges", "N Binomial Back Edges",
            "P Binomial Back Edges", "N Binomial Cross Edges",
            "P Binomial Cross Edges", "Proportion of AttackStep OR", "Lowest Nbr of Children", "Highest Nbr of Children",
            "Mean Nbr of Children", "Mean Nbr of Tree Edges", "Mean Nbr of Forward Edges", "Mean Nbr of Back Edges", "Mean Nbr of Cross Edges"
    };

    //Create the CSVFormat object with "\n" as a record delimiter
    private static final CSVFormat CSV_FILE_FORMAT = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

    private AtomicInteger idCount;
    FileWriter fileWriter;
    CSVPrinter csvFilePrinter;
    ArrayList<List<String>> records;

    public CSVFileWriter(String fileName) {
        reset(fileName);
    }

    public void reset(String fileName) {
        try {
            fileWriter = new FileWriter(fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, CSV_FILE_FORMAT);
            csvFilePrinter.printRecord(FILE_HEADER);
            records = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        idCount = new AtomicInteger(1);
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

    public void addNewRecord(int nEntrySteps, int nExitSteps, int nbAttackStepsUnreduced, float reduction_ratio, int nBinomialChildren,
                             double pBinomialChildren, int nBinomialForwardEdges, double pBinomialForwardEdges,
                             int nBinomialBackEdges, double pBinomialBackEdges,  int nBinomialCrossEdges, double pBinomialCrossEdges, double pMinAttackSteps, float graphDensity, int nbAttackStepsReduced, double execTime,
                             int minChildrenNb,
                               int maxChildrenNb, float meanChildrenNb,
                             float mean_tree, float mean_forward, float mean_back, float mean_cross) {
        List<String> record = createNewRecord(nEntrySteps,nExitSteps,nbAttackStepsUnreduced,reduction_ratio,nBinomialChildren,
                pBinomialChildren,nBinomialForwardEdges,pBinomialForwardEdges, nBinomialBackEdges, pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,pMinAttackSteps,graphDensity,nbAttackStepsReduced,
                execTime,minChildrenNb,maxChildrenNb,meanChildrenNb,mean_tree,mean_forward,mean_back,mean_cross);
        records.add(record);
        idCount.incrementAndGet();
    }

    private List<String> createNewRecord (int nEntrySteps, int nExitSteps, int nbAttackStepsUnreduced, float reduction_ratio, int nBinomialChildren,
                             double pBinomialChildren, int nBinomialForwardEdges, double pBinomialForwardEdges,
                             int nBinomialBackEdges, double pBinomialBackEdges,  int nBinomialCrossEdges, double pBinomialCrossEdges,
                             double pMinAttackSteps, float graphDensity, int nbAttackStepsReduced, double execTime, int minChildrenNb, int maxChildrenNb,
                                          float meanChildrenNb, float mean_tree, float mean_forward, float mean_back, float mean_cross) {
        List<String> record = new LinkedList<>();
        record.add(Integer.toString(idCount.get()));
        record.add(Integer.toString(nbAttackStepsUnreduced));
        record.add(Integer.toString(nbAttackStepsReduced));
        record.add(Float.toString(reduction_ratio));
        record.add(Double.toString(execTime));
        record.add(Integer.toString(nEntrySteps));
        record.add(Integer.toString(nExitSteps));
        record.add(Float.toString(graphDensity));
        record.add(Integer.toString(nBinomialChildren));
        record.add(Double.toString(pBinomialChildren));
        record.add(Integer.toString(nBinomialForwardEdges));
        record.add(Double.toString(pBinomialForwardEdges));
        record.add(Integer.toString(nBinomialBackEdges));
        record.add(Double.toString(pBinomialBackEdges));
        record.add(Integer.toString(nBinomialCrossEdges));
        record.add(Double.toString(pBinomialCrossEdges));
        record.add(Double.toString(pMinAttackSteps));
        record.add(Integer.toString(minChildrenNb));
        record.add(Integer.toString(maxChildrenNb));
        record.add(Float.toString(meanChildrenNb));
        record.add(Float.toString(mean_tree));
        record.add(Float.toString(mean_forward));
        record.add(Float.toString(mean_back));
        record.add(Float.toString(mean_cross));
        System.out.println(record.stream().collect(Collectors.joining("; ")));
        return record;
    }

    public void printNewRecord(int nEntrySteps, int nExitSteps, int nbAttackStepsUnreduced, float reduction_ratio, int nBinomialChildren,
                               double pBinomialChildren, int nBinomialForwardEdges, double pBinomialForwardEdges,
                               int nBinomialBackEdges, double pBinomialBackEdges,  int nBinomialCrossEdges, double pBinomialCrossEdges,
                               double pMinAttackSteps, float graphDensity, int nbAttackStepsReduced, double execTime, int minChildrenNb,
                               int maxChildrenNb, float meanChildrenNb, float mean_tree, float mean_forward, float mean_back, float mean_cross) {
        List<String> record = createNewRecord(nEntrySteps,nExitSteps,nbAttackStepsUnreduced,reduction_ratio,nBinomialChildren,
                pBinomialChildren,nBinomialForwardEdges,pBinomialForwardEdges, nBinomialBackEdges, pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,pMinAttackSteps,graphDensity,nbAttackStepsReduced,
                execTime,minChildrenNb, maxChildrenNb,meanChildrenNb,mean_tree,mean_forward,mean_back,mean_cross);

        try {
            csvFilePrinter.printRecord(record);
            System.out.println("CSV record was printed successfully.");
            csvFilePrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        idCount.incrementAndGet();
    }

    public void printAllRecords() {
        for (List<String> record: records) {
            try {
                csvFilePrinter.printRecord(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CSV file was populated successfully.");
        }
    }


}
