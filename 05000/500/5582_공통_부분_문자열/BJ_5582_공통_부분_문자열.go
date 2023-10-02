// https://www.acmicpc.net/problem/5582

package main

import (
	"bufio"
	"fmt"
	"os"
)

func setup() ([]byte, []byte) {
	in := bufio.NewReaderSize(os.Stdin, 1<<16)
	s1, _, _ := in.ReadLine()
	s1 = clone(s1)
	s2, _, _ := in.ReadLine()
	return s1, clone(s2)
}

func clone[T any](src []T) []T {
	dst := make([]T, len(src))
	copy(dst, src)
	return dst
}

func max(values ...int) int {
	v := values[0]
	for i := 1; i < len(values); i++ {
		if v < values[i] {
			v = values[i]
		}
	}
	return v
}

func solve(s1 []byte, s2 []byte) {
	dp := make([][]int, len(s1)+1)
	for i := range dp {
		dp[i] = make([]int, len(s2)+1)
	}

	ans := 0
	for i := 1; i <= len(s1); i++ {
		for j := 1; j <= len(s2); j++ {
			if s1[i-1] == s2[j-1] {
				dp[i][j] = dp[i-1][j-1] + 1
				ans = max(ans, dp[i][j])
			}
		}
	}

	fmt.Println(ans)
}

func main() {
	solve(setup())
}
