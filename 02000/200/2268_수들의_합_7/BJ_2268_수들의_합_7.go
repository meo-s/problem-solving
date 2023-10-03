// https://www.acmicpc.net/problem/2268

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

func (t *FenwickTree) Resize(size int) {
	na := make([]int64, size)
	copy(na, t.a)
	t.a = na
}

func (t *FenwickTree) Update(index int, dv int64) {
	for index <= len(t.a) {
		t.a[index-1] += dv
		index += index & -index
	}
}

func (t *FenwickTree) Query(index int) (n int64) {
	for index != 0 {
		n += t.a[index-1]
		index -= index & -index
	}
	return
}

func (r *Reader) ReadInt() (n int) {
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

func minmax(a, b int) (min, max int) {
	if a <= b {
		return a, b
	} else {
		return b, a
	}
}

func solve(in Reader, out *bufio.Writer) {
	a := make([]int, in.ReadInt())
	t := new(FenwickTree)
	t.Resize(len(a))

	handler := [2]func(){
		func() {
			i, j := minmax(in.ReadInt(), in.ReadInt())
			out.WriteString(strconv.FormatInt(t.Query(j)-t.Query(i-1), 10))
			out.WriteByte('\n')
		},
		func() {
			i := in.ReadInt()
			k := in.ReadInt()
			dv := k - a[i-1]
			a[i-1] = k
			t.Update(i, int64(dv))
		},
	}

	for m := in.ReadInt(); 0 < m; m-- {
		handler[in.ReadInt()]()
	}
}

func main() {
	in := Reader{bufio.NewReaderSize(os.Stdin, 1<<20)}
	out := bufio.NewWriterSize(os.Stdout, 1<<18)
	defer out.Flush()

	solve(in, out)
}
