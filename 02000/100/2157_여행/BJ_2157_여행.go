// https://www.acmicpc.net/problem/2157

package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type SharedContext struct {
	n, m int
	g    [][]int
}

func Init() SharedContext {
	stdin := bufio.NewReaderSize(os.Stdin, 1<<20)

	line, _, _ := stdin.ReadLine()
	tokens := strings.Split(string(line), " ")
	n, _ := strconv.Atoi(tokens[0])
	m, _ := strconv.Atoi(tokens[1])
	k, _ := strconv.Atoi(tokens[2])

	g := make([][]int, n)
	for i := 0; i < n; i++ {
		g[i] = make([]int, n)
	}

	for 0 < k {
		k--

		line, _, _ := stdin.ReadLine()
		tokens := strings.Split(string(line), " ")
		u, _ := strconv.Atoi(tokens[0])
		v, _ := strconv.Atoi(tokens[1])
		w, _ := strconv.Atoi(tokens[2])

		if u < v && g[u-1][v-1] < w {
			g[u-1][v-1] = w
		}
	}

	return SharedContext{n, m, g}
}

func Plan(ctx SharedContext) int {
	dp := make([][]int, ctx.n)
	for i := 0; i < ctx.n; i++ {
		dp[i] = make([]int, ctx.m)
		for j := range dp[i] {
			dp[i][j] = -1
		}
	}

	dp[0][0] = 0
	for u := 0; u < ctx.n-1; u++ {
		for i := 0; i < ctx.m-1; i++ {
			if dp[u][i] == -1 {
				continue
			}

			for v := u + 1; v < ctx.n; v++ {
				if 0 < ctx.g[u][v] && dp[v][i+1] < dp[u][i]+ctx.g[u][v] {
					dp[v][i+1] = dp[u][i] + ctx.g[u][v]
				}
			}
		}
	}

	maxScore := 0
	for _, score := range dp[len(dp)-1] {
		if maxScore < score {
			maxScore = score
		}
	}

	return maxScore
}

func main() {
	fmt.Println(Plan(Init()))
}
