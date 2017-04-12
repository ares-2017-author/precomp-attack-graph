package support;


public class Plotter {

    public static void main(String[] args) {
        System.out.println(CSVDataPlotter.csvRecordsToPgfPlots("graph-experiments-indiv-nosize-cross.csv",20,3,101,127));
    }
}
