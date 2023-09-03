// https://www.acmicpc.net/problem/1955

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
)

type Fin struct {
	*bufio.Reader
}

func (in Fin) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

	for {
		b, err := in.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				in.ReadByte()
			}
			return n
		}

		n = n*10 + int(b-'0')
	}
}

func Solve(goal int) {
	visited := make([]bool, 10_001)
	visited[1] = true

	var dp []map[int]any
	dp = append(dp, map[int]any{1: nil})

	visitFactorial := func(k int, callee any) {
		if k < 8 {
			n := 1
			for 0 < k {
				n *= k
				k--
			}

			if !visited[n] {
				visited[n] = true
				dp[len(dp)-1][n] = nil
				callee.(func(int, any))(n, callee)
			}
		}
	}

	for !visited[goal] {
		dp = append(dp, map[int]any{})

		for i := 0; i < len(dp)-1; i++ {
			for a := range dp[i] {
				for b := range dp[(len(dp)-1)-(i+1)] {
					if a+b < len(visited) && !visited[a+b] {
						visited[a+b] = true
						dp[len(dp)-1][a+b] = nil
						visitFactorial(a+b, visitFactorial)
					}
					if a*b < len(visited) && !visited[a*b] {
						visited[a*b] = true
						dp[len(dp)-1][a*b] = nil
						visitFactorial(a*b, visitFactorial)
					}
				}
			}
		}
	}

	fmt.Printf("%d\n", len(dp))
}

func main() {
	Solve(Fin{bufio.NewReader(os.Stdin)}.ReadInt())
}
