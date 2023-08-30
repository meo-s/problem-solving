// https://www.acmicpc.net/problem/2287

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strconv"
	"strings"
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

func Cache(k int) map[int]int {
	dp := make([]map[int]interface{}, 8)

	n := 0
	for i := 0; i < len(dp); i++ {
		n = n*10 + k
		dp[i] = map[int]interface{}{}
		dp[i][n] = nil
	}

	for i := 1; i < len(dp); i++ {
		for j := 0; j < i; j++ {
			for a := range dp[(i-1)-j] {
				for b := range dp[j] {
					dp[i][a+b] = nil
					dp[i][a-b] = nil
					dp[i][a*b] = nil
					if b != 0 {
						dp[i][a/b] = nil
					}
				}
			}
		}
	}

	ans := map[int]int{}
	for i := 0; i < len(dp); i++ {
		for k := range dp[(len(dp)-1)-i] {
			ans[k] = len(dp) - i
		}
	}

	return ans
}

func main() {
	in := Fin{bufio.NewReaderSize(os.Stdin, 1<<20)}
	cache := Cache(in.ReadInt())

	sb := strings.Builder{}
	sb.Grow(3001)

	for i := in.ReadInt(); 0 < i; i-- {
		a := in.ReadInt()
		if ans, ok := cache[a]; ok {
			sb.WriteString(strconv.Itoa(ans))
			sb.WriteByte('\n')
		} else {
			sb.WriteString("NO\n")
		}
	}

	fmt.Print(sb.String())
}
