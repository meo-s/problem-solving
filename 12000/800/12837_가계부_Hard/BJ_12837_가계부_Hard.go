// https://www.acmicpc.net/problem/12837

package main

import (
	"bufio"
	"os"
	"runtime"
	"strconv"
)

type Reader struct {
	*bufio.Reader
}

type FenwickTree struct {
	a []int64
}

func (r Reader) ReadInt() (n int64) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
	sign := int64(1)

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

		n = n*10 + int64(b-'0')
	}
}

func (t FenwickTree) Update(index int, dv int64) {
	for index <= len(t.a) {
		t.a[index-1] += dv
		index += index & -index
	}
}

func (t FenwickTree) Query(index int) (v int64) {
	for index != 0 {
		v += t.a[index-1]
		index -= index & -index
	}

	return
}

func NewFenwickTree(size int) FenwickTree {
	return FenwickTree{make([]int64, size)}
}

func solve(in Reader, out *bufio.Writer) {
	N, Q := in.ReadInt(), in.ReadInt()
	t := NewFenwickTree(int(N))

	for 0 < Q {
		Q--

		if in.ReadInt() == 1 {
			t.Update(int(in.ReadInt()), in.ReadInt())
		} else {
			p, q := int(in.ReadInt()), int(in.ReadInt())
			out.WriteString(strconv.FormatInt(t.Query(q)-t.Query(p-1), 10))
			out.WriteByte('\n')
		}
	}
}

func main() {
	stdout := bufio.NewWriterSize(os.Stdout, 1<<18)
	solve(Reader{bufio.NewReaderSize(os.Stdin, 1<<20)}, stdout)
	stdout.Flush()
}
