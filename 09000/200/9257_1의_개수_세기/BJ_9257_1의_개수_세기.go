// https://www.acmicpc.net/problem/9257

package main

import (
	"fmt"
)

const NotEval = -1

var dp [60][60]int64

func init() {
	for i := 0; i < len(dp); i++ {
		for j := 0; j < len(dp[i]); j++ {
			dp[i][j] = NotEval
		}
	}
}

func nr(n, r int) int64 { // nCr
	if dp[n][r] == NotEval {
		if r == n || r == 0 {
			dp[n][r] = 1
		} else {
			dp[n][r] = nr(n-1, r) + nr(n-1, r-1)
		}
	}
	return dp[n][r]
}

func f(x int64) (ones int64) {
	onesInDigits := func(digits int) (ones int64) {
		for i := 1; i <= digits; i++ {
			ones += int64(i) * nr(digits, i)
		}
		return
	}

	if 0 < x {
		k := 0
		for i := 60; ; i-- {
			if (x & (1 << i)) != 0 {
				k = i
				break
			}
		}
		ones = 1<<k + onesInDigits(k)<<1
		fixedOnes := int64(1)
		for i := k - 1; 0 <= i; i-- {
			if (x & (1 << i)) != 0 {
				fixedOnes++
			} else {
				ones -= (fixedOnes+1)*(1<<i) + onesInDigits(i)
			}
		}
	}

	return
}

func main() {
	var a, b int64
	fmt.Scan(&a, &b)
	fmt.Println(f(b) - f(a-1))
}
