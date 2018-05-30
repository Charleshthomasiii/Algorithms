// Code by Charles Thomas
// Input: The input consists of two lines, a, b, where each is a string
// Scoring matrix: 2 for a match, -1 for a mismatch. Blanks are allowed
// Output: THis algorithm outputs the score of the alignment, and the two strings with the blanks inserted.
//This algorithm makes use of memoization.
import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        ArrayList stringFirst = new ArrayList(); //arraylists are for the final two returned strings
        ArrayList stringSecond = new ArrayList();

       Scanner in = new Scanner(System.in); //getting input
        String first = in.next(); 
       int firstLength=first.length(); //getting lengths
        String second = in.next();
        int secondLength=second.length();
        // System.out.println(first+ firstLength);
        // System.out.println(second+secondLength);
        int[][] scoring = new int[firstLength+1][secondLength+1]; //dynamic programming sub problem array/matrix
        scoring[0][0] =0;
        for (int i=0;i<firstLength+1 ;i++ ) { //setting the entire mismatch columns
            scoring[i][0]=-i;
            
        }
        for (int i=0;i<secondLength+1 ;i++ ) {
            scoring[0][i]=-i; //initializing full mismatch columns
        }
        for(int j=1;j<firstLength+1;j++){ //the logic of constructing the matrix
            for(int k=1;k<secondLength+1;k++){
                int diff;
                char firstChar = first.charAt(j-1); //getting chars
                char secondChar = second.charAt(k-1);
                if (firstChar==secondChar) { //if equal, 2
                    diff=2;
                    
                } else { // else, -1
                    diff=-1;
                }

                int pos1=scoring[j-1][k]-1;//align with blank
                int pos2=scoring[j][k-1]-1; //align with blank
                int pos3=scoring[j-1][k-1]+ diff;//if same
                int max;
                if (pos1>=pos2 && pos1>=pos3) { //checking max of three
                    max=pos1;
                    
                }
                else if (pos2>=pos3 && pos2>=pos3) {
                    max=pos2;
                   
                }
                else{
                    max=pos3;
                   
                }
             scoring[j][k]=max;//setting max of the three
            }     
        }

        System.out.println(scoring[firstLength][secondLength]);
        int finalScore=scoring[firstLength][secondLength];
        int finalFirst = firstLength;
        int finalSecond =secondLength;
        while(finalFirst+finalSecond>0){
            if ((finalFirst>0 && finalSecond>0) && (finalScore-2 ==scoring[finalFirst-1][finalSecond-1])&& (first.charAt(finalFirst-1)==second.charAt(finalSecond-1)) ) {
                //its a match and if there is space left in the subproblem matrix

                finalScore=finalScore-2;
                finalFirst--;
                finalSecond--;
                stringFirst.add(first.charAt(finalFirst));
                stringSecond.add(second.charAt(finalSecond));
            }
            else if( (finalFirst!=0) && finalScore+1==scoring[finalFirst-1][finalSecond]){
                //checking shifted
                finalScore=finalScore+1;
                finalFirst--;
                stringFirst.add(first.charAt(finalFirst));
                stringSecond.add('_');
            }
            else if((finalSecond!=0) && finalScore+1==scoring[finalFirst][finalSecond-1]){
                //checking shifted

                finalScore=finalScore+1;
                finalSecond--;
                stringFirst.add('_');
                stringSecond.add(second.charAt(finalSecond));
            }
            else{
                //its a a mismatch
                finalScore=finalScore+1;
                finalFirst--;
                finalSecond--;
                stringFirst.add(first.charAt(finalFirst));
                stringSecond.add(second.charAt(finalSecond));
            }
        }

        for(int i=stringFirst.size()-1; i>-1;i--){
            System.out.print(stringFirst.get(i));
        }//printing final strings
        System.out.println();
        for(int i=stringSecond.size()-1; i>-1;i--){
            System.out.print(stringSecond.get(i));
        }
    }
}