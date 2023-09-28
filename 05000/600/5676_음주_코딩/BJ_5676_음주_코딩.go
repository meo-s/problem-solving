// https://www.acmicpc.net/problem/5676

package main

import (
	"bufio"
	"math"
	"os"
	"runtime"
)

type Reader struct {
	*bufio.Reader
}

type SegmentTree struct {
	a        []int
	elements int
}

type sharedContext struct {
	n, k int
	t    SegmentTree
}

func sign(v int) int {
	if v == 0 {
		return 0
	}
	if 0 < v {
		return 1
	} else {
		return -1
	}
}

func (r Reader) ReadInt() (n int, err error) {
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

			return sign * n, err
		}

		n = n*10 + int(b-'0')
	}
}

func (t SegmentTree) init(node, left, right int, a []int) {
	if left+1 == right {
		t.a[node] = sign(a[left])
	} else {
		mid := (left + right) / 2
		t.init(node*2, left, mid, a)
		t.init(node*2+1, mid, right, a)
		t.a[node] = t.a[node*2] * t.a[node*2+1]
	}
}

func (t SegmentTree) update(node, left, right, index, v int) {
	if left+1 == right {
		t.a[node] = sign(v)
	} else {
		mid := (left + right) / 2
		if index < mid {
			t.update(node*2, left, mid, index, v)
		} else {
			t.update(node*2+1, mid, right, index, v)
		}

		t.a[node] = t.a[node*2] * t.a[node*2+1]
	}
}

func (t SegmentTree) query(node, left, right, beg, end int) int {
	if beg <= left && right <= end {
		return t.a[node]
	}

	mid := (left + right) / 2
	if end <= mid || mid <= beg {
		if end <= mid {
			return t.query(node*2, left, mid, beg, end)
		} else {
			return t.query(node*2+1, mid, right, beg, end)
		}
	} else {
		return t.query(node*2, left, mid, beg, mid) * t.query(node*2+1, mid, right, mid, end)
	}
}

func (t SegmentTree) Update(index, v int) {
	t.update(1, 0, t.elements, index, v)
}

func (t SegmentTree) Query(beg, end int) int {
	return t.query(1, 0, t.elements, beg, end)
}

func NewSegmentTree(a []int) SegmentTree {
	t := SegmentTree{
		a:        make([]int, 1<<int(math.Ceil(math.Log2(float64(len(a))))+1)),
		elements: len(a),
	}

	t.init(1, 0, t.elements, a)
	return t
}

func setup(in Reader) (*sharedContext, error) {
	n, err := in.ReadInt()
	if err != nil {
		return nil, err
	}

	k, _ := in.ReadInt()

	a := make([]int, n)
	for i := range a {
		a[i], _ = in.ReadInt()
	}

	return &sharedContext{n, k, NewSegmentTree(a)}, nil
}

func solve(ctx sharedContext, in Reader, out *bufio.Writer) {
	for k := 0; k < ctx.k; k++ {
		op, _ := in.ReadByte()
		in.ReadByte()

		if op == 'C' {
			i, _ := in.ReadInt()
			v, _ := in.ReadInt()
			ctx.t.Update(i-1, v)
		} else {
			i, _ := in.ReadInt()
			j, _ := in.ReadInt()
			out.WriteByte([3]byte{'-', '0', '+'}[ctx.t.Query(i-1, j)+1])
		}
	}
}

func main() {
	stdin := Reader{bufio.NewReaderSize(os.Stdin, 1<<18)}
	stdout := bufio.NewWriterSize(os.Stdout, 1<<16)
	defer stdout.Flush()

	for {
		if ctx, err := setup(stdin); err == nil {
			solve(*ctx, stdin, stdout)
			stdout.WriteByte('\n')
		} else {
			break
		}
	}
}
