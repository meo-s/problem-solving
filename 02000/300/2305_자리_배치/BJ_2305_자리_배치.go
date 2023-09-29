// https://www.acmicpc.net/problem/2305

package main

import "fmt"

const NEVAL = -1

func setup() (n, k int) {
	fmt.Scan(&n, &k)
	return n, k - 1
}

func count(dp [][][]int, n, k, e, beg, end int) int {
	if beg == end {
		return 1
	}

	if dp[e][beg][end-1] == NEVAL {
		if e < beg || end <= e {
			dp[e][beg][end-1] = count(dp, n, k, e, beg+1, end)

			if beg+1 < end && beg != k && beg+1 != k {
				dp[e][beg][end-1] += count(dp, n, k, e, beg+2, end)
			}
		} else {
			dp[e][beg][end-1] = count(dp, n, k, e, beg, e) * count(dp, n, k, e, e+1, end)

			if e != k {
				if beg <= e-1 && e-1 != k {
					dp[e][beg][end-1] += count(dp, n, k, e-1, beg, e) * count(dp, n, k, e-1, e+1, end)
				}
				if e+1 < end && e+1 != k {
					dp[e][beg][end-1] += count(dp, n, k, e+1, beg, e) * count(dp, n, k, e+1, e+1, end)
				}
			}
		}
	}

	return dp[e][beg][end-1]
}

func solve(n, k int) {
	dp := make([][][]int, n)
	for i := range dp {
		dp[i] = make([][]int, n)
		for j := range dp {
			dp[i][j] = make([]int, n)
			for k := range dp {
				dp[i][j][k] = NEVAL
			}
		}
	}

	ans := 0
	for i := 0; i < n; i++ {
		ans += count(dp, n, k, i, 0, n)
	}

	fmt.Println(ans)
}

func main() {
	solve(setup())
}
