//Code by Charles Thomas
//Input: First line is a number n, which contains the size of the database.
//The next n lines are of the form a k, where a is the key, and k is the value
//The next line contains a number m, the number of queries to the database.
//The next m lines are of the form a b, where a, b are keys

//Output:  the key and value pairs between the lexigraphical order of the two keys.
//The length of each key does not exceed ten characters, and the size of k<10^9.

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.util.Scanner;



public class Solution {
   public static void main(String[] args) {
      
      TwoThreeTree tree = new TwoThreeTree();
      
      try{ 
        // FileReader flr = new FileReader(args[0]);
         BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         int j= Integer.parseInt(reader.readLine());
         for(int i=0; i<j;i++){
            String[] splited= reader.readLine().split(" "); //not sure how fast this is/ if its faster to just use a scanner not bufferreader
            insert(splited[0], Integer.parseInt(splited[1]), tree);
            //System.out.println(splited[0]);
         }
         int k= Integer.parseInt(reader.readLine());//reads how many comparisons needed
         for(int l=0; l<k;l++){
            String[] splited= reader.readLine().split(" "); //not sure how fast this is/ if its faster to just use a scanner not bufferreader
            if(splited[0].compareTo(splited[1])>0){
               printTree(tree.root, tree.height, splited[1], splited[0]);
            }
            else{
               printTree(tree.root, tree.height, splited[0], splited[1]);
            }

         }
         reader.close();
      }catch(IOException e){
         e.printStackTrace();
      }
   }

   static void printTree(Node tree, int height, String lower, String upper){

      if(height==0){//if its a leaf its gonna get printed
         LeafNode leaf = (LeafNode) tree;
         System.out.println(leaf.guide+" "+leaf.value);
      }
      else{ //recursively calls printtree if it has to
         InternalNode intern = (InternalNode) tree;
         if((intern.child0.guide.compareTo(lower)>= 0) && (intern.child0.guide.compareTo(upper)<=0)) {
             printTree( intern.child0, height-1,lower,upper); //if the child lies between the range exclusive upper
         }//Makes sure the guide is above or equal to the lower bound, and strictly less than the upper bound
         if((intern.child1.guide.compareTo(lower)>= 0) && (intern.child1.guide.compareTo(upper)<=0)) {
             printTree( intern.child1, height-1,lower,upper); //if the child lies between the range exclusive upper
         }
         if( (intern.child2!=null) && ((intern.child2.guide.compareTo(lower)>= 0) && (intern.child2.guide.compareTo(upper)<=0))) {//if it exists and lies between the bounds
             printTree( intern.child2, height-1,lower,upper); //if the child lies between the range exclusive upper
         }

         //its printing one more than necessary because when it is on the leaf level it prints the one leaf greater than the range
         
         if(intern.child0.guide.compareTo(upper)>0){ 
            if(intern.child0 instanceof InternalNode){ //must not be a leaf else it could print a leaf above the upper limit
               printTree( intern.child0, height-1,lower,upper);
            }           
         }
         else if(intern.child1.guide.compareTo(upper)>0){
            if(intern.child1 instanceof InternalNode){
               printTree( intern.child1, height-1,lower,upper);
            }
         }
         else if((intern.child2!=null)&&(intern.child2.guide.compareTo(upper)>0)){
            if(intern.child2 instanceof InternalNode){
               printTree( intern.child2, height-1,lower,upper);
            }
         }
      }
   }

   static void insert(String key, int value, TwoThreeTree tree) {
   // insert a key value pair into tree (overwrite existsing value
   // if key is already present)

      int h = tree.height;

      if (h == -1) {
          LeafNode newLeaf = new LeafNode();
          newLeaf.guide = key;
          newLeaf.value = value;
          tree.root = newLeaf; 
          tree.height = 0;
      }
      else {
         WorkSpace ws = doInsert(key, value, tree.root, h);

         if (ws != null && ws.newNode != null) {
         // create a new root

            InternalNode newRoot = new InternalNode();
            if (ws.offset == 0) {
               newRoot.child0 = ws.newNode; 
               newRoot.child1 = tree.root;
            }
            else {
               newRoot.child0 = tree.root; 
               newRoot.child1 = ws.newNode;
            }
            resetGuide(newRoot);
            tree.root = newRoot;
            tree.height = h+1;
         }
      }
   }

