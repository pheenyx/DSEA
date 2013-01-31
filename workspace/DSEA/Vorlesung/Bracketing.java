import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bracketing {

	public static void main(String[] args) {
		// query n, d_i
		Scanner s = new Scanner(System.in);
		System.out.println("# n?");
		int n = s.nextInt();
		List<Integer> dims = new ArrayList<>();
		for (int i = 0; i <= n; ++i) {
			System.out.println("# d_" + (i + 1) + "?");
			dims.add(s.nextInt());
		}
		s.close();

		int cost = bestBracketingCost(dims);
		System.out.println(cost);
	}

	public static int bestBracketingCost(List<Integer> dims) {
		// TODO
		return -1;
	}
}
