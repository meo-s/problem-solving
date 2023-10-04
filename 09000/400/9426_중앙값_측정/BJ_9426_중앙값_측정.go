// https://www.acmicpc.net/problem/9426

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

type FenwickTree struct {
	a []int
}

func (r Reader) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				r.ReadByte()
			}
			return
		}

		n = n*10 + int(b-'0')
	}
}

func (t FenwickTree) Len() int {
	return len(t.a)
}

func (t FenwickTree) Update(index, dv int) {
	for index < len(t.a) {
		t.a[index-1] += dv
		index += index & -index
	}
}

func (t FenwickTree) Query(index int) (n int) {
	for 0 < index {
		n += t.a[index-1]
		index -= index & -index
	}
	return
}

func median(t FenwickTree, k int) (v int) {
	lb, ub := 0, t.Len()
	for lb < ub {
		mid := (lb + ub) / 2
		if (k+1)/2 <= t.Query(mid+1) {
			ub, v = mid, mid
		} else {
			lb = mid + 1
		}
	}
	return
}

func main() {
	in := Reader{bufio.NewReaderSize(os.Stdin, 1<<20)}

	n, k := in.ReadInt(), in.ReadInt()
	t := FenwickTree{make([]int, math.MaxUint16+2)}
	q := make(chan int, k)

	for i := 0; i < k-1; i++ {
		n := in.ReadInt()
		q <- n
		t.Update(1+n, 1)
	}

	ans := int64(0)
	for i := 0; i <= n-k; i++ {
		n := in.ReadInt()
		q <- n
		t.Update(1+n, 1)
		ans += int64(median(t, k))
		t.Update(1+<-q, -1)
	}

	fmt.Println(ans)
}
