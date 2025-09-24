package Practice;

public class StringManipulationPractice {
    
    public static String reversedString(String str){
        StringBuilder sb = new StringBuilder();
        for (int i = str.length()-1; i>=0 ; i--){
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }

    public static boolean palindromicString(String str){
        // StringBuilder sb = new StringBuilder();
        int left = 0, right = str.length() - 1;
        while (left <= right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static boolean isAnagram(String str1, String str2){
        if (str1.length() != str2.length()) return false;
        int[] count = new int[128]; // Assuming ASCII character set
        for (char c : str1.toCharArray()) {
            System.err.println("Character: " + c);
            count[c]++;
        }
        for (char c : str2.toCharArray()) {
            count[c]--;
            if (count[c] < 0) {
                return false;
            }
        }
        return true;
    }

    public static int countCharOccurrences(String str, char target) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String reversed = reversedString("Hello");
        System.out.println("Reversed String: " + reversed);
        boolean palindromic = palindromicString("Hello");
        System.out.println("Palindromic String: " + palindromic);
        palindromic = palindromicString("racecar");
        System.out.println("Palindromic String: " + palindromic);

        boolean anagram = isAnagram("listen", "silend");
        System.out.println("Anagram: " + anagram);
    }
}
