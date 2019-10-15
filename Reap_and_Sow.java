/* package whatever; // don't place package name! */

/*
Maurizio Altamura
4232977
Assignment 1 Reap and Sow COP3503
*/
import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class ReapSow
{
	
	//public static final int IMPOSSIBLE = 0;
	public static Scanner sc = new Scanner(System.in);
	public static final int n = sc.nextInt();
	public static final int m = sc.nextInt();
	public static int[][] board_values = new int[n][m];
	public static int[][] board_values2 = new int[m][n];
	
	public static void main (String[] args)// throws java.lang.Exception
	{
		//sacn in the 2d array of the land
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < m; j++)
			{
				board_values[i][j] = sc.nextInt();
			}
		}
		
		//if the land is impossible to attempt to solve
		if( (n == 2 && m != 2) || (n != 2 && m == 2) || (n == 0 || m == 0) )
		{
			System.out.print("impossible");
			//printBoard();
			return;
		}
		
		//solve it
		if (solve(0,0))
		{
			printBoard();
		}
		//solving it didnt work
		else
		{
			System.out.print("impossible");
			//printBoard();
		}
		
		return;
	}
		
	public static boolean solve(int r, int c)
	{
		//if the board is valid
		if (!valid(r, c))
        	return false;
        //if we reached the end of the board row-wise
		if (r >= n)
			return true;
		//if we reached the end of the board column-wise, go to the next row
		if (c >= m)
			return solve(r + 1, 0);
		//if there is a rock or seeds planted already
		if (board_values[r][c] == 1)
		    return solve(r, c + 1);
		if (board_values[r][c] == 0)
		    return solve(r, c + 1);
		
		for (int i = 0; i < 2; i++)
		{
			//set plot of land to grow
			board_values[r][c] = i;
			//see if we can do that
			if (solve(r, c + 1))
				return true;
			//we cannot...go back
			board_values[r][c] = -1;
		}
		//failed to find a valid solution
		return false;
	}

	public static boolean valid(int r, int c)
	{
		//check if the rows are the same 
		for (int i = 0; i < n; i++)
		{
			Object[] arr1 = {board_values[i]};
			
			for(int i2 = 0; i2 < n; i2++)
			{
				//if the two rows we chose are the same row, skip
				if(i == i2)
					continue;
		 
		        Object[] arr2 = {board_values[i2]};
		        //if the rows are equal
				if (Arrays.deepEquals(arr1, arr2))
				{
				    int flag = 0;
				    
				    //check to see if the rows contain -1's
				    for (int j = 0; j < m; j++)
					{
						if (board_values[i][j] == -1 || board_values[i2][j] == -1) 
						{
							flag = 1;
							break;
						}
					}
						
				    if(flag == 1)
				    	continue;
				    	
				    return false;
				}
			}
		}
		
		//check if the rows contain more than half of the max width
		//check if the rows contain three consecutive 1's or 0's
		for (int i = 0; i < n; i++)
		{
			int k = 0;
			int k2 = 0;
			
		    for (int j = 0; j < m; j++)
		    {
		    	//see a seed
		    	if (board_values[i][j] == 1)
		    		k++;
		    	//see a rock
		    	if (board_values[i][j] == 0)
		    		k2++;
		    	//consecutive?
		    	if( m > 2 && (j < (m-2)) )
		    		if( (board_values[i][j] == board_values[i][j+1]) && (board_values[i][j] != -1) )
		    			if( (board_values[i][j+1] == board_values[i][j+2]) )// && (board_values[i][j] != -1) )
		    				return false;
		    }
		    
		    if ( (k > (m/2)) || (k2 > (m/2)) )
		    	return false;
		}
		
		//copy the 2d array to another 2d array, inversed, so that we can check the columns 
		for (int p = 0; p < n; p++)
		{
			for (int q = 0; q < m; q++)
			{
				board_values2[q][p] = board_values[p][q];
			}
		}
		
		//checks to see if the columns are the same
		for (int i = 0; i < m; i++)
		{
			Object[] arr1 = {board_values2[i]};
			
			for(int i2 = 0; i2 < m; i2++)
			{
				//if the two columns we chose are the same column, skip
				if(i == i2)
					continue;
		 
		        Object[] arr2 = {board_values2[i2]};
		        ////if the columns are equal
				if (Arrays.deepEquals(arr1, arr2) )
				{
				    int flag = 0;
				    
				    //check to see if the rows contain -1's
				    for (int j = 0; j < n; j++)
					{
						if (board_values2[i][j] == -1 || board_values2[i2][j] == -1) 
							flag = 1;
					}
				    if(flag == 1)
				    	continue;
				    		
				    return false;
				}
			}
		}
		
		//check if the columns contain more than half of the max height
		//check if the columns contain three consecutive 1's or 0's
		for (int i = 0; i < m; i++)
		{
			int k = 0;
			int k2 = 0;
			
		    for (int j = 0; j < n; j++)
		    {
		    	//see a seed
		    	if (board_values2[i][j] == 1)
		    		k++;
		    	//see a rock
		    	if (board_values2[i][j] == 0)
		    		k2++;
		    	//consecutive?
		    	if( n > 2 && (j < (n-2)) )
		    		if( (board_values2[i][j] == board_values2[i][j+1]) && (board_values2[i][j] != -1) )
		    			if( (board_values2[i][j+1] == board_values2[i][j+2]) )
		    				return false;
		    }
		    
		    if ( (k > (n/2)) || (k2 > (n/2)) )
		    	return false;
		}
		
        return true;
    }

    public static void printBoard()
    {
        // Print a border
        //printLine();
 
        // Print each row
        for (int i = 0; i < n; i++)
        {
            // Print each element in the current row
            for (int j = 0; j < m; j++)
            {
                //System.out.print("|");
 
                // Do some spacing for 1 digit values
                if (board_values[i][j] < 10 && board_values[i][j] >= 0)
                    System.out.print(" ");
 
                // Print the value for the board
                System.out.print(board_values[i][j] + " ");
            }
 
            // Print the end of the row
            //System.out.println("|");
            System.out.println("");
 
            // Print a border
            //printLine();
        }
 
        return;
    }
 
    public static void printLine()
    {
        for (int i = 0; i < m; i++)
        {
            System.out.print("+---");
        }
        System.out.println("+");
    }
}