// https://www.acmicpc.net/problem/14438

package main

import (
	"bufio"
	"math"
	"os"
	"runtime"
	"strconv"
)

func min(a, b int) int {
	if a <= b {
		return a
	} else {
		return b
	}
}

type bufferedReader struct {
	*bufio.Reader
}

func (br *bufferedReader) readInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

	for {
		b, err := br.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				br.ReadByte()
			}
			return
		}

		n = n*10 + int(b-'0')
	}
}

type SegmentTree struct {
	elements int
	a        []int
}

func (t *SegmentTree) Elements() int {
	return t.elements
}

func (t *SegmentTree) Init(node, left, right int, a []int) {
	if right-left == 1 {
		t.a[node] = a[left]
	} else {
		mid := (left + right) / 2
		t.Init(node*2, left, mid, a)
		t.Init(node*2+1, mid, right, a)
		t.a[node] = min(t.a[node*2], t.a[node*2+1])
	}
}

func (t *SegmentTree) Update(node, left, right, index, v int) {
	if left <= index && index < right {
		if right-left == 1 {
			t.a[node] = v
		} else {
			mid := (left + right) / 2
			t.Update(node*2, left, mid, index, v)
			t.Update(node*2+1, mid, right, index, v)
			t.a[node] = min(t.a[node*2], t.a[node*2+1])
		}
	}
}

func (t *SegmentTree) Query(node, left, right, beg, end int) int {
	if beg <= left && right <= end {
		return t.a[node]
	}

	mid := (left + right) / 2
	if beg < mid && mid < end {
		return min(t.Query(node*2, left, mid, beg, mid), t.Query(node*2+1, mid, right, mid, end))
	} else {
		if end <= mid {
			return t.Query(node*2, left, mid, beg, end)
		} else {
			return t.Query(node*2+1, mid, right, beg, end)
		}
	}
}

func NewSegmentTree(a []int) *SegmentTree {
	height := int(math.Ceil(math.Log2(float64(len(a))))) + 1
	t := &SegmentTree{a: make([]int, 1<<height), elements: len(a)}
	t.Init(1, 0, len(a), a)
	return t
}

func setup(br bufferedReader) *SegmentTree {
	n := br.readInt()
	a := make([]int, n)
	for i := 0; i < n; i++ {
		a[i] = br.readInt()
	}

	return NewSegmentTree(a)
}

func solve(br bufferedReader, bw *bufio.Writer, t *SegmentTree) {
	for m := br.readInt(); 0 < m; m-- {
		if br.readInt() == 1 {
			t.Update(1, 0, t.Elements(), br.readInt()-1, br.readInt())
		} else {
			bw.WriteString(strconv.Itoa(t.Query(1, 0, t.Elements(), br.readInt()-1, br.readInt())))
			bw.WriteByte('\n')
		}
	}
}

func main() {
	in := bufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}
	out := bufio.NewWriterSize(os.Stdout, 1<<18)
	solve(in, out, setup(in))
	out.Flush()
}
