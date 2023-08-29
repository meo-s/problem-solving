// https://www.acmicpc.net/problem/2259

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"sort"
	"strconv"
)

type Fin struct {
	*bufio.Reader
}

type SharedContext struct {
	N     int
	S     int
	moles [][3]int
}

func (fin *Fin) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
	sign := 1

	for {
		b, err := fin.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if b == '-' {
				sign = -1
				continue
			}
			if GOOS_IS_WINDOWS && b == '\r' {
				fin.ReadByte()
			}
			return sign * n

		}

		n = n*10 + int(b-'0')
	}
}

func Max[T int | int32 | int64](a, b T) T {
	if b <= a {
		return a
	} else {
		return b
	}
}

func Reachable(from, to [3]int, spd int) bool {
	dx := float64(to[0] - from[0])
	dy := float64(to[1] - from[1])
	dt := float64(to[2] - from[2])
	return dx*dx+dy*dy <= float64(spd*spd)*dt*dt
}

func Init() SharedContext {
	in := Fin{bufio.NewReaderSize(os.Stdin, 1<<20)}

	N := in.ReadInt()
	S := in.ReadInt()
	moles := make([][3]int, N+1)
	for i := 1; i <= N; i++ {
		x := in.ReadInt()
		y := in.ReadInt()
		t := in.ReadInt()
		moles[i] = [3]int{x, y, t}
	}

	sort.Slice(moles, func(i, j int) bool {
		return moles[i][2] < moles[j][2]
	})

	return SharedContext{N, S, moles}
}

func Solve(ctx SharedContext) {
	dp := make([]int, ctx.N+1)
	for i := range dp {
		dp[i] = -1
	}

	dp[0] = 0
	for k := 1; k <= ctx.N; k++ {
		for i := 0; i < k; i++ {
			if dp[i] != -1 && Reachable(ctx.moles[i], ctx.moles[k], ctx.S) {
				dp[k] = Max(dp[k], dp[i]+1)
			}
		}
	}

	ans := dp[0]
	for i := 1; i <= ctx.N; i++ {
		ans = Max(ans, dp[i])
	}

	fmt.Println(strconv.Itoa(ans))
}

func main() {
	Solve(Init())
}
