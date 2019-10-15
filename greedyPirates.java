/*
Maurizio Altamura
4232977
Assignment 3 Greedy Pirates
COP3503
*/

import java.util.*;
import java.lang.*;
import java.io.*;

class greedyPirates
{
	//scan initial stuff
	public static Scanner sc = new Scanner(System.in);
	public static int x1 = sc.nextInt();
	public static int y1 = sc.nextInt();
	public static int x2 = sc.nextInt();
	public static int y2 = sc.nextInt();
	public static int n = sc.nextInt();
	
	
	public static void main(String[] Args)
	{
		//create array of events
		ArrayList<Event> al = new ArrayList<Event>();
		
		//sort curtain opening
		if(x1 > x2)
		{
			int t = x1;
			x1 = x2;
			x2 = t;
			
			int s = y1;
			y1 = y2;
			y2 = s;
		}
		
		//scan into array and event
		for (int i = 0; i < n; i++)
		{
			int x = sc.nextInt();
			int y = sc.nextInt();
			
			int st = 0;
			int en = 0;
			double m = 0; 
			
			//similar triangles 
			
			if(x != x1)
			{
			m = (double)(y-y1) / (double)(x-x1);
			st = (int) Math.round(((-y)/m) + x);
			}
			if(x != x2)
			{
			m = (double)(y-y2) / (double)(x-x2);
			en = (int) Math.round(((-y)/m) + x);
			}
			if(x == x1 || x == x2)
			{
				if(x == x1)
					st = x;
				if(x == x2)
					en = x;
			}
			
			//end of triangles
			
			//System.out.println("pirate " + (i + 1) + " start = " + st + "     end = " + en);
			
			
			Event  e1 = new Event(st, 1);
			Event  e2 = new Event(en, -1);
			al.add(e1);
			al.add(e2);
		}
		
		//Sort
		Collections.sort(al);
		
		//answer
		int ans = 0;
		int tans = 0;
		for (Event eve : al)
		{
			//System.out.println("type " + eve.type);
			tans += eve.type;
			if (ans < tans)
			{
				ans = tans;
			}
		}
		System.out.print(ans);
	}
	
	public static class Event implements Comparable<Event>
	{
		int time, type;
		
		//event
		Event(int time, int type)
		{
			this.time = time;
			this.type = type;
		}
		
		//comparing events
		public int compareTo(Event o)
		{
			return (time - o.time) == 0 ? (-1) : (time - o.time);
		}
	}
}


