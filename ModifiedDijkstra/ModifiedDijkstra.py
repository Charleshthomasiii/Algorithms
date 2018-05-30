#Code by Charles Thomas
#The first line contains two numbers n,m: The number of verteces and the number of edges connecting them.
#The next m lines contains three numbers a,b,c : an edge from a to b with c indicating it has been marked if its 1, and 2 if it is unmarked
#The output is the minimum number of edges to pass from vertex 1 to m, while passing through at least one marked edge, and as few unmarked edges as possible
#This algorithm uses a sparse representation, constructs a graph, constructs a supergraph, and runs dijkstra's algorithm on it.
string = input() #taking first input of edges and vertices
edge_verts = string.split() 
verts = int(edge_verts[0])
edges = int(edge_verts[1])
graph = []
graph.append([]) #appending extra list so that vertex  1 is at index 1 and not index 0
for i in range(verts): #constructing the graph
	graph.append([])

for j in range(edges): #have decided to store each edge as a tuple of destination, parchedness
	tripl = input()
	tripl = tripl.split()
	start = int(tripl[0]) #start node
	end = int(tripl[1]) #finish node
	parched = int(tripl[2]) #parchedness
	graph[start].append((end,parched)) #append to the graph (at start node) the tuple of parchedness and destination

def dijkstra( graph): #takes graph as argument because it is assumed that the source is the first node and sink is last node
	size = len(graph)
	distances =[]

	for i in range(size):
		distances.append(float('inf')) #setting distances to infinity

	distances[0]=0 #setting first and second distance to zero,the start node
	distances[1]=0
	preds =[] #predesessor node, I don't think I actually need this

	for x in range(size):
		preds.append(None) #None is the null value in python
		#special checking for none 'is None'

	treeR =[] #empty list for tree nodes
	bucketA = [] #is a list of lists indexed by distance maybe

	for i in range(size):
		bucketA.append([])

	bucketA[0].append(1) # first distance is zero, append first node to it
	nextA = 0

	while(nextA<len(bucketA)): #while we haven't reached the end of the bucket
		while (nextA<len(bucketA) and len(bucketA[nextA])==0): #while there is a node in the current bucket
			nextA+=1
		if(nextA==len(bucketA)):
			break
		nodeU = bucketA[nextA].pop() #pop off a node from the bucket
		treeR.append(nodeU) #append node to tree

		#for all successors of nodeU
		number_of_successors = len(graph[nodeU])
		for k in range(number_of_successors): #for all successors of node U
			nodeV = graph[nodeU][k]
			weightUV = distances[nodeU]+nodeV[1] #distance to u plus u to v
			if(weightUV<distances[nodeV[0]] ):#weight from u to v is given by the second value in the tuple
				if(distances[nodeV[0]]==float('inf')): #if equal to infinity
					bucketA[weightUV].append(nodeV[0]) #append to the correct bucket
				else: #distance is not infinite but less, need to move to correct bucket
					bucketA[distances[nodeV[0]]].remove(nodeV[0]) #remove from the bucket
					bucketA[weightUV].append(nodeV[0])
				distances[nodeV[0]]=weightUV #distance to u plus u to v
				preds[nodeV[0]]=nodeU
		#hit bucket with node in it
		#pop off node from bucket

	return distances[len(graph)-1]

#above code produces accurate graph, 
def fangorn(graph):
	# need to make a new graph that is double first graph
	double =[]
	for i in range(len(graph)*2): #double sized graph
		double.append([])
	graph_size = len(graph)
	for j in range(graph_size):#for each vertex
		vertexs=len(graph[j])
		for k in range(vertexs): #for each edge in vertex graph[j]
			start = j
			edge = graph[j][k]
			color = edge[1]
			destination = edge[0]
			newWeight = 1
			if(color ==1): #road has draught
				newWeight=0
				double[start].append((destination+graph_size,newWeight)) #tuple containing destination and newWeight
			double[start].append((destination,newWeight))
			double[start+graph_size].append((destination+graph_size,newWeight))
	answer = dijkstra(double) #calling dijkstra on new super graph
	if(answer==float('inf')):
		print(-1)
	else:
		print(answer)
	#return distance found
fangorn(graph)
