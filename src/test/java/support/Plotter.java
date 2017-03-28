package support;


public class Plotter {

    public static void main(String[] args) {
        System.out.println(CSVDataPlotter.csvRecordsToPgfPlots("graph-expe-indiv-nosize-para.csv",8,9,3,102,128));
    }
}
