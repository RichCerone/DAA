import java.io.*;
import java.util.ArrayList;
 
/**
 * newTsp class uses the nearest neighbor method. 
 * We increment the starting city for each iteration
 * and look for the next smallest weight until all cities
 * have been selected.
 * @author Matthew Cieslak and Richard Cerone 
 */
public class tsp 
{
     int[] xPos;
     int[] yPos;
     
    int[] bestX;
    int[] bestY;
    int[] bestPath;
 
    public static void main(String[] args)
    {   
         
        tsp tsp = new tsp();
        tsp.readfile("brute10.txt");
        float matrix[][] = tsp.generateMatrix();
        for(int i=0; i<tsp.xPos.length; i++){
            for(int j=0; j<tsp.xPos.length; j++){
                 
                System.out.print(matrix[i][j] + " ");
                 
            }
            System.out.println();
             
        }
        System.out.println();
        tsp.NearestNeighbor();
    }
     
    /**
     * Reads the file and loads the xPos array and 
     * yPos array with the coordinates.
     * @param string
     */
    private void readfile(String fileName)
    {
        File file = new File(fileName);
        ArrayList<String> x = new ArrayList<String>();
        ArrayList<String> y = new ArrayList<String>();
        boolean found = false;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
             
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
                if(found)
                {
                    if(line.contains("EOF"))
                    {
                        break;
                    } 
                    else
                    {
                        String[] parts = line.split("\\s+");
                        x.add(parts[1]);
                        y.add(parts[2]);
                    }
                }
                if(line.contains("NODE_COORD_SECTION"))
                {
                    found = true;
                }
            }
            br.close();
        } 
        catch (IOException xx)
        {
            System.out.println("IO EXCEPTION");
        }
 
        xPos = new int[x.size()+1];
        yPos = new int[y.size()+1];
        bestX = new int[xPos.length];
        bestY = new int[yPos.length];
        bestPath = new int[xPos.length];
 
        for(int i = 0; i < xPos.length-1; i++)
        {   
            int valx = (int) Double.parseDouble(x.get(i));
            xPos[i] = valx;
            int valy = (int) Double.parseDouble(y.get(i));
            yPos[i] = valy;
        }
 
    }
     
    /**
     * Populates the cost matrix with the edge
     * weights.
     * 
     * @return returns the cost matrix of all
     * the cities.
     */
    private float[][] generateMatrix()
    {
        int[] index = new int[xPos.length];
        index = populate();
        float costs[][] = new float[xPos.length][xPos.length];
        for(int i=0; i<xPos.length; i++)
        {
            for(int j=0; j<xPos.length ; j++)
            {
                costs[i][j] = (float) Math.sqrt(Math.pow((xPos[index[i]] - xPos[index[j]]),2)+Math.pow((yPos[index[i]] - yPos[index[j]]),2));
            }
        }
        return costs;
    }
     
    /**
     * Checks if this current tour was better than
     * the last one. If it is the last run through
     * it returns the best tour out of all the runs.
     * 
     * @param startCity the start city of the current tour.
     * @param bestTour the current best tour.
     * @param tourCost the cost of the current tour.
     * @return bestTour cost out of all 
     * runs.
     */
    private float tourCost(int startCity,float bestTour,float tourCost, ArrayList visited, int best_x[], int best_y[])
    {
        if(startCity == 0)
        {
            bestTour = tourCost;
            bestX = bestX(startCity,visited,best_x,bestTour,tourCost);
            bestY = bestY(startCity,visited,best_y,bestTour,tourCost);
            for(int i = 0; i < bestPath.length; i++)
            {
                bestPath[i] = (int) visited.get(i);
            }
        }
        else if(startCity > 0)
        {
            if(tourCost < bestTour)
            {
                bestTour = tourCost;
                bestX = bestX(startCity,visited,best_x,bestTour,tourCost);
                bestY = bestY(startCity,visited,best_y,bestTour,tourCost);
                for(int i = 0; i < bestPath.length; i++)
                {
                    bestPath[i] = (int) visited.get(i);
                }
            }
        }
        return bestTour;
    }
     
    /**
     * Populates the array with the best tour
     * of x-coordinates.
     * 
     * @param startCity the start city of the current tour.
     * @param index array of indexes. 
     * @param best_x that will hold the best tour for 
     * x-coordinates.
     * @param bestTour the current best tour.
     * @param tourCost the cost of the current tour.
     * @return returns the array holding the best tour of 
     * x-coordinates.
     */
    private int[] bestX(int startCity, ArrayList index, int best_x[], float bestTour, float tourCost){
        if(startCity == 0)
        {
            bestTour = tourCost;
            for(int i = 0; i < index.size(); i++)
            {
                best_x[i] = xPos[(int) index.get(i)];
            }
        }
        else if(startCity > 0)
        {
            if(tourCost <= bestTour)
            {
                bestTour = tourCost;
                for(int i = 0; i < index.size(); i++)
                {
                    best_x[i] = xPos[(int) index.get(i)];
                }
            }
        }
        return best_x;
    }
     
    /**
     * Populates the array with the best tour
     * of y-coordinates.
     * 
     * @param startCity the start city of the current tour.
     * @param index array of indexes. 
     * @param best_y that will hold the best tour for 
     * y-coordinates.
     * @param bestTour the current best tour.
     * @param tourCost the cost of the current tour.
     * @return returns the array holding the best tour of 
     * y-coordinates.
     */
    private int[] bestY(int startCity, ArrayList index, int best_y[], float bestTour, float tourCost){
        if(startCity == 0)
        {
            bestTour = tourCost;
            for(int i = 0; i < index.size(); i++)
            {
                best_y[i] = yPos[(int) index.get(i)];
            }
        }
        else if(startCity > 0)
        {
            if(tourCost <= bestTour)
            {
                bestTour = tourCost;
                for(int i = 0; i < index.size(); i ++)
                {
                    best_y[i] = yPos[(int) index.get(i)];
                }
            }
        }
        return best_y;
    }
     
    /**
     * Populates the index array with city 
     * numbers.
     * 
     * @return returns the populated array.
     */
    private int[] populate()
    {
        int[] index = new int[xPos.length];
        for(int i = 0; i < index.length; i++)
        {
            index[i] = i;
        }
        return index;
    }
     
    /**
     * Prints the best tour out of all tours.
     * 
     * @param index array with city numbers.
     * @param best_x that will hold the best tour for 
     * x-coordinates.
     * @param best_y that will hold the best tour for 
     * y-coordinates.
     * @param bestTour the current best tour.
     */
    private void print_best(ArrayList index, float bestTour){
        System.out.println();
        for(int m=0; m<index.size(); m++)
        {
            System.out.print(bestX[m] + " ");
        }
        System.out.println();
        for(int m=0; m<index.size(); m++)
        {
            System.out.print(bestY[m] + " ");
        }
        System.out.println();
        for(int x=0; x < index.size(); x++)
        {
        System.out.print(bestPath[x] + "  ");
        }
        System.out.println();
        System.out.println("tour cost : " + bestTour);
         
    }
 
    /**
     *    In the Nearest Neighbor Method we generate the costs matrix
     *    Just as we did in the brute force method. Then starting from
     *    city 1 when see which is the smallest edge weight, then we take
     *    the index of that edge weight and use it to locate the next city
     *    for example in our matrix, costs[i][j], we iterate for j on each i
     *    to find the lowest cost, the index of that cost will be the new i
     *    then when we jump to that city we repeat the process until all cities
     *    have been visited, we store the cities in a ArrayList "visited". once
     *    we calculate the tour and its costs we simply increment the start city
     *    until all possible starting vertices have been exhausted.
     */
    private void NearestNeighbor()
    {
        int[] index = new int[xPos.length];
        float costs[][];
        int start = 0;
        int curr = start;
        float bestweight = 0;
        float besttour = 0;
        int[] best_x = new int[index.length];
        int[] best_y = new int[index.length];
        int next = 0;
        float tourcost = 0;
        ArrayList<Integer> visited = new ArrayList<Integer>();
 
        index = populate();
        costs = generateMatrix();
        //Iteration of each tour.
        for(int startCity = 0; startCity < index.length; startCity++)
        {
            visited.clear();
            visited.add(start);
            //Iteration of each row (best city).
             
            for(int nextCity = 0; nextCity < index.length-1; nextCity++)
            {
                for(int i=0; i < index.length; i++) //Finds best next vertex.
                {
                    if(i == 0)
                    {
                        //Check if city has been visited.
                        if(costs[curr][i] == 0 && !visited.contains(i))
                        {
                            bestweight = costs[curr][i+1];
                            next = i + 1;
                        } 
                        else
                        {
                            while(visited.contains(i)) 
                            {
                                i++;
                            }
                            bestweight = costs[curr][i];
                            next = i;
                        }
                    }
                    else if(costs[curr][i] < bestweight && costs[curr][i] != 0
                            && !visited.contains(i))
                    {
                        bestweight = costs[curr][i];
                        next = i;
                    }
                    if(i == index.length - 1)
                    {
                        visited.add(next);
                        curr = next;
                        if(nextCity == index.length - 2)
                        {
                            tourcost += bestweight;
                            bestweight = costs[curr][visited.get(0)];
                            start++;
                            curr = start;
                            //for(int x=0; x < visited.size(); x++)
                            //{
                            //System.out.print(visited.get(x) + " ");
                            //}
                            //System.out.println();
                        }
                    }
                }
                if(nextCity != index.length - 2){
                tourcost += bestweight;
                }
                if(nextCity == index.length - 2)
                {
                System.out.println("tourcost : " + tourcost);
                }
            }
            besttour = tourCost(startCity,besttour,tourcost, visited, best_x, best_y);
            tourcost = 0;
            if(startCity == index.length - 1)
            {
                print_best(visited,besttour);
            }
        }
    }
}