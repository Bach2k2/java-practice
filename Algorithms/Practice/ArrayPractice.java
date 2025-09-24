package Practice;

import java.util.Arrays;

public class ArrayPractice {

    public static void main(String[] args) {
        // int [] arr = {3,4,-1,-2, 5};
        // System.err.println("The number of pair: "+ Problem1.twoSumArrayV1(arr, 2));
        // System.err.println("The number of pair: "+ Problem1.twoSumArrayV2(arr, 2));

        // int[] prices = {7, 10, 1, 3, 6, 9, 2};
        // int[] pricesOfFeb = {1, 3, 6, 9, 11} ;
        // System.err.println("Max profit: "+ Problem2.maxProfitV1(prices));
        // System.err.println("Max profit of Feb: "+ Problem2.maxProfitV1(pricesOfFeb));

        int[] demo_arr = { 10, 3, 5, 6, 2 ,0};
        Problem3.getProductArr(demo_arr);

    }
}

// Problem 1: Pair with the given sum
class Problem1 {

    // naive approach
    static int twoSumArrayV1(int[] arr, int target) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == target) {
                    count++;
                }
            }
        }
        return count;
    }

    // Sorting and binary search
    static int binarySearch(int[] arr, int left, int right, int target) {
        while (left <= right) {
            // int mid = (left+right)/2; Integer overflow
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    static int twoSumArrayV2(int[] arr, int target) {
        Arrays.sort(arr); // Using quick sort?
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            int complement = target - arr[i];
            if (binarySearch(arr, i, arr.length, complement) != -1) {
                count++;
            }
        }
        return count;
    }
}

// Problem 2: Best time to sell and buy stock
class Problem2 {
    // (kỹ thuật tối ưu hóa duyệt tuyến tính)
    static int maxProfitV1(int[] prices) {
        int len = prices.length;
        int left = 0, right = left + 1;
        int max = 0;
        while (left <= right && right < len) {
            if (prices[left] < prices[right]) {
                max = Math.max(max, prices[right] - prices[left]);
                right++;
            } else {
                left = right;
                right = left + 1;
            }
        }
        return max;
    }
}

// Product array except itself
class Problem3 {
    public static int[] getProductArr(int[] arr) {
        int len = arr.length;
        int[] res = new int[len];
        Arrays.fill(res, 0);
        int product = 1;
        int zeros = 0;
        int idx = -1;
        for (int i = 0; i < len; i++) {
            if (arr[i] != 0) {
                product = product * arr[i];
            } else {
                zeros++;
                idx = i;
            }
        }
        System.err.printf("\nArray of product: [");
        if (zeros == 0) {
            for (int i = 0; i < len; i++) {
                res[i] = product / arr[i];
            }
        } else if(zeros ==1){
            res[idx] = product;
        }
        System.err.printf("\nArray of product: %s ", Arrays.toString(res));
        return res;
    }
}