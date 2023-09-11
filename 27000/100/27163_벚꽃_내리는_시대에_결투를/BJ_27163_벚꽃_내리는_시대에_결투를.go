// https://www.acmicpc.net/problem/27163

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strings"
)

const NEVAL = -1

type bufferedReader struct {
	*bufio.Reader
}

type sharedContext struct {
	aura, life int
	cards      [][2]int
}

type state struct {
	aura       int
	prevLife   int
	attackType byte
}

func (br *bufferedReader) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

	sign := 1
	for {
		b, err := br.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if b == '-' {
				sign = -1
				continue
			}
			if GOOS_IS_WINDOWS && b == '\r' {
				br.ReadByte()
			}
			return sign * n
		}

		n = n*10 + int(b-'0')
	}
}

func setup() sharedContext {
	br := bufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}

	N := br.ReadInt()
	aura := br.ReadInt()
	life := br.ReadInt()
	cards := make([][2]int, 0, N)
	for i := 0; i < N; i++ {
		x := br.ReadInt()
		y := br.ReadInt()
		cards = append(cards, [2]int{x, y})
	}

	return sharedContext{aura, life, cards}
}

func max(a, b int) int {
	if b <= a {
		return a
	} else {
		return b
	}
}

func optimal(a, b state) state {
	if b.aura <= a.aura {
		return a
	} else {
		return b
	}
}

func trace(dp [][]state, cardIndex int, s state, sb *strings.Builder) {
	if cardIndex != 0 {
		trace(dp, cardIndex-1, dp[cardIndex-1][s.prevLife], sb)
		sb.WriteByte(s.attackType)
	}
}

func canSurvive(dp [][]state) (bool, string) {
	var anySurvivedState *state = nil
	for _, s := range dp[len(dp)-1] {
		if s.aura != NEVAL {
			anySurvivedState = &s
			break
		}
	}

	if anySurvivedState == nil {
		return false, ""
	}

	sb := strings.Builder{}
	sb.Grow(len(dp))
	trace(dp, len(dp)-1, *anySurvivedState, &sb)
	return true, sb.String()
}

func solve(ctx sharedContext) {
	dp := make([][]state, len(ctx.cards)+1)
	for k := range dp {
		dp[k] = make([]state, ctx.life+1)
		for i := range dp[k] {
			dp[k][i] = state{NEVAL, NEVAL, 0}
		}
	}

	dp[0][ctx.life].aura = ctx.aura

	for k := range ctx.cards {
		x, y := ctx.cards[k][0], ctx.cards[k][1]
		for i := range dp[k] {
			curAura := dp[k][i].aura
			if curAura != NEVAL {
				if y == -1 || (x != -1 && x <= curAura) {
					dp[k+1][i] = optimal(dp[k+1][i], state{max(curAura-x, 0), i, 'A'})
				}
				if y != -1 && y < i {
					dp[k+1][i-y] = optimal(dp[k+1][i-y], state{curAura, i, 'L'})
				}
			}
		}
	}

	survived, attacks := canSurvive(dp)
	if survived {
		fmt.Printf("YES\n%s\n", attacks)
	} else {
		fmt.Printf("NO")
	}
}

func main() {
	solve(setup())
}
