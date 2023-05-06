// https://www.acmicpc.net/problem/1261

package main

import (
	"bufio"
	"container/heap"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

type PriorityQueue[T any] struct {
	items      []T
	comparator func(T, T) bool
}

func (pq *PriorityQueue[T]) Len() int           { return len(pq.items) }
func (pq *PriorityQueue[T]) IsEmpty() bool      { return pq.Len() == 0 }
func (pq *PriorityQueue[T]) Swap(i, j int)      { pq.items[i], pq.items[j] = pq.items[j], pq.items[i] }
func (pq *PriorityQueue[T]) Less(i, j int) bool { return pq.comparator(pq.items[i], pq.items[j]) }
func (pq *PriorityQueue[T]) Push(v any)         { pq.items = append(pq.items, v.(T)) }
func (pq *PriorityQueue[T]) Pop() (item any) {
	item = pq.items[pq.Len()-1]
	pq.items = pq.items[:pq.Len()-1]
	return
}

var dy = []int{-1, 0, 1, 0}
var dx = []int{0, 1, 0, -1}
var br = bufio.NewReader(os.Stdin)

func main() {
	var H, W int
	{
		line, _, _ := br.ReadLine()
		tokens := strings.Split(string(line), " ")
		W, _ = strconv.Atoi(tokens[0])
		H, _ = strconv.Atoi(tokens[1])
	}

	var m = make([][]byte, H, H)
	for y := range m {
		m[y] = make([]byte, W, W)
		for x := range m[y] {
			m[y][x], _ = br.ReadByte()
		}
		br.ReadByte()
	}

	var dists = make([][]int, H, H)
	for y := range dists {
		dists[y] = make([]int, W, W)
		for x := range dists[y] {
			dists[y][x] = math.MaxInt32
		}
	}

	dists[0][0] = 0
	waypoints := &PriorityQueue[[]int]{}
	waypoints.comparator = func(lhs, rhs []int) bool { return lhs[0] < rhs[0] }
	heap.Push(waypoints, []int{0, 0, 0})
	for !waypoints.IsEmpty() {
		waypoint := heap.Pop(waypoints).([]int)
		dist, y, x := waypoint[0], waypoint[1], waypoint[2]
		if dists[y][x] != dist {
			continue
		}

		for i := range dy {
			ny := y + dy[i]
			nx := x + dx[i]
			if 0 <= ny && ny < H && 0 <= nx && nx < W {
				w := (int)(m[ny][nx] - '0')
				if dists[y][x]+w < dists[ny][nx] {
					dists[ny][nx] = dists[y][x] + w
					heap.Push(waypoints, []int{dists[ny][nx], ny, nx})
				}
			}
		}
	}

	fmt.Println(dists[H-1][W-1])
}
