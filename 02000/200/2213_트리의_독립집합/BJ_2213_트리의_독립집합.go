// https://www.acmicpc.net/problem/2213

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"sort"
	"strconv"
	"strings"
)

const NEVAL = -1

const (
	WITH    = 0
	WITHOUT = 1
)

type SharedContext struct {
	n int
	w []int
	g [][]int
}

type BufferedReader struct {
	*bufio.Reader
}

func (br *BufferedReader) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

	for {
		b, err := br.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				br.ReadByte()
			}

			return
		}

		n = n*10 + int(b-'0')
	}
}

func setup() SharedContext {
	in := BufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}

	n := in.ReadInt()

	w := make([]int, n)
	for i := range w {
		w[i] = in.ReadInt()
	}

	g := make([][]int, n)
	for i := range g {
		g[i] = make([]int, 0, n)
	}
	for i := 0; i < n-1; i++ {
		u := in.ReadInt() - 1
		v := in.ReadInt() - 1
		g[u] = append(g[u], v)
		g[v] = append(g[v], u)
	}

	return SharedContext{n, w, g}
}

func max(a, b int) int {
	if b <= a {
		return a
	} else {
		return b
	}
}

func travel(ctx SharedContext, dp [2][]int, u int, p int) {
	if dp[WITH][u] == NEVAL {
		dp[WITH][u] = ctx.w[u]
		dp[WITHOUT][u] = 0

		for _, v := range ctx.g[u] {
			if v == p {
				continue
			}

			travel(ctx, dp, v, u)
			dp[WITH][u] += dp[WITHOUT][v]
			dp[WITHOUT][u] += max(dp[WITH][v], dp[WITHOUT][v])
		}
	}
}

func trace(ctx SharedContext, dp [2][]int, u [2]int, p int, vertices *[]int) {
	if u[0] == WITH {
		*vertices = append(*vertices, u[1]+1)
	}

	for _, v := range ctx.g[u[1]] {
		if v != p {
			if u[0] == WITH || dp[WITH][v] <= dp[WITHOUT][v] {
				trace(ctx, dp, [2]int{WITHOUT, v}, u[1], vertices)
			} else {
				trace(ctx, dp, [2]int{WITH, v}, u[1], vertices)
			}
		}
	}
}

func solve(ctx SharedContext) {
	dp := [2][]int{}
	dp[0] = make([]int, ctx.n)
	dp[1] = make([]int, ctx.n)

	for i := 0; i < ctx.n; i++ {
		dp[0][i] = NEVAL
		dp[1][i] = NEVAL
	}

	travel(ctx, dp, 0, -1)

	var vertices []int
	if dp[WITHOUT][0] <= dp[WITH][0] {
		trace(ctx, dp, [2]int{WITH, 0}, -1, &vertices)
	} else {
		trace(ctx, dp, [2]int{WITHOUT, 0}, -1, &vertices)
	}

	ans := strings.Builder{}

	ans.WriteString(strconv.Itoa(max(dp[WITH][0], dp[WITHOUT][0])))
	ans.WriteByte('\n')

	sort.Slice(vertices, func(i, j int) bool { return vertices[i] < vertices[j] })
	for _, vertex := range vertices {
		ans.WriteString(strconv.Itoa(vertex))
		ans.WriteByte(' ')
	}

	fmt.Println(ans.String())
}

func main() {
	solve(setup())
}
