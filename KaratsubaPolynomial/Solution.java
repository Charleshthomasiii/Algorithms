// Code by Charles Thomas
// The first line of the input is n, the degree of the two following polynomials.
//The second line consists of n+1 seperated integers, each indicating the coefficient of x in ascending order
//The third line consists of n+1 seperated integers, same as above for a different polynomial

//Output: This algorithm outputs the coefficients of the polynomial that results from multiplying the two input polynomials
//This algorithm makes use of the Karatsuba fast multiplication algorithm in time n^log2(3)

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        int power;
        power = reader.nextInt();
        int[] first = new int[power+1];
       int[] second = new int[power+1];
       // System.out.println(power);
        for(int i=0;i<power+1;i++){
            first[i]=reader.nextInt();
        }
        for(int i=0;i<power+1;i++){
        	second[i]=reader.nextInt();
        }
       // System.out.println(first+" "+ second);
       // System.out.println(power);
        int[] out = karatsuba(first, second);
        for (int i=0;i< out.length ;i++ ) {
            System.out.print(out[i]+" ");
        }
        

        // if ((power+1)%2!=0) {//its an odd number
        // 	F.add(first.get())
        // }
        //if # of terms is odd, then need to add last term

    }
    public static int[] karatsuba(int[] first, int[] second){
        int power = first.length-1;
        if (power<16) {
            //run smaller version
            int[] c = new int[2*(power+1)-1];
            //int [] c = new int[2*(power+1)-1];
            for(int i=0;i<power+1;i++){
                for(int j=0;j<power+1;j++){
                    c[i+j]=c[i+j] +first[i]*second[j];
                }
            }
            return c;         
        }
        else{
            int[] F =new int[(power+1)/2];
            int[] G =new int[(power+1)/2];
            int[] U;// =new ArrayList<Integer>();
            int[] V;// =new ArrayList<Integer>();
            int[] W;// =new ArrayList<Integer>();
            int[] fhi = new int[(power+1)/2];
            int[] ghi = new int[(power+1)/2];
            int[] flo = new int[(power+1)/2];
            int[] glo = new int[(power+1)/2];
            int k = (power+1)/2;
           // System.out.println("value of n (number of terms: "+(power+1));
           // System.out.println("value of k: "+k);

            for (int i=0;i<k ;i++ ) {
                int firsti  = first[i];
                int firsti2 = first[i+k];
                int secondi =second[i];
                int secondi2=second[i+k];
                F[i]=(firsti+firsti2);
                G[i]=(secondi+secondi2);
                flo[i]=(firsti);
                fhi[i]=(firsti2);
                glo[i]=(secondi);
                ghi[i]=(secondi2);
               // System.out.print(" flo: "+ firsti+" fhi: "+ firsti2+" glo: "+ secondi+" ghi: "+ secondi2);
            }
            // System.out.println();
            // System.out.println("Flo");
            // for (int i =0;i<flo.length ;i++ ) {
            //     System.out.print(flo[i]+ " ");
            // }
            // System.out.println("fhi");
            // for (int i =0;i<flo.length ;i++ ) {
            //     System.out.print(fhi[i]+ " ");
            // }
            // System.out.println();
            // System.out.println("glo");
            // for (int i =0;i<flo.length ;i++ ) {
            //     System.out.print(glo[i]+ " ");
            // }
            // System.out.println("ghi");
            // for (int i =0;i<flo.length ;i++ ) {
            //     System.out.print(ghi[i]+ " ");
            // }
            // System.out.println("F");
            // for (int i =0;i<flo.length ;i++ ) {
            //     System.out.print(F[i]+ " ");
            // }
            // System.out.println("G");
            // for (int i =0;i<flo.length ;i++ ) {
            //     System.out.print(G[i]+ " ");
            // }
            // System.out.println("flo "+flo);
            // System.out.println("fhi "+fhi);
            // System.out.println("glo "+glo);
            // System.out.println("ghi "+ghi);
            // System.out.println("F "+F);
            // System.out.println("G "+G);


            U = karatsuba(fhi,ghi);
            V = karatsuba(flo,glo);
            W = karatsuba(F,G);
            // System.out.println("U " +U);
            // System.out.println("V " +V);
            // System.out.println("W " +W);
            int[] answer =new int[(power+1)*2-1];//has extra
            int n =(power+1);
            for (int i=0;i<n/2 ;i++ ) {
                answer[i]=V[(i)];
                //answer.add(V.get(i));
            }
            for (int i=0;i<n-1 ;i++ ) {
                int value;

                if (i<(n/2)-1) { // n/2 is 16 //the first 15
                    value = W[(i)]-U[(i)]-V[(i)]+V[(i+(n/2))];//need indexs 16-30

                    //need index 16-31
                }
                else if(i==n/2-1){
                    value = W[(i)]-U[(i)]-V[(i)];
                   // System.out.print("got");
                }
                // else if(i==n/2){
                //     value = W.get(i)-U.get(i)-V.get(i);
                // }
                //16 15 1 15 16
                else{

                     value = W[(i)]-U[(i)]-V[(i)]+U[(i-(n/2))]; //ned indeces 0-16
                }
                answer[(n/2)+i]=value;
                //System.out.println(answer);
                //cant just tack on values, there is overlap between v and this and u and this

            }
            for (int i=0;i<n/2 ;i++ ) {
                answer[n+n/2+i-1]=U[(i-1+n/2)];
            }
            //System.out.println("answerlwength"+answer.length);

            return answer;
        }
    }

}

