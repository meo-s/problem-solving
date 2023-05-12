// https://www.acmicpc.net/problem/13925

package main

import (
	"bufio"
	"math"
	"os"
	"strconv"
)

const M int64 = 1e+9 + 7

type UpdateQuery struct{ add, mul int64 }

func (uq *UpdateQuery) IsEmpty() bool { return uq.add == 0 && uq.mul == 1 }
func (uq *UpdateQuery) Clear()        { uq.add, uq.mul = 0, 1 }
func (uq *UpdateQuery) Accumulate(other *UpdateQuery) {
	uq.add = (other.add + (uq.add * other.mul)) % M
	uq.mul = (other.mul * uq.mul) % M
}

type LazySegmentTree struct {
	depth    int
	nodes    []int64
	pendings []UpdateQuery
}

func (tree *LazySegmentTree) updateLazy(index, left, right int) {
	if !tree.pendings[index].IsEmpty() {
		uq := &tree.pendings[index]
		tree.nodes[index] = ((tree.nodes[index]*uq.mul)%M + int64(right-left)*uq.add) % M
		if 1 < right-left {
			tree.pendings[index*2].Accumulate(uq)
			tree.pendings[index*2+1].Accumulate(uq)
		}
		uq.Clear()
	}
}

func (tree *LazySegmentTree) updateRange(index, left, right, beg, end int, uq *UpdateQuery) int64 {
	tree.updateLazy(index, left, right)
	before := tree.nodes[index]

	if beg == left && end == right {
		tree.pendings[index].Accumulate(uq)
		tree.updateLazy(index, left, right)
	} else {
		mid := (left + right) / 2
		if end <= mid {
			tree.nodes[index] = (tree.nodes[index] + tree.updateRange(index*2, left, mid, beg, end, uq) + M) % M
		} else if mid <= beg {
			tree.nodes[index] = (tree.nodes[index] + tree.updateRange(index*2+1, mid, right, beg, end, uq) + M) % M
		} else {
			tree.nodes[index] = (tree.nodes[index] + tree.updateRange(index*2, left, mid, beg, mid, uq) + M) % M
			tree.nodes[index] = (tree.nodes[index] + tree.updateRange(index*2+1, mid, right, mid, end, uq) + M) % M
		}
	}

	return tree.nodes[index] - before
}

func (tree *LazySegmentTree) Update(beg, end int, uq *UpdateQuery) {
	tree.updateRange(1, 1, 1+1<<tree.depth, beg, end, uq)
}

func (tree *LazySegmentTree) query(index, left, right, beg, end int) int {
	tree.updateLazy(index, left, right)
	if beg == left && end == right {
		return int(tree.nodes[index])
	} else {
		mid := (left + right) / 2
		if end <= mid {
			return tree.query(index*2, left, mid, beg, end)
		} else if mid <= beg {
			return tree.query(index*2+1, mid, right, beg, end)
		} else {
			lr := int64(tree.query(index*2, left, mid, beg, mid))
			rr := int64(tree.query(index*2+1, mid, right, mid, end))
			return int((lr + rr) % M)
		}
	}
}

func (tree *LazySegmentTree) Query(beg, end int) int {
	return tree.query(1, 1, 1+1<<tree.depth, beg, end)
}

func NewLazySegmentTree(N int) *LazySegmentTree {
	depth := int(math.Ceil(math.Log2(float64(N))))
	pendings := make([]UpdateQuery, 1<<(depth+1))
	for i := range pendings {
		pendings[i].Clear()
	}
	return &LazySegmentTree{
		depth:    depth,
		nodes:    make([]int64, 1<<(depth+1)),
		pendings: pendings,
	}
}

var tree *LazySegmentTree
var br = bufio.NewReaderSize(os.Stdin, 1<<22)
var bw = bufio.NewWriterSize(os.Stdout, 1<<21)

func ReadInt() (n int) {
	for {
		b, err := br.ReadByte()
		if b == ' ' || b == '\n' || err != nil {
			return
		}
		n = n*10 + int(b-'0')
	}
}

func InitLazySegmentTree() {
	N := ReadInt()
	tree = NewLazySegmentTree(N)
	for i := 0; i < N; i++ {
		tree.nodes[i+1<<tree.depth] = int64(ReadInt())
	}
	for depth := tree.depth - 1; 0 <= depth; depth-- {
		for i := 1 << depth; i < (1 << (depth + 1)); i++ {
			tree.nodes[i] = (tree.nodes[i*2] + tree.nodes[i*2+1]) % M
		}
	}
}

func ExecuteQuery() {
	switch ReadInt() {
	case 1:
		x, y, v := ReadInt(), ReadInt(), ReadInt()
		tree.Update(x, y+1, &UpdateQuery{add: int64(v), mul: 1})
	case 2:
		x, y, v := ReadInt(), ReadInt(), ReadInt()
		tree.Update(x, y+1, &UpdateQuery{add: 0, mul: int64(v)})
	case 3:
		x, y, v := ReadInt(), ReadInt(), ReadInt()
		tree.Update(x, y+1, &UpdateQuery{add: int64(v), mul: 0})
	case 4:
		x, y := ReadInt(), ReadInt()
		bw.WriteString(strconv.Itoa(tree.Query(x, y+1)))
		bw.WriteByte('\n')
	}

}

func main() {
	defer bw.Flush()

	InitLazySegmentTree()
	Q := ReadInt()
	for i := 0; i < Q; i++ {
		ExecuteQuery()
	}
}
