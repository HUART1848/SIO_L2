package sio.groupX;

import sio.tsp.TspImprovementHeuristic;
import sio.tsp.TspTour;
import sio.tsp.TspData;
import java.util.Arrays;

public final class TwoOptBestImprovement implements TspImprovementHeuristic {
  @Override
  public TspTour computeTour(TspTour tspTour) {
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

  private void twoOptSwap(int[] tour, int i, int k) {
    while (i < k) {
      int temp = tour[i];
      tour[i] = tour[k];
      tour[k] = temp;
      i++;
      k--;
    }
  }

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
