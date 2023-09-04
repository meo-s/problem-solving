// https://www.acmicpc.net/problem/1470

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strconv"
)

const (
	BRONZE = 0
	SILVER = 1
	GOLD   = 2
)

const NEVAL = -1

type SharedContext struct {
	N      int
	L      int
	Medals [][3]int
}

type BufferedReader struct {
	*bufio.Reader
}

func (br BufferedReader) ReadInt() (n int) {
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

func Max[T int](a, b T) T {
	if b <= a {
		return a
	} else {
		return b
	}
}

func Init() SharedContext {
	in := BufferedReader{bufio.NewReaderSize(os.Stdin, 1<<16)}

	N := in.ReadInt()
	L := in.ReadInt()

	medals := make([][3]int, N)
	medals[0][GOLD] += L
	for i := range medals {
		medals[i][GOLD] += in.ReadInt()
		medals[i][SILVER] += in.ReadInt()
		medals[i][BRONZE] += in.ReadInt()
	}

	return SharedContext{N, L, medals}
}

func Solve(ctx SharedContext) {
	medals := ctx.Medals

	dp := [2][][]int{}
	for k := range dp {
		dp[k] = make([][]int, ctx.N)
		for i := range dp[k] {
			dp[k][i] = make([]int, ctx.L+1)
			for j := range dp[k][i] {
				dp[k][i][j] = NEVAL
			}
		}
	}

	dp[1][0][ctx.L] = ctx.L

	for k := 1; k < ctx.N; k++ {
		dp[0], dp[1] = dp[1], dp[0]

		for rank := 0; rank < k; rank++ {
			for numSilvers := 0; numSilvers <= ctx.L; numSilvers++ {
				if dp[0][rank][numSilvers] == NEVAL {
					continue
				}

				dp[1][rank][numSilvers] = Max(dp[1][rank][numSilvers], dp[0][rank][numSilvers])

				if medals[k][GOLD] < medals[0][GOLD] {
					continue
				}

				if medals[0][GOLD] < medals[k][GOLD] ||
					(medals[0][GOLD] == medals[k][GOLD] && medals[0][SILVER] < medals[k][SILVER]) ||
					(medals[0][GOLD] == medals[k][GOLD] && medals[0][SILVER] == medals[k][SILVER] && medals[0][BRONZE] < medals[k][BRONZE]) {
					dp[1][rank+1][numSilvers] = Max(dp[1][rank+1][numSilvers], dp[0][rank][numSilvers])
					continue
				}

				numBronzes := dp[0][rank][numSilvers]
				silverDiff := medals[0][SILVER] - medals[k][SILVER]
				bronzeDiff := medals[0][BRONZE] - medals[k][BRONZE]

				if 0 < silverDiff {
					if silverDiff <= numSilvers && bronzeDiff < 0 {
						nextSilvers := numSilvers - silverDiff
						dp[1][rank+1][nextSilvers] = Max(dp[1][rank+1][nextSilvers], numBronzes)
					} else {
						if silverDiff+1 <= numSilvers {
							nextSilvers := numSilvers - (silverDiff + 1)
							dp[1][rank+1][nextSilvers] = Max(dp[1][rank+1][nextSilvers], numBronzes)
						}

						if silverDiff <= numSilvers && bronzeDiff+1 <= numBronzes && (silverDiff+bronzeDiff+1) <= ctx.L {
							nextSilvers := numSilvers - silverDiff
							dp[1][rank+1][nextSilvers] = Max(dp[1][rank+1][nextSilvers], numBronzes-(bronzeDiff+1))
						}
					}
				} else /* if silverDiff == 0 */ {
					if 1 <= numSilvers {
						nextSilvers := numSilvers - 1
						dp[1][rank+1][nextSilvers] = Max(dp[1][rank+1][nextSilvers], numBronzes)
					}

					if bronzeDiff+1 <= numBronzes {
						dp[1][rank+1][numSilvers] = Max(dp[1][rank+1][numSilvers], numBronzes-(bronzeDiff+1))
					}
				}
			}
		}
	}

	for rank := ctx.N; 0 < rank; rank-- {
		for _, numBronzes := range dp[1][rank-1] {
			if numBronzes != NEVAL {
				fmt.Println(strconv.Itoa(rank))
				os.Exit(0)
			}
		}
	}
}

func main() {
	Solve(Init())
}
