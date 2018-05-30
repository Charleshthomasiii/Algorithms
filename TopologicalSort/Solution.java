//Code by Charles Thomas, cht327@nyu.edu, Basic Algorithms with Victor Shoup
//Input: The first line contains two numbers n , m.  The number of nodes of and edges, respectively.
//The next lines contain two numbers a,b denoting each edge, which means that to reach the b'th node needs the a'th node needs to be reached.
//This implementation uses are sparse representation of the graph, and implements a basic topological sort
//The output is a series of nodes in the correct order, else -1 if a cycle is detected.
import java.io.*;
import java.util.*;

class Solution{
	public static void main(String[] args) {
		Scanner reader =new Scanner(System.in);//reading input 
		int vertices = reader.nextInt(); //number of vertices
		int relations = reader.nextInt(); //number of relations
		ArrayList<ArrayList<Integer>> outside = new ArrayList<ArrayList<Integer>>(); //outer list
		//ArrayList<Integer> inside = new ArrayList<Integer>();
		
		for(int i=0;i<vertices;i++){
			outside.add(new ArrayList<Integer>()); //adding inner lists
		}
		for(int i=0;i<relations;i++){
			int start = reader.nextInt(); //get input
			int end = reader.nextInt(); //get input
			start--;
			end--; //the first element is at index zero, must decerement
			outside.get(start).add(end); //put it in the arraylist
			//System.out.println(outside);
		}
		ArrayList solution = LexTopSort(outside); //call LexTopSort Function
		if(solution==null){ //if theres a cycle
			System.out.println(-1);
		}
		else if(solution.size()==0){ //if there wasn't a solution
			System.out.print(-1);
		}
		else{ //if there was a solution
			for(int i=0;i<solution.size();i++){
				System.out.print(solution.get(i)+" "); //formatting
			}
		}
		
	}

	static ArrayList LexTopSort( ArrayList g){
		int[] inDegrees = new int[g.size()]; //all zeros
		for(int i=0;i<g.size();i++){
			//vertices of in-degree zero
			ArrayList start = (ArrayList) g.get(i); //vertex in sparse adjacency list representation
			for(int j=0; j<start.size(); j++){ //for all vertexes in the list
				inDegrees[(int) start.get(j)]++; //increment in degree of that vertex
			}
		}
		//printing in-degrees for testing purposes
		// System.out.println("In degree array:[ ");
		// for(int i=0;i<g.size();i++){
		// 	System.out.print(inDegrees[i]+", ");
		// }
		// System.out.println("]");


		PriorityQueue<Integer> zeroVertices = new PriorityQueue<Integer>(); //this is a minheap that stores numbers in order of vertex, it needs ot be a minheap because lexographical order matters
		ArrayList<Integer> orderedVertices =new ArrayList(); //this is the solution, the ordered vertices
		for(int i=0; i<g.size();i++){//adding all of in degree zero vertices to minheap
			if(inDegrees[i]==0){
				zeroVertices.add(i);//if theres an index with in-degree zero, add it to the minheap
			}
		}

		int counter = g.size(); //counter is for detecting cycles
		while (zeroVertices.size()!=0){//while heap not empty
			int vertexIndex = zeroVertices.poll();//pop next vertex in lexographical order off heap
			ArrayList nextVertex = (ArrayList) g.get(vertexIndex); //popped the next vertex
			orderedVertices.add(vertexIndex+1);//the plus one is because the first vertex has index 0, not 1
			for(int i=0; i<nextVertex.size();i++){
				//reduce in degree of neighbours by 1
				inDegrees[(int)nextVertex.get(i)]--; //decrement indegree
				if(inDegrees[(int)nextVertex.get(i)]==0){ //if neighbor has in degree zero, add it to heap
					zeroVertices.add((int)nextVertex.get(i));
				}
			}
			counter--; //decrement counter
		}
		if (counter==0) { //detecting cycles
			return orderedVertices;
		}
		else{ //cycle detected
			return null;
		}
	}
}