package com.tpo.labs;

public class Main {
    public static void main(String[] args) {
        int[] arr = {10, 7, 19, 13};
        HeapSort.sort(arr);
        System.out.println();
        for(int i = 1; i < 75; i++){
            if (i<10){
                System.out.print(i + "  ");
            }else{
                System.out.print(i + " ");
            }

        }
    }
}
