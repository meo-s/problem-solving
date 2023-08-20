// https://www.acmicpc.net/problem/27449

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"sort"
	"strconv"
)

const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
const (
	L = 0
	R = 1
)

var N, M int
var updrafts [2][]Updraft
var dp [][][2]State

type Updraft struct{ x, p int }
type State struct{ x, y, t int }

type Fin struct {
	*bufio.Reader
}

func (in Fin) ReadInt() (n int) {
	sign := 1

	for {
		b, err := in.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if b == '-' && n == 0 {
				sign = -1
				continue
			}
			if b == '\r' && GOOS_IS_WINDOWS {
				in.ReadByte()
			}
			return n * sign
		}

		n = n*10 + int(b-'0')
	}
}

func Max[T int](a, b T) T {
	if b <= a {
		return a
	} else {
		return b
	}
}

func Init() {
	in := Fin{bufio.NewReaderSize(os.Stdin, 1<<20)}

	N = in.ReadInt()
	M = in.ReadInt()

	for i := 0; i < N; i++ {
		x, p := in.ReadInt(), in.ReadInt()
		if x < 0 {
			updrafts[L] = append(updrafts[L], Updraft{x, p})
		} else {
			updrafts[R] = append(updrafts[R], Updraft{x, p})
		}
	}

	sort.Slice(updrafts[L], func(i, j int) bool {
		return updrafts[L][j].x < updrafts[L][i].x
	})
	sort.Slice(updrafts[R], func(i, j int) bool {
		return updrafts[R][i].x < updrafts[R][j].x
	})

	dp = make([][][2]State, len(updrafts[L])+1)
	for i := range dp {
		dp[i] = make([][2]State, len(updrafts[R])+1)
	}
}

func Solve() {
	maxFloatTime := M
	dp[0][0][L] = State{x: 0, y: M, t: 0}
	dp[0][0][R] = State{x: 0, y: M, t: 0}

	for k := 0; k <= N; k++ {
		for i := 0; i <= k; i++ {
			if len(updrafts[L]) < i || len(updrafts[R]) < k-i {
				continue
			}

			for side := 0; side < 2; side++ {
				if dp[i][k-i][side].y == 0 {
					continue
				}

				cx := dp[i][k-i][side].x
				cy := dp[i][k-i][side].y
				ct := dp[i][k-i][side].t

				if i < len(updrafts[L]) && cx-updrafts[L][i].x < cy {
					nx := updrafts[L][i].x
					ny := cy - (cx - nx) + updrafts[L][i].p

					if dp[i+1][k-i][L].y < ny {
						ns := State{x: nx, y: ny, t: ct + (cx - nx)}
						dp[i+1][k-i][L] = ns
						maxFloatTime = Max(maxFloatTime, ns.t+ns.y)
					}
				}

				if k-i < len(updrafts[R]) && updrafts[R][k-i].x-cx < cy {
					nx := updrafts[R][k-i].x
					ny := cy - (nx - cx) + updrafts[R][k-i].p

					if dp[i][k-i+1][R].y < ny {
						ns := State{x: nx, y: ny, t: ct + (nx - cx)}
						dp[i][k-i+1][R] = ns
						maxFloatTime = Max(maxFloatTime, ns.t+ns.y)
					}
				}
			}
		}
	}

	fmt.Println(strconv.Itoa(maxFloatTime) + "\n")
}

func main() {
	Init()
	Solve()
}
