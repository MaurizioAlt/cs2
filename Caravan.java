/*
Maurizio Altamura
4232977
Assignment 2 Caravan
COP3503
*/

import java.util.*;
import java.io.*;
import java.lang.*;

class Caravan
{
	//public static final int IMPOSSIBLE = 0;
	public static Scanner sc = new Scanner(System.in);
	public static int V = sc.nextInt();
	public static int E = sc.nextInt();
	public static int budget = 0;
	public static int costVan = 0;
	public static int weightVan = 0;
	public static int src = 0;
	public static int dest = 0;
	public static int roadCost = 0;
	public static int weight = 0;
	public static int minCap = 0;
	public static int vans = 0;
	public static int money = 0;
	public static int capX = 0;
	public static int capY = 0;
	public static int curr = 0;
	public static int countResults = 0;
	public static boolean[] result = new boolean[10];
	public static boolean[] mstSet = new boolean[V];
	public static int[] parent = new int[V];
	public static int[] key = new int[V];
	public static int[][] roadCap = new int[V][V];
	public static int[][] roadPrice = new int[V][V];
	
	public static void main (String[] Args)// throws java.lang.Exception
	{
		//scanning data in
		for(int i = 0; i < E ; i++)
		{
			//initialize 
			src = sc.nextInt();
			dest = sc.nextInt();
			roadCost = sc.nextInt();
			roadPrice[src - 1][dest - 1] = roadCost;
			roadPrice[dest - 1][src - 1] = roadCost;
			
			weight = sc.nextInt();	
			roadCap[src - 1][dest - 1] = weight;
			roadCap[dest - 1][src - 1] = weight;
			
			//find min weight
			if(i == 0 || weight < minCap)
			{
				minCap = weight;
				capX = src - 1;
				capY = dest - 1;
			}
		}
		//initialize
		budget = sc.nextInt();
		costVan = sc.nextInt();
		weightVan = sc.nextInt();
		
		double possible = weightVan / (double)minCap;
		
		// Consider first purchasing some number of wagons.
		while( possible > 10 ) 
		{
			minCap = findMinCap();
			possible = weightVan / (double)minCap;
		}
		
		//find money needed
		money = (int)Math.ceil(weightVan / (double)minCap) * costVan;
		
		//MST algo
		while( primMST() )
		{
			minCap = findMinCap();
			
			//impossible
			if(minCap == -1)
				break;
				
			money = (int)Math.ceil(weightVan / (double)minCap) * costVan;
		}
		
		printResult();
    }
 
	//driver program
    public static boolean primMST()
    {
        // Initialize as INFINITE
        for (int i = 0; i < V ; i++)
        {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }
 
        // 1st vertex in MST. 
        key[0] = 0;     // key 0 
        parent[0] = -1; // First node is always root

		// V  vertices
        for (int i = 0; i < V - 1 ; i++)
        {
            // minimum key vertex
            // not yet included in MST 
            curr = findCurr();
            
            // Add the picked vertex
            mstSet[curr] = true;
 
            // Update key value and parent index of the adjacent 
            // vertices of the picked vertex. Consider only those 
            // vertices which are not yet included in MST 
            for (int j = 0; j < V ; j++)
            {
				// roadPrice[curr][j] is non zero only for adjacent vertices of m 
                // mstSet[j] is false for vertices not yet included in MST 
                // Update the key only if roadPrice[curr][j] is smaller than key[j]    
                if (roadPrice[curr][j] != 0 && mstSet[j] == false && roadPrice[curr][j] <  key[j])
				{
                    parent[j]  = curr;
                    key[j] = roadPrice[curr][j];
                }
			}
        }
		
		//need more money than budget 
		for(int i = 0; i < V ; i++)
		{
			money += key[i];
			if(money > budget)
				return false;
		}
		
		//num vans can have
		vans = (int)Math.ceil(weightVan / (double)roadCap[capX][capY]);
		int spend = money;
		
		//checking for result 
		for(int i = 0; spend <= budget; i++)
		{
			if(vans + i > 10)
				break;
				
			result[vans + i - 1] = true;
			countResults++;
			
			spend = (costVan * (i + 1) ) + money;
		}
		
		return true;
    }
 
	public static int findMinCap()
	{
		int flag = 0;
		
		for(int i = 0; i < V; i++)
		{
			for(int j = 0; j < V; j++)
			{
				//min cap road found
				if(roadCap[i][j] == minCap)
				{
					roadCap[i][j] = 0;
					roadCap[j][i] = 0;
					roadPrice[i][j] = 0;
					roadPrice[j][i] = 0;
					//destroy road
					E--;
				}
				//road can carry
				else if( roadCap[i][j] > 0 && ( i == 0 || roadCap[i][j] < flag) )
				{
					flag = roadCap[i][j];
					capX = i;
					capY = j;
				}
				/*
				else
					System.out.println("error " + i + " " + j);
				*/
			}
		}
		
		//impossible
		if( (V - 1) > E )
			return -1;
			
		return flag;
	}
 
    public static int findCurr()
    {
    	//initialize
        int min = Integer.MAX_VALUE;
		int flag = 0; 
		
		//find city
        for (int i = 0; i < V ; i++)
        {
            if (mstSet[i] != true && min > key[i])
            {
                min = key[i];
                flag = i;
            }
        }
            
        return flag;
    }
	
	public static void printResult()
    {
    	//print out result
    	System.out.println(countResults);
    	
    	if(countResults == 0)
    	{
    		System.out.print("impossible");
    		return;
    	}
		
		for (int i = 0; i < 10 ; i++)
		{
			if(result[i] == true)
				System.out.print( (i + 1) + " " );
		}
		
		return;
    }
	
}