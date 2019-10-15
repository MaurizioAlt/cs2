/*
Maurizio Altamura
4232977
Assignment 4 Arrest Warrant
COP3503
*/

import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.LinkedList;

class ArrestWarrant
{

	public static Scanner sc = new Scanner(System.in);
	public static int n = sc.nextInt(); //Number of cities
	public static int m = sc.nextInt(); //Number of Roads
	public static ArrayList<NodeCity> arrayCity;

	public static void main(String[] args)
	{
		// array of city nodes
		arrayCity = new ArrayList<NodeCity>(n);

		for (int i = 0; i < n; i++)
		{
			arrayCity.add(i, new NodeCity(sc.nextInt(), sc.nextInt(), i + 1)); // add one to fix indices
		}


		for (int i = 0; i < m; i++)
		{
			int si = sc.nextInt();
			int ei = sc.nextInt();
			int ci = sc.nextInt();

			arrayCity.get(si-1).adjList.put((arrayCity.get(ei-1)), ci);

			// undirected so reverse
			arrayCity.get(ei-1).adjList.put((arrayCity.get(si-1)), ci);
		}

		// Min cost
		int minimumCost = Integer.MAX_VALUE;

		// modified Dijkstra
		dijkstra();

		//adjust minimum cost
		minimumCost = arrayCity.get(n - 1).costGreedy;

		ArrayList<NodeCity> pathway = new ArrayList<NodeCity>();

		for (int i = 0;  i < n; i++)
		{
			if (arrayCity.get(i).costGreedy > minimumCost)
			{
				pathway.add(arrayCity.get(i));
			}
		}

		// Make pathways
		NodeCity beginNode = copyCity(arrayCity.get(0), 0, 20);
		PriorityQueue<NodeCity> priorityQ = new PriorityQueue<NodeCity>();
		priorityQ.add(beginNode);

		while(!priorityQ.isEmpty())
		{
			NodeCity currentNode = priorityQ.poll();

			//when cost is greater than minimum, cut off that path
			if (currentNode.currentCost > minimumCost)
				continue;

			//loop through
			for (NodeCity connectedCity : arrayCity.get(currentNode.id - 1).adjList.keySet())
			{
				if (pathway.contains(connectedCity))
					continue;

				//loop through all the pirates
				for (int i = 0; i <= connectedCity.numPirates; i++)
				{
					int newCost = currentNode.currentCost + (arrayCity.get(currentNode.id - 1).adjList.get(connectedCity) *
									currentNode.currentPass) + (i * connectedCity.bribeCost);

					int newPassengers = currentNode.currentPass + ((i * 2) - connectedCity.numPirates);

//					if (currentNode.currentCost == 7 && currentNode.currentPass == 2 && connectedCity.id == 10 && currentNode.id == 3 && i == 0)
//						System.out.println("New Path To ID #10 Pass: " + newPassengers + ". Cost: " + newCost + "| " + (arrayCity.get(currentNode.id - 1).adjList.get(connectedCity) * currentNode.currentPass));

					// conditions
					if (newCost >= minimumCost)
						continue;

					if (newPassengers > 20)
						continue;

					if (connectedCity.id == n)
					{
						if (newPassengers < 0)
							continue;

						minimumCost = newCost;
						continue;
					}
					else
					{
						if (newPassengers < 1)
							continue;

						NodeCity nextNode = copyCity(connectedCity, newCost, newPassengers);
						priorityQ.add(nextNode);
					}
				}
			}
		}

		//adjust answer
		if(minimumCost < 21 && minimumCost > 0)
			minimumCost--;

		//print answer
		System.out.print(minimumCost);
	}

	public static void dijkstra()
	{
		//initialize
		Queue<NodeCity> nodeQueue = new LinkedList<NodeCity>();
		arrayCity.get(0).passGreedy = 20;
		arrayCity.get(0).costGreedy = 0;

		nodeQueue.add(arrayCity.get(0));

		//while queue is not empty
		while(!nodeQueue.isEmpty())
		{
			NodeCity currentNode = nodeQueue.poll();

			for (NodeCity connectingCity : currentNode.adjList.keySet())
			{
				int weight = currentNode.adjList.get(connectingCity);

				if (currentNode.passGreedy > connectingCity.numPirates)
				{
					if ( (currentNode.costGreedy + (currentNode.passGreedy * weight)) > connectingCity.costGreedy)
					{
						continue;
					}

					//adjust variables
					connectingCity.costGreedy = currentNode.costGreedy + (currentNode.passGreedy * weight);
					connectingCity.passGreedy = currentNode.passGreedy - connectingCity.numPirates;
					nodeQueue.add(connectingCity);
					continue;
				}

				int variable = connectingCity.numPirates - currentNode.passGreedy + 1;

				//divisible by 2
				if (variable%2 == 0)
				{
					if (currentNode.costGreedy + (currentNode.passGreedy * weight) + (connectingCity.bribeCost * (variable / 2)) > connectingCity.costGreedy)
						continue;
					connectingCity.passGreedy = 1;
					connectingCity.costGreedy = currentNode.costGreedy + (currentNode.passGreedy * weight) + (connectingCity.bribeCost * (variable / 2));
					nodeQueue.add(connectingCity);
					continue;
				}
				else
				{
					if (currentNode.costGreedy + (currentNode.passGreedy * weight) + (connectingCity.bribeCost * ((variable / 2) +  1)) > connectingCity.costGreedy)
						continue;
					connectingCity.passGreedy = 2;
					connectingCity.costGreedy = currentNode.costGreedy + (currentNode.passGreedy * weight) + (connectingCity.bribeCost * ((variable / 2) + 1));
					nodeQueue.add(connectingCity);
					continue;
				}
			}
		}

	}

	//make copy of city
	public static NodeCity copyCity(NodeCity n, int currentCost, int currentPass)
	{
		NodeCity newNode = new NodeCity(n.numPirates, n.bribeCost, n.id);
		newNode.currentCost = currentCost;
		newNode.currentPass = currentPass;
		return newNode;
	}
}


// type nodecity
class NodeCity implements Comparable<NodeCity> {

	int numPirates;
	int bribeCost;
	int id;

	Map <NodeCity, Integer> adjList = new HashMap <NodeCity, Integer>();

	// greedy
	int costGreedy;
	int passGreedy;

	// pathways
	int currentCost;
	int currentPass;

	public NodeCity(int numPirates, int bribeCost, int id)
	{
		// data
		this.numPirates = numPirates;
		this.bribeCost = bribeCost;
		this.id = id;

		// pq
		this.currentCost = -1;
		this.currentPass = -1;

		// greedy
		this.costGreedy = Integer.MAX_VALUE;
		this.passGreedy = -1;
	}

	// Comparable
	public int compareTo(NodeCity n)
	{
		return (this.currentCost - n.currentCost);
	}
}
