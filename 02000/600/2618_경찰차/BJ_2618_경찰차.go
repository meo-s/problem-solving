// https://www.acmicpc.net/problem/2618

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"runtime"
	"strconv"
	"strings"
)

type Fin struct{ *bufio.Reader }
type State struct {
	prevLoc   [2]int
	movements int
}

const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
const NEVAL = -1
const (
	ONE = 0
	TWO = 1
)

var N, W int
var locs [][2]int
var dp [][]State

func (fin Fin) ReadInt() (n int) {
	for {
		b, err := fin.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if b == '\r' && GOOS_IS_WINDOWS {
				fin.ReadByte()
			}
			return
		}

		n = n*10 + int(b-'0')
	}
}

func Init() {
	in := Fin{bufio.NewReaderSize(os.Stdin, 1<<20)}

	N = in.ReadInt()
	W = in.ReadInt()

	locs = make([][2]int, 0, W+2)
	locs = append(locs, [2]int{1, 1})
	locs = append(locs, [2]int{N, N})
	for i := 0; i < W; i++ {
		locs = append(locs, [2]int{in.ReadInt(), in.ReadInt()})
	}

	dp = make([][]State, W+2)
	for i := 0; i < W+2; i++ {
		dp[i] = make([]State, W+2)
		for j := range dp[i] {
			if i != 0 || j != 1 {
				dp[i][j].movements = math.MaxInt32
			}
		}
	}
}

func Abs(v int) int {
	if 0 <= v {
		return v
	} else {
		return -v
	}
}

func CalcDist(a, b [2]int) int {
	return Abs(a[0]-b[0]) + Abs(a[1]-b[1])
}

func Move(src1, src2, dst1, dst2 int) {
	st := State{
		prevLoc:   [2]int{src1, src2},
		movements: dp[src1][src2].movements + CalcDist(locs[src1], locs[dst1]) + CalcDist(locs[src2], locs[dst2]),
	}
	if st.movements < dp[dst1][dst2].movements {
		dp[dst1][dst2] = st
	}
}

func FindOptimalSolution() (int, [2]int) {
	optimal := State{movements: math.MaxInt}
	carLoc := [2]int{}

	for i := 0; i < len(locs)-1; i++ {
		if dp[i][len(locs)-1].movements < optimal.movements {
			optimal = dp[i][len(locs)-1]
			carLoc = [2]int{i, len(locs) - 1}
		}
		if dp[len(locs)-1][i].movements < optimal.movements {
			optimal = dp[len(locs)-1][i]
			carLoc = [2]int{len(locs) - 1, i}
		}
	}

	return optimal.movements, carLoc
}

func AnswerString() string {
	movements, carLoc := FindOptimalSolution()

	var trace [][2]int
	for carLoc[0] != 0 || carLoc[1] != 1 {
		trace = append(trace, carLoc)
		carLoc = dp[carLoc[0]][carLoc[1]].prevLoc
	}

	ans := strings.Builder{}
	ans.WriteString(strconv.Itoa(movements))
	ans.WriteByte('\n')
	for i := len(trace) - 1; 0 <= i; i-- {
		if trace[i][0] < trace[i][1] {
			ans.WriteString("2\n")
		} else {
			ans.WriteString("1\n")
		}
	}

	return ans.String()
}

func Solve() {
	for k := 2; k < len(locs); k++ {
		for i := 0; i < k-1; i++ {
			Move(i, k-1, k, k-1)
			Move(k-1, i, k, i)
			Move(i, k-1, i, k)
			Move(k-1, i, k-1, k)
		}
	}

	fmt.Print(AnswerString())
}

func main() {
	Init()
	Solve()
}
