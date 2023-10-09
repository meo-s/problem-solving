// https://www.acmicpc.net/problem/16398

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
)

type Heap[T any] struct {
	data []any
	less func(a, b T) bool
}

func (h Heap[T]) Len() int {
	return len(h.data)
}

func (h *Heap[T]) Push(v T) {
	h.data = append(h.data, v)
	h.up(len(h.data) - 1)
}

func (h *Heap[T]) Pop() (v T) {
	n := h.Len() - 1
	v, h.data[0] = h.data[0].(T), h.data[n]
	h.data[n] = nil
	h.data = h.data[:n]
	h.down(0, n)
	return
}

func (h *Heap[T]) up(i int) {
	for {
		j := (i - 1) / 2
		if i == j || h.less(h.data[j].(T), h.data[i].(T)) {
			break
		}
		h.swap(i, j)
		i = j
	}
}

func (h *Heap[T]) down(i, n int) {
	for {
		j := i*2 + 1
		if n <= j || j < 0 {
			break
		}
		if j+1 < n && h.less(h.data[j+1].(T), h.data[j].(T)) {
			j++
		}
		if !h.less(h.data[j].(T), h.data[i].(T)) {
			break
		}
		h.swap(i, j)
		i = j
	}
}

func (h *Heap[T]) swap(i, j int) {
	h.data[i], h.data[j] = h.data[j], h.data[i]
}

func NewHeap[T any](less func(a, b T) bool) Heap[T] {
	return Heap[T]{less: less}
}

func readInt(r *bufio.Reader) (n int) {
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

func setup() (int, [][]int) {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	n := readInt(in)
	g := make([][]int, n)
	for i := range g {
		g[i] = make([]int, n)
	}
	for u := 0; u < n; u++ {
		for v := 0; v < n; v++ {
			g[u][v] = readInt(in)
		}
	}
	return n, g
}

func solve(n int, g [][]int) {
	h := NewHeap[[2]int](func(a [2]int, b [2]int) bool {
		return a[1] < b[1]
	})

	var cost int64
	vertices := 0
	h.Push([2]int{0, 0})
	for vertices < n {
		edge := h.Pop()
		if g[edge[0]][edge[0]] == 0 {
			g[edge[0]][edge[0]] = 1
			cost += int64(edge[1])
			vertices++
			for v := 0; v < n; v++ {
				if g[v][v] == 0 {
					h.Push([2]int{v, g[edge[0]][v]})
				}
			}
		}
	}

	fmt.Println(cost)
}

func main() {
	solve(setup())
}
