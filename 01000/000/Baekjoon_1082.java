// https://www.acmicpc.net/problem/1082

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

public class Baekjoon_1082 {
    public static class Number {
        int n;
        int price;
    }

    public static void main(String[] args) throws IOException {
        var stdin = new BufferedReader(new InputStreamReader(System.in));
        var stdout = new BufferedWriter(new OutputStreamWriter(System.out));

        final var N = Integer.parseInt(stdin.readLine());
        final var prices = stdin.readLine().split(" ");

        var nums = new Number[N];
        for (int i = 0; i < N; ++i) {
            nums[i] = new Number();
            nums[i].n = i;
            nums[i].price = Integer.parseInt(prices[i]);
        }

        Arrays.sort(nums, new Comparator<Number>() {
            @Override
            public int compare(Number lhs, Number rhs) {
                return lhs.price - rhs.price;
            }
        });

        var balance = Integer.parseInt(stdin.readLine()) - nums[0].price;
        var room = new Vector<Integer>();
        room.add(0);
        if (nums[0].n == 0) {
            if (1 < nums.length) {
                final var price_diff = nums[1].price - nums[0].price;
                if (price_diff <= balance) {
                    room.set(0, 1);
                    balance -= price_diff;
                }
            }
        }

        if (nums[room.get(0)].n != 0) {
            while (nums[0].price <= balance) {
                balance -= nums[0].price;
                room.add(0);
            }

            var i = -1;
            while (i < room.size() - 1) {
                i += 1;
                for (int j = 0; j < nums.length; ++j) {
                    if (nums[j].n <= nums[room.get(i)].n) {
                        continue;
                    }

                    final var price_diff = nums[j].price - nums[room.get(i)].price;
                    if (price_diff <= balance) {
                        balance -= price_diff;
                        room.set(i, j);
                    }
                }
            }
        }

        for (var n : room) {
            stdout.write(nums[n].n + "");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
