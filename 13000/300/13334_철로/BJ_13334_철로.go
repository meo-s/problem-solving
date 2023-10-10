// https://www.acmicpc.net/problem/13334

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"sort"
)

type Heap[T any] struct {
	data []any
	less func(a, b T) bool
}

func (h Heap[T]) Len() int {
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
			return sign * n
		}
		n = n*10 + int(b-'0')
	}
}

func setup() (int, [][2]int, int) {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	n := readInt(in)
	lines := make([][2]int, 0, n)
	for i := 0; i < n; i++ {
		h, o := readInt(in), readInt(in)
		if h <= o {
			lines = append(lines, [2]int{h, o})
		} else {
			lines = append(lines, [2]int{o, h})
		}
	}
	return n, lines, readInt(in)
}

func max(a, b int) int {
	if b <= a {
		return a
	} else {
		return b
	}
}

func solve(n int, lines [][2]int, d int) {
	sort.Slice(lines, func(i, j int) bool {
		return lines[i][1] < lines[j][1]
	})

	ans := 0
	commuters := NewHeap[int](func(a, b int) bool { return a < b })
	for i := 0; i < len(lines); i++ {
		if lines[i][1]-lines[i][0] <= d {
			commuters.Push(lines[i][0])
			for 0 < commuters.Len() && d < lines[i][1]-commuters.Peek() {
				commuters.Pop()
			}
			ans = max(ans, commuters.Len())
		}
	}

	fmt.Println(ans)
}

func main() {
	solve(setup())
}
