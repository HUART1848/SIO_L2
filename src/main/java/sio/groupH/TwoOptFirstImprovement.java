package sio.groupH;

import sio.tsp.TspData;
import sio.tsp.TspImprovementHeuristic;
import sio.tsp.TspTour;

import java.util.Arrays;

/**
 * Implements the First Improvement variant of the 2-opt heuristic for the Travelling Salesman Problem.
 */
public final class TwoOptFirstImprovement implements TspImprovementHeuristic {

  /**
   * Applies the First Improvement variant of the 2-opt heuristic to the given TSP tour.
   *
   * @param tspTour The initial TSP tour.
   * @return An improved TSP tour.
   * @throws IllegalArgumentException if the tour is null or has less than two cities.
   */
  @Override
  public TspTour computeTour(TspTour tspTour) {
    if (tspTour == null || tspTour.tour().length < 2) {
      throw new IllegalArgumentException("Invalid TSP tour provided.");
    }

    int[] tour = tspTour.tour();
    long bestLength = tspTour.length();
    boolean improved = true;

    while (improved) {
      improved = false;
      for (int i = 0; i < tour.length - 1; i++) {
        for (int j = i + 2; j < tour.length - (i == 0 ? 1 : 0); j++) {
          long newLength = calculateNewLength(tour, i, j, tspTour.data());
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

    return new TspTour(tspTour.data(), tour, bestLength);
  }

  /**
   * Swaps the segments between two indices in the tour.
   *
   * @param tour The tour array.
   * @param i The start index for the swap.
   * @param k The end index for the swap.
   */
  private void twoOptSwap(int[] tour, int i, int k) {
    while (i < k) {
      int temp = tour[i];
      tour[i] = tour[k];
      tour[k] = temp;
      i++;
      k--;
    }
  }

  /**
   * Calculates the new length of the tour after performing a 2-opt swap.
   *
   * @param tour The current tour array.
   * @param i The start index for the swap.
   * @param j The end index for the swap.
   * @param data The TSP data containing distance information.
   * @return The length of the new tour.
   */
  private long calculateNewLength(int[] tour, int i, int j, TspData data) {
    int[] newTour = Arrays.copyOf(tour, tour.length);
    twoOptSwap(newTour, i + 1, j);

    long newLength = 0;
    int[][] distanceMatrix = data.getDistanceMatrix();

    for (int k = 0; k < newTour.length - 1; k++) {
      newLength += distanceMatrix[newTour[k]][newTour[k + 1]];
    }
    newLength += distanceMatrix[newTour[newTour.length - 1]][newTour[0]]; // Closing the loop

    return newLength;
  }
}
