package techstageone;

/**
 * Run all algorithm class methods in this class
 */
public class Main {
    public static void main(String[] args) throws Exception {

        FirstChallenge.getUsernames(2);
        FirstChallenge.getUsernameWithHighestCommentCount();
        FirstChallenge.getUsernamesSortedByRecordDate(2);

        SecondChallenge.getMaximumPairOfSocks(2, new int[] {1, 2, 1, 1}, new int[] {1, 4, 3, 2, 4});
          
    }
}