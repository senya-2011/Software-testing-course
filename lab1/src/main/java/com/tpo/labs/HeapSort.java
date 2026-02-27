package com.tpo.labs;

import java.util.ArrayList;
import java.util.List;


public class HeapSort {

    public static List<String> trace = new ArrayList<String>();

    public static void sort(int[] arr) {
        trace.add("Т1: Старт сортировки");
        System.out.print("T1 ");
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            trace.add("Т2: Берем последний внутренний узел. i: " + i);
            System.out.print("T2 ");
            heapify(arr, n, i);
        }

        trace.add("Т8: Начало исключения элементов");
        System.out.print("T8 ");
        for (int i = n - 1; i > 0; i--) {
            int swap = arr[0];
            arr[0] = arr[i];
            arr[i] = swap;
            trace.add("Т9: Максимальный элемент в конце массива. i: "+ i);
            System.out.print("T9 ");

            trace.add("Т10: Берем последний внутренний узел после удаления");
            System.out.print("T10 ");
            heapify(arr, i, 0);
        }
        trace.add("Т11: Конец сортировки");
        System.out.print("T11 ");
    }

    static void heapify(int arr[], int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;


        if (left < n) {
            trace.add("Т3: проверяем левый потомок. i: " + left);
            System.out.print("T3 ");
            if (arr[left] > arr[largest]) {
                trace.add("Т5: левый потомок больше");
                System.out.print("T5 ");
                largest = left;
            }
        }

        if (right < n) {
            trace.add("Т4: проверяем правый потомок. i: " + right);
            System.out.print("T4 ");
            if (arr[right] > arr[largest]) {
                trace.add("Т5: правый потомок больше");
                System.out.print("T5 ");
                largest = right;
            }
        }

        if (largest != i) {

            trace.add("Т6: меняем " + i + " с " + largest);
            System.out.print("T6 ");
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            trace.add("Т7: рекрусивно проверить поддерево");
            System.out.print("T7 ");
            heapify(arr, n, largest);
        }
    }
}