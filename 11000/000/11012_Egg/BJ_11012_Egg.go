// https://www.acmicpc.net/problem/11012

package main

import (
	"bufio"
	"os"
	"strconv"
)

type BufferedReader struct {
	*bufio.Reader
}

func (br *BufferedReader) ReadInt() (n int) {
	numBytes := 0
	for {
		b, err := br.ReadByte()
		numBytes++
		if b == '\r' || b == '\n' || b == ' ' || err != nil {
			if err == nil && numBytes == 1 {
				numBytes = 0
				continue
			}
			return
		}
		n = n*10 + int(b-'0')
	}
}

type Node struct {
	l, r *Node
	v    int
}

func (node *Node) Clone() *Node {
	if node == nil {
		return &Node{}
	}
	return &Node{node.l, node.r, node.v}
}

type DynamicSegmentTree struct {
	roots []*Node
	size  int
}

func (dst *DynamicSegmentTree) update(node *Node, left, right, index int) {
	node.v++
	if 1 < right-left {
		mid := (left + right) / 2
		if index < mid {
			node.l = node.l.Clone()
			dst.update(node.l, left, mid, index)
		} else {
			node.r = node.r.Clone()
			dst.update(node.r, mid, right, index)
		}
	}
}

func (dst *DynamicSegmentTree) query(node *Node, left, right, beg, end int) (v int64) {
	if beg < right && left < end {
		if beg <= left && right <= end {
			v = int64(node.v)
		} else {
			mid := (left + right) / 2
			if node.l != nil {
				v += dst.query(node.l, left, mid, beg, end)
			}
			if node.r != nil {
				v += dst.query(node.r, mid, right, beg, end)
			}
		}
	}
	return
}

func (dst *DynamicSegmentTree) Update(index int) {
	if 0 < len(dst.roots) {
		dst.roots = append(dst.roots, dst.roots[len(dst.roots)-1].Clone())
	} else {
		dst.roots = append(dst.roots, &Node{})
	}
	dst.update(dst.roots[len(dst.roots)-1], 0, dst.size+1, index)
}

func (dst *DynamicSegmentTree) Query(k, beg, end int) int64 {
	return dst.query(dst.roots[k], 0, dst.size+1, beg, end)
}

func NewDynamicSegmentTree(size int) *DynamicSegmentTree {
	roots := []*Node{}
	roots = append(roots, &Node{})
	return &DynamicSegmentTree{roots, size}
}

func main() {
	const BOUND = 100001
	stdin := &BufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}
	stdout := bufio.NewWriterSize(os.Stdout, 1<<20)

	T := stdin.ReadInt()
	for 0 < T {
		T--

		N := stdin.ReadInt()
		M := stdin.ReadInt()

		folks := make([][]int, BOUND)
		for i := 0; i < N; i++ {
			x := stdin.ReadInt()
			y := stdin.ReadInt()
			folks[y] = append(folks[y], x)
		}

		dst := NewDynamicSegmentTree(BOUND)
		for y := 0; y < BOUND; y++ {
			for _, x := range folks[y] {
				dst.Update(x)
			}
		}

		checkpoints := make([]int, BOUND)
		for y := 0; y < BOUND; y++ {
			checkpoints[y] = len(folks[y])
			if 0 < y {
				checkpoints[y] += checkpoints[y-1]
			}
		}

		numEggs := int64(0)
		for i := 0; i < M; i++ {
			l := stdin.ReadInt()
			r := stdin.ReadInt()
			b := stdin.ReadInt()
			t := stdin.ReadInt()
			numEggs += dst.Query(checkpoints[t], l, r+1)
			if 0 < b {
				numEggs -= dst.Query(checkpoints[b-1], l, r+1)
			}
		}

		stdout.WriteString(strconv.FormatInt(numEggs, 10))
		stdout.WriteByte('\n')
	}

	stdout.Flush()
}
