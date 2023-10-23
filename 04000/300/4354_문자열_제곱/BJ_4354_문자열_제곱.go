// https://www.acmicpc.net/problem/4354

package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func max(a, b int) int {
	if a < b {
		return b
	} else {
		return a
	}
}

func preprocess(s []byte, dp []int) {
	dp[0] = 0
	for i, j := 1, 0; i < len(s); i++ {
		for 0 < j && s[i] != s[j] {
			j = dp[j-1]
		}
		if s[i] == s[j] {
			j++
			dp[i] = j
		} else {
			dp[i] = 0
		}
	}
}

func main() {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	sb := &strings.Builder{}
	dp := make([]int, 1_000_000)
	for {
		s, _, _ := in.ReadLine()
		if s[0] == '.' {
			break
		}

		preprocess(s, dp)
		if dp[len(s)-1] == 0 || len(s)%(len(s)-dp[len(s)-1]) != 0 {
			sb.WriteString("1\n")
		} else {
			sb.WriteString(strconv.Itoa(max(len(s)/(len(s)-dp[len(s)-1]), 1)))
			sb.WriteByte('\n')
		}
	}

	fmt.Print(sb.String())
}
