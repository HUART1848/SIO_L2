package sio.groupH;

import sio.tsp.TspImprovementHeuristic;
import sio.tsp.TspTour;
import sio.tsp.TspData;


/**
 * Implements the Best Improvement variant of the 2-opt heuristic for the Travelling Salesman Problem.
 */
public final class TwoOptBestImprovement implements TspImprovementHeuristic {

  /**
   * Applies the Best Improvement variant of the 2-opt heuristic to the given TSP tour.
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
    boolean improved;

    do {
      improved = false;
      long bestDelta = 0;
      int bestI = -1;
      int bestJ = -1;

      for (int i = 0; i < tour.length - 1; i++) {
        for (int j = i + 2; j < tour.length - (i == 0 ? 1 : 0); j++) {
          long delta = calculateDelta(tour, i, j, tspTour.data());
          if (delta < bestDelta) {
            bestDelta = delta;
            bestI = i;
            bestJ = j;
            improved = true;
          }
        }
      }

      if (improved) {
        twoOptSwap(tour, bestI + 1, bestJ);
        bestLength += bestDelta;
      }
    } while (improved);

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
   * Calculates the change in tour length (delta) after performing a 2-opt swap.
   *
   * @param tour The current tour array.
   * @param i The start index for the swap.
   * @param j The end index for the swap.
   * @param data The TSP data containing distance information.
   * @return The change in length of the tour.
   */
  private long calculateDelta(int[] tour, int i, int j, TspData data) {
    int[][] distanceMatrix = data.getDistanceMatrix();
    int size = tour.length;

    int a = tour[i];
    int b = tour[(i + 1) % size];
    int c = tour[j];
    int d = tour[(j + 1) % size];

    long currentDistance = distanceMatrix[a][b] + distanceMatrix[c][d];
    long newDistance = distanceMatrix[a][c] + distanceMatrix[b][d];

    return newDistance - currentDistance;
  }
}
