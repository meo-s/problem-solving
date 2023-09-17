// https://www.acmicpc.net/problem/2092

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
)

const MODULAR = 1_000_000

type bufferedReader struct {
	*bufio.Reader
}

type sharedContext struct {
	t, a, s, b int
	numCounts  [4001]int
}

func (br *bufferedReader) readInt() (n int) {
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

func setup() *sharedContext {
	in := bufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}

	t := in.readInt()
	a := in.readInt()
	s := in.readInt()
	b := in.readInt()

	ctx := &sharedContext{t, a, s, b, [4001]int{}}
	for i := 0; i < a; i++ {
		ctx.numCounts[in.readInt()]++
	}

	return ctx
}

func solve(ctx *sharedContext) {
	dp := make([]int, ctx.a+1)
	dp[0] = 1

	for num := range ctx.numCounts {
		for size := ctx.a - 1; 0 <= size; size-- {
			if 0 < dp[size] {
				for i := 1; i <= ctx.numCounts[num] && size+i <= ctx.a; i++ {
					dp[size+i] = (dp[size+i] + dp[size]) % MODULAR
				}
			}
		}
	}

	ans := 0
	for i := ctx.s; i <= ctx.b; i++ {
		ans = (ans + dp[i]) % MODULAR
	}

	fmt.Printf("%d\n", ans)
}

func main() {
	solve(setup())
}
