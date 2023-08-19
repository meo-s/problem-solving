// https://www.acmicpc.net/problem/1983

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"runtime"
)

const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
const NEVAL = math.MinInt

var N int
var box [2][]int
var dp [][][]int

type Fin struct {
	*bufio.Reader
}

func (in Fin) ReadInt() (n int) {
	sign := 1

	for {
		b, err := in.ReadByte()

		if b == ' ' || b == '\r' || b == '\n' || err != nil {
			if b == '\r' && GOOS_IS_WINDOWS {
				in.ReadByte()
			}
			return n * sign
		}

		if b == '-' {
			sign = -1
		} else {
			n = n*10 + int(b-'0')
		}
	}
}

func Init() {
	in := Fin{bufio.NewReaderSize(os.Stdin, 1<<20)}

	N = in.ReadInt()
	for i := 0; i < 2; i++ {
		box[i] = make([]int, 0, N)
		for j := 0; j < N; j++ {
			n := in.ReadInt()
			if n != 0 {
				box[i] = append(box[i], n)
			}
		}
	}

	dp = make([][][]int, N)
	for i := 0; i < N; i++ {
		dp[i] = make([][]int, N)
		for j := 0; j < N; j++ {
			dp[i][j] = make([]int, N)
			for k := 0; k < N; k++ {
				dp[i][j][k] = NEVAL
			}
		}
	}
}

func Max[T int](a, b T) T {
	if b <= a {
		return a
	} else {
		return b
	}
}

func Match(ci, ti, bi int) int {
	if ci == N || ti == len(box[0]) || bi == len(box[1]) {
		return 0
	}

	if dp[ci][ti][bi] == NEVAL {
		dp[ci][ti][bi] = box[0][ti]*box[1][bi] + Match(ci+1, ti+1, bi+1)
		if (ci+1)+(len(box[0])-ti) <= N {
			dp[ci][ti][bi] = Max(dp[ci][ti][bi], Match(ci+1, ti, bi+1))
		}
		if (ci+1)+(len(box[1])-bi) <= N {
			dp[ci][ti][bi] = Max(dp[ci][ti][bi], Match(ci+1, ti+1, bi))
		}
	}

	return dp[ci][ti][bi]
}

func Solve() {
	fmt.Println(Match(0, 0, 0))
}

func main() {
	Init()
	Solve()
}
