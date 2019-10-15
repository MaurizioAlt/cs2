/*
Maurizio Altamura
4232977
Assignment 5 Epic Fight
COP3503
*/

import java.util.*;
import java.lang.*;
import java.io.*;

//Name of the class has to be "Main" only if the class is public!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class epic
{
	public static Scanner sc = new Scanner(System.in);
	public static int[][][] memo; //3d array  time, xhp, rhp
    public static SpecialMove[] xMoves; //x moves
    public static SpecialMove[] rMoves; //r moves
    public static int numXMoves; //number of x moves
    public static int numRMoves; //number of r moves
    public static int xMaxHp; //x maximum hp
    public static int rMaxHp; //r maximum hp
    public static final int IMPOSSIBLE = -1; //not valid answer
    public static final int MOD = 10007; //mod by 10,007
    public static int time = sc.nextInt(); //scan time of fight
    
	public static class SpecialMove
    {
        int cost; 
        int reduction; 
        
		//moves have a cost to use and a reduction to opponent
        SpecialMove(int a, int b)
        {
            cost = a; 
            reduction = b; 
        }
    }

    public static void main(String[] args)
    {
    	//declare variables
        int answer = 0; 
        int reduction = 0; 
        int cost = 0; 
        
        //scan x max hp and number of moves
        numXMoves = sc.nextInt(); 
        xMaxHp = sc.nextInt(); 
        //store x  moves  
        xMoves = new SpecialMove[numXMoves]; 
        for(int i = 0; i < numXMoves; i++)
        {
            cost = sc.nextInt(); 
            reduction = sc.nextInt(); 
            SpecialMove sp = new SpecialMove(cost, reduction); 
            xMoves[i] = sp; 
        }
        
        //scan r max hp and number of moves
        numRMoves = sc.nextInt(); 
        rMaxHp = sc.nextInt(); 
        //store r moves 
        rMoves = new SpecialMove[numRMoves]; 
        for(int j = 0; j < numRMoves; j++)
        {
            cost = sc.nextInt(); 
            reduction = sc.nextInt(); 
            SpecialMove sp = new SpecialMove(cost, reduction); 
            rMoves[j] = sp; 
        }
        
        //initializes the memoization array dynamic programming
        memo = new int[time+1][xMaxHp+1][rMaxHp+1];
        
        //initialize to impossible
        for(int[][] m_m : memo) 
            for(int[] m : m_m)
                Arrays.fill(m, IMPOSSIBLE);
                
        //solve function to find different ways the fight could have happened
        answer = recursiveSolver(xMaxHp, 0, rMaxHp, 0, 0);

        //mod answer 
        answer %= MOD;
        
        //print answer
        System.out.print(answer);
    }

    public static int recursiveSolver(int xHp, int rDmg, int rHp, int xDmg, int curTime)
    {
    	//declarations
    	int answer = 0;
        int curXHp = xHp;
        int curRHp = rHp;
        
        //Base cases
        //X is still alive and R dies when the time is up
        if(xHp > 0 && rHp <= 0 && curTime > time)
            return 1;
        //X dies
        else if(xHp <= 0) 
            return 0; 
        //R dies before the time is up
        else if(curTime < time && rHp <= 0) 
            return 0; 
        //R is still alive after time is up
        else if(curTime > time && rHp > 0) 
            return 0; 
        //checks if there is a valid solution stored
        else if(xHp > 0 && rHp > 0 && memo[curTime][xHp][rHp] != IMPOSSIBLE)
        	return memo[curTime][xHp][rHp];
        
        //even time = deduct cost
        else if(curTime % 2 == 0)
        {
            for(int i=0; i<numXMoves; i++)
            {
                for(int j=0; j<numRMoves; j++)
                {
                    //damage received 
                    xHp -= xMoves[i].cost;
                    rHp -= rMoves[j].cost;
                    
                    //restore hp above their max capacity
                    if(xHp > xMaxHp)
                        xHp = xMaxHp; 
                    if(rHp > rMaxHp)
                        rHp = rMaxHp; 
                    
                    //find the number of possible combinations
                    answer += recursiveSolver(xHp, xMoves[i].reduction, rHp, rMoves[j].reduction, curTime+1);
                    
                    //backtrack
                    xHp = curXHp; 
                    rHp = curRHp; 
                }
            }
        }
        
        //odd time = deduct damage
        else
        {
        	//damage received 
            xHp -= xDmg; 
            rHp -= rDmg;
                    
            //restore hp above their max capacity
            if (xHp > xMaxHp)
                xHp = xMaxHp; 
            if (rHp > rMaxHp)
                rHp = rMaxHp; 
                    
            //step next second
            return recursiveSolver(xHp, rDmg, rHp, xDmg, curTime+1);
        }
        
        //store solution into array 
        return (memo[curTime][xHp][rHp] = answer);
    }
}



