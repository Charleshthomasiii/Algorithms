# Code by Charles Thomas
# This program takes a series of key value pairs and inserts them into a heap corresponding dictionary (standard hash table)
# It allows for adjusting individual scores, which then update in the heap, and it also allows for removing all key value pairs that have a value less than a given integer.

#Input: The first line of the input is a number n, which is the number of key value pairs
# The next n lines are of the format s a, where s is the key and a is the value
#The next line is a number m, which is the number of subsequent lines in the input, which are of type:
# 1 s, b which tells that the score of candidate s has improved by b
# 2 k, which removes all key value pairs of value less than k

#Output: Every key value pair that has not been removed with all adjustments made.
class Data:
    def __init__(self, name, score, pos):
        self.name = name
        self.score = score
        self.pos = pos  # the pos in data marks its array in the heap
class SpartanHeap:  # every time an element moves in the heap, change its pos

    def __init__(self): #class constructor makes an empty list and sets its size to zero
        self.hList = [] #I decided to use the list collection because of its built in functions, and the time cost is marginal
        self.heapSize = 0



    def percolateDown(self, i): #see notes for percolate up, except it's percolating down
        while (i*2) <= self.heapSize:
            minchild = self.minChild(i)
            if self.hList[i].score > self.hList[minchild].score:
                tp=self.hList[i]
                self.hList[i] = self.hList[minchild]
                self.hList[minchild]=tp
                self.hList[i].pos = i
                self.hList[minchild].pos = minchild


            i =minchild


    def delMin(self):
        value = self.hList[1] #get the min value
        self.hList[1] = self.hList[self.heapSize] #bubble down the last value
        self.heapSize = self.heapSize - 1 #make heap one smaller
        self.hList.pop() #pop off last value from list
        self.percolateDown(1) #percolate down the swapped value
        return value #return the minimum value

    def buildHeap(self):
        self.heapSize = 0
        self.hList = [0]


    def minChild(self, i): #determining the minimum of two children
        double = i*2
        if double + 1 > self.heapSize:
            return double
        else:
            if self.hList[double].score < self.hList[double + 1].score:
                return double

        return double + 1

    def insert(self, j):  # inserting into the heap
        self.hList.append(j)  # appends to the end of the list
        j.pos = self.heapSize + 1  # records its position in its pos field
        self.heapSize = self.heapSize + 1  # add one to the size of the heap
        self.percolateUp(self.heapSize)  # call percolate up on what was just inserted
    def percolateUp(self, i): #percolates vertex in the heap upwards, but this is an array implementation so it just swaps index i and i//2 if needed
        while i // 2 > 0:
            if self.hList[i].score < self.hList[i // 2].score:#testing the score of each object in the heap
                tp = self.hList[i//2] #setting tp to smaller
                self.hList[i//2] = self.hList[i] #swapping
                self.hList[i] = tp #swapped

                self.hList[i].pos = i #fixing the object pos field so it's accessible by the hash/dictionary
                self.hList[i//2].pos = (i//2) #same as line above


            i = i // 2

def insertAll(heap, dictionary, name, score):  # takes heap, data, and hash table
    d = Data(name, score, 0)
    dictionary[name] = d
    heap.insert(d)
    return


def update(heap, dictionary, name, improvement):
    # get object in the dictionary
    # adjust the object's score by improvement
    # call percolate down on it so that the heap is maintained

    spartan = dictionary[name]
    spartan.score += improvement
    heap.percolateDown(spartan.pos)
    return


def threshold(heap, score):
    while heap.hList[1].score < score:
        heap.delMin()
    return


nameIndexDict = {}  # a python dictionary is a hash table, it has constant time lookups and is easy to use
bh = SpartanHeap()
bh.buildHeap()

candidates = int(input()) #number of entries
for x in range(candidates): #iterate over the next n candidates
    nex = input()
    nex = nex.split()
    insertAll(bh, nameIndexDict, nex[0], int(nex[1])) #insert them into the tree and dictionary

query = int(input()) #number of queries
for i in range(query):
    nex = input() #reads line by line
    nex=nex.split() #split each line by space
    if (int(nex[0]) == 1): #type 1 query
        update(bh, nameIndexDict, nex[1], int(nex[2]))
    else:
        threshold(bh, int(nex[1])) #type two query, calls threshold function
        print(len(bh.hList) - 1)