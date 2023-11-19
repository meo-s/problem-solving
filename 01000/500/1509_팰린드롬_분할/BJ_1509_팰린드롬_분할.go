// https://www.acmicpc.net/problem/1509

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
)

const NotEval = math.MaxInt

func min(a, b int) int {
	if b < a {
		return b
	} else {
		return a
	}
}

func IsPalindrome(s []byte, dp [][]int, l, r int) bool {
	if dp[l][r] == NotEval {
		if (r-l <= 1 && s[l] == s[r]) || (s[l] == s[r] && IsPalindrome(s, dp, l+1, r-1)) {
			dp[l][r] = 1
		} else {
			dp[l][r] = 0
		}
	}
	return dp[l][r] == 1
}

func main() {
	in := bufio.NewReaderSize(os.Stdin, 1<<12)
	s, _, _ := in.ReadLine()

	dp := make([]int, len(s))
	isPalindromeCache := make([][]int, len(s))
	for i := range isPalindromeCache {
		dp[i] = NotEval
		isPalindromeCache[i] = make([]int, len(s))
		for j := range isPalindromeCache[i] {
			isPalindromeCache[i][j] = NotEval
		}
	}

	for k := 0; k < len(s); k++ {
		for i := 0; i <= k; i++ {
			if IsPalindrome(s, isPalindromeCache, i, k) {
				if i == 0 {
					dp[k] = 1
					break
				} else {
					dp[k] = min(dp[k], dp[i-1]+1)
				}
			}
		}
	}

	fmt.Println(dp[len(s)-1])
}
