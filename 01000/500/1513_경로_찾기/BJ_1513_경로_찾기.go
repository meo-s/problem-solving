// https://www.acmicpc.net/problem/1513

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strconv"
	"strings"
)

const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
const MODULAR = 1_000_007

var br = bufio.NewReaderSize(os.Stdin, 1<<20)
var N, M, C int
var dp [][][][]int
var arcades [][]int

func ReadInt() (n int) {
	for {
		if b, err := br.ReadByte(); err != nil {
			return
		} else {
			if b == '\r' || b == '\n' || b == ' ' {
				if b == '\r' && GOOS_IS_WINDOWS {
					br.ReadByte()
				}
				return
			}

			n = n*10 + int(b-'0')
		}
	}
}

func Init() {
	N = ReadInt()
	M = ReadInt()
	C = ReadInt()

	arcades = make([][]int, N+1)
	for y := range arcades {
		arcades[y] = make([]int, M+1)
	}

	for i := 1; i <= C; i++ {
		y := ReadInt()
		x := ReadInt()
		arcades[y][x] = i
	}

	dp = make([][][][]int, N+1)
	for y := range dp {
		dp[y] = make([][][]int, M+1)
		for x := range dp[y] {
			dp[y][x] = make([][]int, C+1)
			for c := range dp[y][x] {
				dp[y][x][c] = make([]int, C+1)
			}
		}
	}

	if arcades[1][1] == 0 {
		dp[1][1][0][0] = 1
	} else {
		dp[1][1][arcades[1][1]][1] = 1
	}
}

func Solve() {
	for y := 1; y <= N; y++ {
		for x := 1; x <= M; x++ {
			if arcades[y][x] == 0 {
				for c := 0; c <= C; c++ {
					for i := 0; i <= c; i++ {
						dp[y][x][c][i] = (dp[y][x][c][i] + dp[y-1][x][c][i] + dp[y][x-1][c][i]) % MODULAR
					}
				}
			} else {
				k := arcades[y][x]
				for c := 0; c < k; c++ {
					for i := 0; i <= c; i++ {
						dp[y][x][k][i+1] = (dp[y][x][k][i+1] + dp[y-1][x][c][i] + dp[y][x-1][c][i]) % MODULAR
					}
				}
			}
		}
	}

	cnts := make([]int, C+1)
	for c := 0; c <= C; c++ {
		for i := 0; i <= c; i++ {
			cnts[i] = (cnts[i] + dp[N][M][c][i]) % MODULAR
		}
	}

	sb := strings.Builder{}
	for i := 0; i <= C; i++ {
		sb.WriteString(strconv.Itoa(cnts[i]))
		sb.WriteByte(' ')
	}

	fmt.Println(sb.String())
}

func main() {
	Init()
	Solve()
}
