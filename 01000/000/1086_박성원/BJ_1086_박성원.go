// https://www.acmicpc.net/problem/1086

package main

import (
	"bufio"
	"fmt"
	"math/big"
	"os"
	"runtime"
)

type sharedContext struct {
	n, k int
	s    []string
	r    []int
}

func must[T any](v T, _ any) T {
	return v
}

func readInt(r *bufio.Reader) (n int) {
	const GOOSIsWindows = (runtime.GOOS == "windows")
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOSIsWindows && b == '\r' {
				r.ReadByte()
			}
			return
		}
		n = n*10 + int(b-'0')
	}
}

func setup(in *bufio.Reader) sharedContext {
	n := readInt(in)
	s := make([]string, 0, n)
	for i := 0; i < n; i++ {
		line, _, _ := in.ReadLine()
		s = append(s, string(line))
	}
	k := big.NewInt(int64(readInt(in)))
	r := make([]int, 0, n)
	for i := range s {
		v := must((&big.Int{}).SetString(s[i], 10))
		r = append(r, int(v.Mod(v, k).Int64()))
	}
	return sharedContext{n, int(k.Int64()), s, r}
}

func count(c sharedContext, tens []int, dp [][]int64, depth, bits, length int) {
	if dp[bits] == nil {
		dp[bits] = make([]int64, c.k)
		if bits == (1<<c.n)-1 {
			dp[bits][0] = 1
			for i := 1; i < c.k; i++ {
				dp[bits][i] = 0
			}
		} else {
			for i := 0; i < c.n; i++ {
				if (bits>>i)&1 == 0 {
					count(c, tens, dp, depth+1, bits|(1<<i), length+len(c.s[i]))
					for j := 0; j < c.k; j++ {
						r := (c.r[i] * tens[length]) % c.k
						dp[bits][(r+j)%c.k] += dp[bits|(1<<i)][j]
					}
				}
			}
		}
	}
}

func sum(a []int64) (v int64) {
	for _, n := range a {
		v += n
	}
	return
}

func gcd(a, b int64) int64 {
	r := a % b
	for 0 < r {
		a = b
		b = r
		r = a % b
	}
	return b
}

func solve(c sharedContext) {
	tens := make([]int, 50*(c.n-1)+2)
	tens[0] = 1
	tens[1] = 10 % c.k
	for i := 2; i < len(tens); i++ {
		tens[i] = (tens[i-1] * (10 % c.k)) % c.k
	}

	dp := make([][]int64, 1<<len(c.s))
	count(c, tens, dp, 0, 0, 0)
	if dp[0][0] == 0 {
		fmt.Println("0/1")
	} else {
		total := sum(dp[0])
		gcd := gcd(total, dp[0][0])
		fmt.Printf("%d/%d\n", dp[0][0]/gcd, total/gcd)
	}
}

func main() {
	solve(setup(bufio.NewReaderSize(os.Stdin, 1<<10)))
}
