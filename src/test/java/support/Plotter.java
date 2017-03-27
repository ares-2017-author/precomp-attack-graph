package support;


public class Plotter {

    public static void main(String[] args) {
        System.out.println(CSVDataPlotter.csvRecordsToPgfPlots("graph experiments individuals - children variation.csv",7,3,101,143));
    }
}