   static WorkSpace doInsert(String key, int value, Node p, int h) {
   // auxiliary recursive routine for insert

      if (h == 0) {
         // we're at the leaf level, so compare and 
         // either update value or insert new leaf

         LeafNode leaf = (LeafNode) p; //downcast
         int cmp = key.compareTo(leaf.guide);

         if (cmp == 0) {
            leaf.value = value; 
            return null;
         }

         // create new leaf node and insert into tree
         LeafNode newLeaf = new LeafNode();
         newLeaf.guide = key; 
         newLeaf.value = value;

         int offset = (cmp < 0) ? 0 : 1;
         // offset == 0 => newLeaf inserted as left sibling
         // offset == 1 => newLeaf inserted as right sibling

         WorkSpace ws = new WorkSpace();
         ws.newNode = newLeaf;
         ws.offset = offset;
         ws.scratch = new Node[4];

         return ws;
      }
      else {
         InternalNode q = (InternalNode) p; // downcast
         int pos;
         WorkSpace ws;

         if (key.compareTo(q.child0.guide) <= 0) {
            pos = 0; 
            ws = doInsert(key, value, q.child0, h-1);
         }
         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
            pos = 1;
            ws = doInsert(key, value, q.child1, h-1);
         }
         else {
            pos = 2; 
            ws = doInsert(key, value, q.child2, h-1);
         }

         if (ws != null) {
            if (ws.newNode != null) {
               // make ws.newNode child # pos + ws.offset of q

               int sz = copyOutChildren(q, ws.scratch);
               insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
               if (sz == 2) {
                  ws.newNode = null;
                  ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
               }
               else {
                  ws.newNode = new InternalNode();
                  ws.offset = 1;
                  resetChildren(q, ws.scratch, 0, 2);
                  resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
               }
            }
            else if (ws.guideChanged) {
               ws.guideChanged = resetGuide(q);
            }
         }

         return ws;
      }
   }


   static int copyOutChildren(InternalNode q, Node[] x) {
   // copy children of q into x, and return # of children

      int sz = 2;
      x[0] = q.child0; x[1] = q.child1;
      if (q.child2 != null) {
         x[2] = q.child2; 
         sz = 3;
      }
      return sz;
   }

   static void insertNode(Node[] x, Node p, int sz, int pos) {
   // insert p in x[0..sz) at position pos,
   // moving existing extries to the right

      for (int i = sz; i > pos; i--)
         x[i] = x[i-1];

      x[pos] = p;
   }

   static boolean resetGuide(InternalNode q) {
   // reset q.guide, and return true if it changes.

      String oldGuide = q.guide;
      if (q.child2 != null)
         q.guide = q.child2.guide;
      else
         q.guide = q.child1.guide;

      return q.guide != oldGuide;
   }


   static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
   // reset q's children to x[pos..pos+sz), where sz is 2 or 3.
   // also resets guide, and returns the result of that

      q.child0 = x[pos]; 
      q.child1 = x[pos+1];

      if (sz == 3) 
         q.child2 = x[pos+2];
      else
         q.child2 = null;

      return resetGuide(q);
   }
}


class Node {
   String guide;
   // guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
   Node child0, child1, child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
   // guide points to the key

   int value;
}

class TwoThreeTree {
   Node root;
   int height;

   TwoThreeTree() {
      root = null;
      height = -1;
   }
}

class WorkSpace {
// this class is used to hold return values for the recursive doInsert
// routine

   Node newNode;
   int offset;
   boolean guideChanged;
   Node[] scratch;
}
