package sio.groupX;

import sio.tsp.TspData;
import sio.tsp.TspImprovementHeuristic;
import sio.tsp.TspTour;

import java.util.Arrays;

public final class TwoOptFirstImprovement implements TspImprovementHeuristic {
  @Override
  public TspTour computeTour(TspTour tspTour) {


    int[] tour = tspTour.tour(); // Using record's getter method
    long bestLength = tspTour.length(); // Using record's getter method
    boolean improved = true;

    while (improved) {
      improved = false;
      for (int i = 0; i < tour.length - 1; i++) {
        for (int j = i + 2; j < tour.length - (i == 0 ? 1 : 0); j++) {
          long newLength = calculateNewLength(tour, i, j, tspTour.data()); // Using record's getter method
          if (newLength < bestLength) {
            twoOptSwap(tour, i + 1, j);
            bestLength = newLength;
            improved = true;
            break;
          }
        }
        if (improved) {
          break;
        }
      }
    }

    return new TspTour(tspTour.data(), tour, bestLength); // Using record's constructor
  }


  private void twoOptSwap(int[] tour, int i, int k) {
    while (i < k) {
      int temp = tour[i];
      tour[i] = tour[k];
      tour[k] = temp;
      i++;
      k--;
    }
  }

  private long calculateNewLength(int[] tour, int i, int j, TspData data) {
    int[] newTour = Arrays.copyOf(tour, tour.length);
    twoOptSwap(newTour, i + 1, j);

    long newLength = 0;
    int[][] distanceMatrix = data.getDistanceMatrix(); // Using the getter method to access the distance matrix

    for (int k = 0; k < newTour.length - 1; k++) {
      newLength += distanceMatrix[newTour[k]][newTour[k + 1]];
    }
    newLength += distanceMatrix[newTour[newTour.length - 1]][newTour[0]]; // Adding the distance from the last city back to the first

    return newLength;
  }
}
