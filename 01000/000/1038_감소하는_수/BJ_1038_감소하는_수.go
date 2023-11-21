// https://www.acmicpc.net/problem/1038

package main

import "fmt"

type Heap[T any] struct {
	less func(x, y T) bool
	data []any
}

func (h *Heap[T]) Len() int {
	return len(h.data)
}

func (h *Heap[T]) Peek() T {
	return h.data[0].(T)
}

func (h *Heap[T]) Push(v T) {
	h.data = append(h.data, v)
	h.up(len(h.data) - 1)
}

func (h *Heap[T]) Pop() (v T) {
	n := len(h.data) - 1
	v, h.data[0] = h.data[0].(T), h.data[n]
	h.data[n] = nil
	h.data = h.data[:n]
	h.down(0, n)
	return
}

func (h *Heap[T]) swap(u, v int) {
	h.data[u], h.data[v] = h.data[v], h.data[u]
}

func (h *Heap[T]) up(u int) {
	for {
		p := (u - 1) / 2
		if p == u || h.less(h.data[p].(T), h.data[u].(T)) {
			break
		}
		h.swap(u, p)
		u = p
	}
}

func (h *Heap[T]) down(u, n int) {
	for {
		v := u*2 + 1
		if n <= v || v < 0 {
			break
		}
		if v+1 < n && h.less(h.data[v+1].(T), h.data[v].(T)) {
			v++
		}
		if !h.less(h.data[v].(T), h.data[u].(T)) {
			break
		}
		h.swap(u, v)
		u = v
	}
}

func NewHeap[T any](less func(x, y T) bool) *Heap[T] {
	return &Heap[T]{less: less, data: nil}
}

func main() {
	var n int
	fmt.Scan(&n)
	heap := NewHeap[[3]int64](func(x, y [3]int64) bool {
		return x[0] < y[0]
	})
	for i := int64(0); i <= 9; i++ {
		heap.Push([3]int64{i, i + 1, 10})
	}
	for 0 < n && 0 < heap.Len() {
		n--
		v := heap.Pop()
		for i := v[1]; i <= 9; i++ {
			heap.Push([3]int64{i*v[2] + v[0], i + 1, v[2] * 10})
		}
	}
	if n == 0 && 0 < heap.Len() {
		fmt.Println(heap.Pop()[0])
	} else {
		fmt.Println(-1)
	}
}
