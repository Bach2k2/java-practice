package Practice;
import java.util.Arrays;
public class ArrayPractice{

    public static void main(String[] args) {
        // int [] arr = {3,4,-1,-2, 5};
        // System.err.println("The number of pair: "+ Problem1.twoSumArrayV1(arr, 2));
        // System.err.println("The number of pair: "+ Problem1.twoSumArrayV2(arr, 2));

        int[] prices = {7, 10, 1, 3, 6, 9, 2};
        System.err.println("Max profit: "+ Problem2.maxProfit(prices));
    }
}
// Problem 1: Pair with the given sum
class Problem1{

    // naive approach
    static int twoSumArrayV1(int [] arr, int target){
        int count = 0;
        for (int i =0; i< arr.length; i++){
            for (int j = i+1; j<arr.length; j++){
                if(arr[i]+arr[j]== target){
                    count ++;
                }   
            }
        }
        return count;
    }


    // Sorting and binary search
    static int binarySearch(int []arr, int left, int right,int target){
        while (left<= right){
            // int mid = (left+right)/2;  Integer overflow
            int mid = left + (right - left )/ 2;
            if(arr[mid]== target){
                return mid;
            }else if (arr[mid]< target){
                left = mid +1;
            }else{
                right = mid -1;
            }
        }
        return -1;
    }
    static int twoSumArrayV2(int [] arr, int target){
        Arrays.sort(arr); // Using quick sort?
        int count =0;
        for (int i =0; i< arr.length; i++){
            int complement = target - arr[i];
            if (binarySearch(arr, i, arr.length, complement)!= -1){
                count ++;
            }
        }
        return count;
    }
}

// Problem 2: Best time to sell and buy stock
class Problem2{
    static int maxProfit(int [] prices){
        int len= prices.length;
        int left=0, right = left+1;
        int max =0;
        while (left<=right && right < len){
            if(prices[left]< prices[right]){
                max = Math.max(max, prices[right]-prices[left]);
                right++;
            }else{
                left = right;
                right = left +1;
            }
        }
        return max;
    }
}