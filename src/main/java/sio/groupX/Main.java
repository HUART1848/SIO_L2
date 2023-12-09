package sio.groupX;
import sio.tsp.*;

import java.io.IOException;
import java.util.Random;

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
    long seed = 0x134B3BD;

    for (String filePath : filePaths) {
      try {
        TspData data = TspData.fromFile(filePath);
        for (int i = 0; i < 10; i++) {
          RandomTour randomTourGenerator = new RandomTour(seed);
          TspTour initialRandomTour = randomTourGenerator.computeTour(data, 0);



          evaluateHeuristic(new TwoOptFirstImprovement(), initialRandomTour);
          evaluateHeuristic(new TwoOptBestImprovement(), initialRandomTour);

        }

      } catch (IOException e) {
        System.err.println("Error reading file: " + filePath);
      } catch (TspParsingException e) {
          throw new RuntimeException(e);
      }
    }
  }


  private static void evaluateHeuristic(TspImprovementHeuristic heuristic, TspTour initialTour) {
    long startTime = System.currentTimeMillis();
    TspTour improvedTour = heuristic.computeTour(initialTour);
    long endTime = System.currentTimeMillis();
    System.out.println("Heuristic: " + heuristic.getClass().getSimpleName());
    System.out.println("Initial Tour Length: " + initialTour.length());
    System.out.println("Improved Tour Length: " + improvedTour.length());
    System.out.println("Time Taken: " + (endTime - startTime) + " ms");
    System.out.println();
  }

    // Longueurs optimales :
    // att532 : 86729
    // rat575 : 6773
    // rl1889 : 316536
    // u574   : 36905
    // u1817  : 57201
    // vm1748 : 336556


}