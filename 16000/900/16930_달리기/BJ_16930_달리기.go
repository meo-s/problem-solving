// https://www.acmicpc.net/problem/16930

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"runtime"
)

const NEVAL = math.MaxInt
const WALL = '#'

type BufferedReader struct {
	*bufio.Reader
}

type Deque[T any] struct {
	capacity int
	elements []any
	size     int
	head     int
}

type Point struct {
	x, y int
}

type sharedContext struct {
	h, w int
	k    int
	m    [][]byte
	from Point
	goal Point
}

func (br *BufferedReader) ReadInt() (n int) {
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

func NewDeque[T any](capacity int) *Deque[T] {
	return &Deque[T]{
		capacity: capacity,
		elements: make([]any, capacity),
	}
}

func (dq *Deque[T]) Len() int {
	return dq.size
}

func (dq *Deque[T]) Empty() bool {
	return dq.Len() == 0
}

func (dq *Deque[T]) PushFront(v T) {
	if dq.size == dq.capacity {
		panic("Deque: out of capacity")
	}

	dq.size++
	dq.head = ((dq.head - 1) + dq.capacity) % dq.capacity
	dq.elements[dq.head] = v
}

func (dq *Deque[T]) PopFront() T {
	if dq.size == 0 {
		panic("Deque: deque is empty")
	}

	ret := dq.elements[dq.head]
	dq.elements[dq.head] = nil
	dq.head = (dq.head + 1) % dq.capacity
	dq.size--

	return ret.(T)
}

func (dq *Deque[T]) PushBack(v T) {
	if dq.size == dq.capacity {
		panic("Deque: out of memory")
	}

	dq.elements[(dq.head+dq.size)%dq.capacity] = v
	dq.size++
}

func (dq *Deque[T]) PopBack() T {
	if dq.size == 0 {
		panic("Deque: deque is empty")
	}

	dq.size--
	ret := dq.elements[(dq.head+dq.size)%dq.capacity]
	dq.elements[(dq.head+dq.size)%dq.capacity] = nil

	return ret.(T)
}

func (pt *Point) Inside(x1, x2, y1, y2 int) bool {
	return x1 <= pt.x && pt.x < x2 && y1 <= pt.y && pt.y < y2
}

func setup() sharedContext {
	in := BufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}

	h := in.ReadInt()
	w := in.ReadInt()
	k := in.ReadInt()

	m := make([][]byte, h)
	for i := range m {
		m[i] = make([]byte, w)
		line, _ := in.ReadBytes('\n')
		copy(m[i], line)
	}

	sy := in.ReadInt() - 1
	sx := in.ReadInt() - 1
	gy := in.ReadInt() - 1
	gx := in.ReadInt() - 1

	return sharedContext{h, w, k, m, Point{sx, sy}, Point{gx, gy}}
}

func solve(ctx sharedContext) {
	deltas := [4]Point{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}

	visited := make([][]int, ctx.h)
	for y := range visited {
		visited[y] = make([]int, ctx.w)
		for x := range visited[y] {
			visited[y][x] = NEVAL
		}
	}

	visited[ctx.from.y][ctx.from.x] = 0
	waypoints := NewDeque[Point](ctx.h * ctx.w)
	waypoints.PushBack(ctx.from)

	for visited[ctx.goal.y][ctx.goal.x] == NEVAL && !waypoints.Empty() {
		loc := waypoints.PopFront()

		for _, delta := range deltas {
			for i := 1; i <= ctx.k; i++ {
				nextLoc := Point{loc.x + delta.x*i, loc.y + delta.y*i}

				if !nextLoc.Inside(0, ctx.w, 0, ctx.h) || ctx.m[nextLoc.y][nextLoc.x] == WALL {
					break
				}
				if visited[nextLoc.y][nextLoc.x] < visited[loc.y][loc.x]+1 {
					break
				}

				if visited[nextLoc.y][nextLoc.x] == NEVAL {
					visited[nextLoc.y][nextLoc.x] = visited[loc.y][loc.x] + 1
					waypoints.PushBack(nextLoc)
				}
			}
		}
	}

	if visited[ctx.goal.y][ctx.goal.x] != NEVAL {
		fmt.Println(visited[ctx.goal.y][ctx.goal.x])
	} else {
		fmt.Println(-1)
	}
}

func main() {
	solve(setup())
}
