// https://www.acmicpc.net/problem/3964

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"runtime"
)

type Reader struct {
	*bufio.Reader
}

func (r Reader) ReadInt() (n int64) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				r.ReadByte()
			}
			return
		}
		n = n*10 + int64(b-'0')
	}
}

func min(v ...int) int {
	m := v[0]
	for i := 1; i < len(v); i++ {
		if v[i] < m {
			m = v[i]
		}
	}
	return m
}

func count(n, p0 int64) (cnt int) {
	for p := p0; p <= n; p *= p0 {
		cnt += int(n / p)
		if n/p0 < p {
			break
		}
	}
	return
}

func solve(n, k int64) {
	ans := math.MaxInt
	for i, k0 := int64(2), k; i <= k && i*i <= k0; i++ {
		if k%i == 0 {
			exp := 0
			for k%i == 0 {
				exp++
				k /= i
			}
			ans = min(ans, count(n, i)/exp)
		}
	}
	if k != 1 {
		ans = min(ans, count(n, k))
	}

	fmt.Println(ans)
}

func main() {
	in := Reader{bufio.NewReaderSize(os.Stdin, 1<<16)}
	for t := in.ReadInt(); 0 < t; t-- {
		solve(in.ReadInt(), in.ReadInt())
	}
}
