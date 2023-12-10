package sio.groupH;

import sio.tsp.*;
import java.io.IOException;

/**
 * Main class to evaluate the performance of TwoOptFirstImprovement and TwoOptBestImprovement heuristics.
 */
public final class Main {
  public static void main(String[] args) {
    String[] filePaths = {
            "data/att532.dat",
            "data/rat575.dat",
            "data/rl1889.dat",
            "data/u1817.dat",
            "data/u574.dat",
            "data/vm1748.dat"
    };
    long seed = 0x134B3BD; // Seed for random tour generation

    for (String filePath : filePaths) {
      try {
        TspData data = TspData.fromFile(filePath);

        for (int i = 0; i < 10; i++) {
          System.out.println("Iteration " + (i + 1) + " for file: " + filePath);
          RandomTour randomTourGenerator = new RandomTour(seed);
          TspTour initialRandomTour = randomTourGenerator.computeTour(data, 0);

          // Evaluate heuristics with the generated random tour
          evaluateHeuristic(new TwoOptFirstImprovement(), initialRandomTour);
          evaluateHeuristic(new TwoOptBestImprovement(), initialRandomTour);
        }

      } catch (IOException e) {
        System.err.println("Error reading file: " + filePath);
      } catch (TspParsingException e) {
        System.err.println("Error parsing TSP data from file: " + filePath + " - " + e.getMessage());
      }
    }
  }

  /**
   * Evaluates a given TSP heuristic and prints out the results.
   *
   * @param heuristic The TSP heuristic to be evaluated.
   * @param initialTour The initial TSP tour.
   */
  private static void evaluateHeuristic(TspImprovementHeuristic heuristic, TspTour initialTour) {
    try {
      long startTime = System.currentTimeMillis();
      TspTour improvedTour = heuristic.computeTour(initialTour);
      long endTime = System.currentTimeMillis();

      System.out.println("Heuristic: " + heuristic.getClass().getSimpleName());
      System.out.println("Initial Tour Length: " + initialTour.length());
      System.out.println("Improved Tour Length: " + improvedTour.length());
      System.out.println("Time Taken: " + (endTime - startTime) + " ms");
      System.out.println();
    } catch (Exception e) {
      System.err.println("Error during heuristic evaluation: " + e.getMessage());
    }
  }
}
