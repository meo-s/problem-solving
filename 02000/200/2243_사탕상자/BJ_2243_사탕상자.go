// https://www.acmicpc.net/problem/2243

package main

import (
	"bufio"
	"os"
	"runtime"
	"strconv"
)

const (
	MinTasteValue = 0
	MaxTasteValue = 1_000_000
)

type FenwickTree struct {
	v []int64
}

func (t FenwickTree) Len() int {
	return len(t.v)
}

func (t FenwickTree) Update(index int, dv int64) {
	for index <= len(t.v) {
		t.v[index-1] += dv
		index += index & -index
	}
}

func (t FenwickTree) Query(index int) (v int64) {
	for 0 < index {
		v += t.v[index-1]
		index -= index & -index
	}
	return
}

func NewFenwickTree(size int) FenwickTree {
	return FenwickTree{make([]int64, size)}
}

func readInt(r *bufio.Reader) (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
	sign := 1
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if b == '-' {
				sign = -1
				continue
			}
			if GOOS_IS_WINDOWS && b == '\r' {
				r.ReadByte()
			}
			return sign * n
		}
		n = n*10 + int(b-'0')
	}
}

func main() {
	t := NewFenwickTree(MaxTasteValue - MinTasteValue + 1)
	stdin := bufio.NewReaderSize(os.Stdin, 1<<20)
	stdout := bufio.NewWriterSize(os.Stdout, 1<<18)
	defer stdout.Flush()

	addCandy := func() {
		t.Update(readInt(stdin), int64(readInt(stdin)))
	}

	delCandy := func() {
		rank := int64(readInt(stdin))
		lb, ub := MinTasteValue, MaxTasteValue+1
		for lb < ub {
			mid := (lb + ub) / 2
			if rank <= t.Query(mid) {
				ub = mid
			} else {
				lb = mid + 1
			}
		}

		t.Update(lb, -1)
		stdout.WriteString(strconv.Itoa(lb))
		stdout.WriteByte('\n')
	}

	Handler := []func(){delCandy, addCandy}
	for i := readInt(stdin); 0 < i; i-- {
		Handler[readInt(stdin)-1]()
	}
}
