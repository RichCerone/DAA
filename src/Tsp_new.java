import java.io.*;
import java.util.ArrayList;
 
/**
 * newTsp class uses the nearest neighbor method. 
 * We increment the starting city for each iteration
 * and look for the next smallest weight until all cities
 * have been selected.
 * @author Matthew Cieslak and Richard Cerone 
 */
public class Tsp_new 
{
    int[] xPos;
    int[] yPos;
    int[] cities;
    int[] bestPath;
 
    public static void main(String[] args)
    {   
    	long startTime = System.currentTimeMillis();
        Tsp_new tsp = new Tsp_new();
        tsp.readfile("mini1.txt");
        double matrix[][] = tsp.generateMatrix();
        for(int i=0; i<tsp.xPos.length; i++){
            for(int j=0; j<tsp.xPos.length; j++){
                 
                System.out.print(matrix[i][j] + " ");
                 
            }
            System.out.println();
             
        }
        System.out.println();
        tsp.solve();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
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
 
        xPos = new int[x.size()];
        yPos = new int[y.size()];
        bestPath = new int[xPos.length];
        cities = new int[xPos.length];
 
        for(int i = 0; i < xPos.length; i++)
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
    private double[][] generateMatrix()
    {
        int[] index = new int[xPos.length];
        index = populate();
        double[][] costs = new double[xPos.length][xPos.length];
        for(int i=0; i<xPos.length; i++)
        {
            for(int j=0; j<xPos.length ; j++)
            {
                costs[i][j] = (double) Math.sqrt(Math.pow((xPos[index[i]] - xPos[index[j]]),2)+Math.pow((yPos[index[i]] - yPos[index[j]]),2));
            }
        }
        return costs;
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
    
    private int[] createCities()
    {
    	for(int i = 0; i < xPos.length; i++)
    	{
    		cities[i] = i+1;
    	}
		return cities;
    }
    
    /**
     * The solve method solves the nearest neighbor problem:
     * 1. First we generate the cost matrix of all city distances.
     * 2. We know that our first city of each tour and the first path 
     * will automatically be the shortest. So, for the first city and path
     * we compare it to a maximum value integer since we know 
     * that the first city and path is the smallest. We then assign the 
     * new tour cost and the new best path to their respective variables.
     * We repeat until all possible cities have been traversed.
     * 3. Print out the best tour and the cost for that tour. 
     */
    private void solve()
    {
    	//Generate cost matrix for distances of each city.
    	double costs[][] = generateMatrix();
    	double bestTotal = Integer.MAX_VALUE; //Best tour cost.
    	ArrayList<Integer> bestTour = new ArrayList<Integer>();
    	//Create all present cities.
    	cities = createCities();
    	for(int city = 0; city < cities.length; city++ )
    	{
    		double currentBest = 0; //Current tour cost.
    		double total = 0; //total tour cost.
    		int startCity = cities[city]; //Current start city.
    		int remove = 0; //City to be removed from the remaining cities array.
    		int currentCity = cities[city]; //Current city.
    		ArrayList<Integer>currentTour = new ArrayList<Integer>(); //Current tour.
    		ArrayList<Integer>remainingCities= new ArrayList<Integer>(); //List of remaining cities.
    		
    		currentTour.add(startCity); // Add start city to the tour.
    		
    		//Add all cities to remaining cities except the start city.
    		for(int i = 0; i < cities.length; i++)
    		{
    			if(cities[i] != cities[city])
    			{
    				remainingCities.add(cities[i]);
    			}
    		}
    		
    		//Keep going until there are no more cities left to visit.
    		while(!remainingCities.isEmpty())
    		{
    			int nextCity = 0; //Next best city to visit.
    			double bestPath = Integer.MAX_VALUE; //Value of the best path 
    			for(int i = 1; i <= remainingCities.size(); i++)
    			{
    				total = costs[remainingCities.get(i-1)-1][currentCity-1];
    				if(total < bestPath)
    				{
    					bestPath = total;
    					nextCity = remainingCities.get(i-1);
    					remove = i-1;
    				}
    			}
	    		currentTour.add(nextCity); //Add the next best city to the tour.
	    		currentCity = nextCity; //Set the next city as the current city.
	    		remainingCities.remove(remove); //Removes the city visited from the list.
	    		currentBest += bestPath; //Adds the best path to this tour.
    		}
    		//Go back to the start city and finish the tour
    		total = costs[startCity-1][currentCity-1];
    		currentTour.add(startCity);
    		currentBest += total;
	    	//Check if this is the best tour.
	    	if(currentBest <= bestTotal)
	    	{
	    		bestTotal = currentBest;
	    		bestTour = currentTour;
	    	}
	    	//Check if on the last tour to print out info.
	    	if(city == cities.length-1)
	    	{
	    		System.out.println("Best total: " + bestTotal);
	    		for(int i = 0; i < bestTour.size(); i++)
	    		{
	    			System.out.print(bestTour.get(i) + " ");
	    		}
	    	}
    	}
    }
}