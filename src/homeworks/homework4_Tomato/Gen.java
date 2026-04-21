import java.io.*;

public class Gen {
    public static void main(String[] args) throws Exception {
        PrintWriter out = new PrintWriter("input.txt");

        int N = 100000;
        out.println(N + " 1000000000 1000000000");

        for (int i = 0; i < N; i++) {
            out.print("1000000000 ");
        }

        out.close();
    }
}