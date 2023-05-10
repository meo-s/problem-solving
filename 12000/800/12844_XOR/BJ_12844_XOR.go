// https://www.acmicpc.net/problem/12844

package main

import (
	"bufio"
	"math"
	"os"
	"strconv"
)

type LazyXorTree struct {
	depth    int
	nodes    []int
	pendings []int
}

func (tree *LazyXorTree) Len() int {
	return len(tree.nodes)
}

func (tree *LazyXorTree) Reflect(index, left, right, v int) {
	if right-left == 1 {
		tree.nodes[index] ^= v
	} else {
		tree.pendings[index*2] ^= v
		tree.pendings[index*2+1] ^= v
	}
}

func (tree *LazyXorTree) Update(index, left, right, beg, end, v int) int {
	before := tree.nodes[index]
	if left == beg && right == end {
		tree.Reflect(index, left, right, v)
	} else {
		mid := (left + right) / 2
		if end <= mid {
			tree.nodes[index] ^= tree.Update(index*2, left, mid, beg, end, v)
		} else if mid <= beg {
			tree.nodes[index] ^= tree.Update(index*2+1, mid, right, beg, end, v)
		} else {
			tree.nodes[index] ^= tree.Update(index*2, left, mid, beg, mid, v)
			tree.nodes[index] ^= tree.Update(index*2+1, mid, right, mid, end, v)
		}
	}

	return before ^ tree.nodes[index]
}

func (tree *LazyXorTree) Query(index, left, right, beg, end int) int {
	if tree.pendings[index] != 0 {
		tree.Reflect(index, left, right, tree.pendings[index])
		tree.pendings[index] = 0
	}

	if left == beg && right == end {
		return tree.nodes[index]
	} else {
		mid := (left + right) / 2
		if end <= mid {
			return tree.Query(index*2, left, mid, beg, end)
		} else if mid <= beg {
			return tree.Query(index*2+1, mid, right, beg, end)
		} else {
			lr := tree.Query(index*2, left, mid, beg, mid)
			rr := tree.Query(index*2+1, mid, right, mid, end)
			return lr ^ rr
		}
	}
}

func NewLazyXorTree(N int) *LazyXorTree {
	depth := int(math.Ceil(math.Log2(float64(N))))
	return &LazyXorTree{
		depth:    depth,
		nodes:    make([]int, 1<<(depth+1)),
		pendings: make([]int, 1<<(depth+1)),
	}
}

var tree *LazyXorTree
var br = bufio.NewReaderSize(os.Stdin, 1<<20)
var bw = bufio.NewWriterSize(os.Stdout, 1<<20)

func Rdi() (n int) {
	for {
		b, err := br.ReadByte()
		if b == ' ' || b == '\n' || err != nil {
			return
		}
		n = n*10 + int(b-'0')
	}
}

func InitLazyXorTree() {
	N := Rdi()
	tree = NewLazyXorTree(N)
	for i := 0; i < N; i++ {
		tree.nodes[i+1<<tree.depth] = Rdi()
	}
	for depth := tree.depth - 1; 0 <= depth; depth-- {
		for i := (1 << depth); i < (2 << depth); i++ {
			tree.nodes[i] = tree.nodes[i*2] ^ tree.nodes[i*2+1]
		}
	}
}

func ExecuteQuery() {
	q := Rdi()
	if q == 1 {
		i, j, k := Rdi()+1, Rdi()+1, Rdi()
		tree.Update(1, 1, 1+1<<tree.depth, i, j+1, k)
	} else {
		i, j := Rdi()+1, Rdi()+1
		bw.WriteString(strconv.Itoa(tree.Query(1, 1, 1+1<<tree.depth, i, j+1)))
		bw.WriteByte('\n')
	}
}

func main() {
	defer bw.Flush()
	InitLazyXorTree()

	Q := Rdi()
	for 0 < Q {
		Q--
		ExecuteQuery()
	}
}
