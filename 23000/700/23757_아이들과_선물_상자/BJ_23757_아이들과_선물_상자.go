// https://www.acmicpc.net/problem/23757

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
)

type (
	Reader struct {
		*bufio.Reader
	}

	Heap[T any] struct {
		less func(x, y T) bool
		data []any
	}
)

func (r Reader) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				r.ReadByte()
			}
			return n
		}
		n = n*10 + int(b-'0')
	}
}

func (h *Heap[T]) Len() int {
	return len(h.data)
}

func (h *Heap[T]) Peek() T {
	return h.data[0].(T)
}

func (h *Heap[T]) Push(v T) {
	h.data = append(h.data, v)
	h.up(h.Len() - 1)
}

func (h *Heap[T]) Pop() (v T) {
	n := h.Len() - 1
	v, h.data[0] = h.data[0].(T), h.data[n]
	h.data[n] = nil
	h.data = h.data[:n]
	h.down(0, n)
	return
}

func (h *Heap[T]) swap(i, j int) {
	h.data[i], h.data[j] = h.data[j], h.data[i]
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

func (h *Heap[T]) down(u0, n int) bool {
	u := u0
	for {
		v1 := 2*u + 1
		if n <= v1 || v1 < 0 {
			break
		}
		v := v1
		if v2 := v1 + 1; v2 < n && h.less(h.data[v2].(T), h.data[v1].(T)) {
			v = v2
		}
		if !h.less(h.data[v].(T), h.data[u].(T)) {
			break
		}
		h.swap(u, v)
		u = v
	}
	return u0 < u
}

func NewHeap[T any](less func(x, y T) bool) *Heap[T] {
	return &Heap[T]{less: less}
}

func main() {
	in := Reader{bufio.NewReaderSize(os.Stdin, 1<<18)}

	n, m := in.ReadInt(), in.ReadInt()
	presents := NewHeap[int](func(x, y int) bool { return x > y })
	for i := 0; i < n; i++ {
		presents.Push(in.ReadInt())
	}

	for ; 0 < m; m-- {
		wish := in.ReadInt()
		if presents.Peek() < wish {
			fmt.Println(0)
			os.Exit(0)
		}
		if remains := presents.Pop() - wish; 0 < remains {
			presents.Push(remains)
		}
	}
	fmt.Println(1)
}
