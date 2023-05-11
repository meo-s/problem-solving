// https://www.acmicpc.net/problem/1395

package main

import (
	"bufio"
	"math"
	"os"
	"strconv"
)

type LazySegmentTree struct {
	leafs    int
	nodes    []int
	pendings []int
}

func (tree *LazySegmentTree) updateLazy(index, left, right int) {
	if dv := tree.pendings[index]; dv != 0 {
		tree.pendings[index] = 0
		if dv%2 == 1 {
			tree.nodes[index] = (right - left) - tree.nodes[index]
			if 1 < right-left {
				tree.pendings[index*2] += 1
				tree.pendings[index*2+1] += 1
			}
		}
	}
}

func (tree *LazySegmentTree) updateRange(index, left, right, beg, end int) int {
	tree.updateLazy(index, left, right)
	before := tree.nodes[index]

	if beg == left && end == right {
		tree.pendings[index]++
		tree.updateLazy(index, left, right)
	} else {
		mid := (left + right) / 2
		if end <= mid {
			tree.nodes[index] += tree.updateRange(index*2, left, mid, beg, end)
		} else if mid <= beg {
			tree.nodes[index] += tree.updateRange(index*2+1, mid, right, beg, end)
		} else {
			tree.nodes[index] += tree.updateRange(index*2, left, mid, beg, mid)
			tree.nodes[index] += tree.updateRange(index*2+1, mid, right, mid, end)
		}
	}

	return tree.nodes[index] - before
}

func (tree *LazySegmentTree) Update(beg, end int) {
	tree.updateRange(1, 1, tree.leafs+1, beg, end)
}

func (tree *LazySegmentTree) query(index, left, right, beg, end int) int {
	tree.updateLazy(index, left, right)

	if beg == left && end == right {
		return tree.nodes[index]
	} else {
		mid := (right + left) / 2
		if end <= mid {
			return tree.query(index*2, left, mid, beg, end)
		} else if mid <= beg {
			return tree.query(index*2+1, mid, right, beg, end)
		} else {
			lr := tree.query(index*2, left, mid, beg, mid)
			rr := tree.query(index*2+1, mid, right, mid, end)
			return lr + rr
		}
	}
}

func (tree *LazySegmentTree) Query(beg, end int) int {
	return tree.query(1, 1, tree.leafs+1, beg, end)
}

func NewLazySegmentTree(N int) *LazySegmentTree {
	depth := int(math.Ceil(math.Log2(float64(N))))
	return &LazySegmentTree{
		leafs:    1 << depth,
		nodes:    make([]int, 1<<(depth+1)),
		pendings: make([]int, 1<<(depth+1)),
	}
}

var tree *LazySegmentTree
var br = bufio.NewReaderSize(os.Stdin, 1<<20)
var bw = bufio.NewWriterSize(os.Stdout, 1<<20)

func ReadInt() (n int) {
	for {
		b, err := br.ReadByte()
		if b == ' ' || b == '\n' || err != nil {
			return
		}
		n = n*10 + int(b-'0')
	}
}

func ExecuteQuery() {
	o, s, t := ReadInt(), ReadInt(), ReadInt()
	if o == 0 {
		tree.Update(s, t+1)
	} else {
		bw.WriteString(strconv.Itoa(tree.Query(s, t+1)))
		bw.WriteByte('\n')
	}
}

func main() {
	defer bw.Flush()

	N, Q := ReadInt(), ReadInt()
	tree = NewLazySegmentTree(N)
	for i := 0; i < Q; i++ {
		ExecuteQuery()
	}
}
