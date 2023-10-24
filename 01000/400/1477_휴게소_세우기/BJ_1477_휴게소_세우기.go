// https://www.acmicpc.net/problem/1477

package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"runtime"
	"sort"
)

func min(a, b int) int {
	if b < a {
		return b
	} else {
		return a
	}
}

func readInt(r io.ByteReader) (n int) {
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

func setup() ([]int, int) {
	in := bufio.NewReaderSize(os.Stdin, 1<<16)
	n := readInt(in)
	m := readInt(in)
	l := readInt(in)
	rests := make([]int, n, n+1)
	for i := 0; i < n; i++ {
		rests[i] = readInt(in)
	}
	sort.Slice(rests, func(i, j int) bool {
		return rests[i] < rests[j]
	})
	rests = append(rests, l)

	highway := make([]int, l)
	for i := 0; i < n; i++ {
		for x := rests[i]; x < rests[i+1]; x++ {
			highway[x] = rests[i]
		}
	}

	return highway, m
}

func simulate(highway []int, m int, gap int) bool {
	for x := gap; x < len(highway) && 0 <= m; x += gap {
		if highway[x] == highway[x-gap] {
			m--
		} else {
			x = highway[x]
		}
	}
	return 0 <= m
}

func solve(highway []int, m int) {
	ans := len(highway)
	lb, ub := 1, len(highway)
	for lb < ub {
		mid := (lb + ub) / 2
		if simulate(highway, m, mid) {
			ub, ans = mid, mid
		} else {
			lb = mid + 1
		}
	}

	fmt.Println(ans)
}

func main() {
	solve(setup())
}
