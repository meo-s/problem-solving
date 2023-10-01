// https://www.acmicpc.net/problem/2666

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"runtime"
)

const NEVAL = math.MaxInt

type Reader struct {
	*bufio.Reader
}

type sharedContext struct {
	n     int
	state [][]int
	goals []int
}

func (r Reader) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				r.ReadByte()
			}
			return
		}

		n = n*10 + int(b-'0')
	}
}

func abs(v int) int {
	if 0 <= v {
		return v
	} else {
		return -v
	}
}

func min(a, b int) int {
	if a <= b {
		return a
	} else {
		return b
	}
}

func makeTable(n int) [][]int {
	table := make([][]int, n)
	for i := range table {
		table[i] = make([]int, n)
		for j := range table[i] {
			table[i][j] = NEVAL
		}
	}

	return table
}

func setup() sharedContext {
	in := Reader{bufio.NewReaderSize(os.Stdin, 1<<10)}

	n := in.ReadInt()
	a := in.ReadInt()
	b := in.ReadInt()

	state := makeTable(n)
	state[a-1][b-1] = 0

	goals := make([]int, in.ReadInt())
	for i := range goals {
		goals[i] = in.ReadInt() - 1
	}

	return sharedContext{n, state, goals}
}

func solve(ctx sharedContext) {
	dp := make([][][]int, 0, 21)
	dp = append(dp, ctx.state)

	for i := 1; i <= len(ctx.goals); i++ {
		dp = append(dp, makeTable(ctx.n))
		g := ctx.goals[i-1]

		for l := 0; l < ctx.n-1; l++ {
			for r := l + 1; r < ctx.n; r++ {
				if dp[i-1][l][r] != NEVAL {
					if g <= l || g < r {
						dp[i][g][r] = min(dp[i][g][r], dp[i-1][l][r]+abs(g-l))
					}
					if r <= g || l < g {
						dp[i][l][g] = min(dp[i][l][g], dp[i-1][l][r]+abs(g-r))
					}
				}
			}
		}
	}

	ans := math.MaxInt
	for l := 0; l < ctx.n-1; l++ {
		ans = min(ans, dp[len(dp)-1][l][ctx.goals[len(ctx.goals)-1]])
	}
	for r := 0; r < ctx.n; r++ {
		ans = min(ans, dp[len(dp)-1][ctx.goals[len(ctx.goals)-1]][r])
	}

	fmt.Println(ans)
}

func main() {
	solve(setup())
}
