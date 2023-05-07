// https://www.acmicpc.net/problem/17096

package main

import (
	"bufio"
	"container/heap"
	"fmt"
	"math"
	"os"
)

type PriorityQueue[T any] struct {
	items      []T
	comparator func(T, T) bool
}

func (pq *PriorityQueue[T]) Len() int           { return len(pq.items) }
func (pq *PriorityQueue[T]) Swap(i, j int)      { pq.items[i], pq.items[j] = pq.items[j], pq.items[i] }
func (pq *PriorityQueue[T]) Less(i, j int) bool { return pq.comparator(pq.items[i], pq.items[j]) }
func (pq *PriorityQueue[T]) Top() T             { return pq.items[pq.Len()-1] }
func (pq *PriorityQueue[T]) Push(v any)         { pq.items = append(pq.items, v.(T)) }
func (pq *PriorityQueue[T]) Pop() (v any) {
	v = pq.Top()
	pq.items = pq.items[:pq.Len()-1]
	return
}

type Edge struct {
	u int
	v int
	w int
}

func (this *Edge) GetOpposite(u int) int {
	if this.u == u {
		return this.v
	} else {
		return this.u
	}
}

type City struct {
	before []*Edge
}

var V, E int
var edges = make([]*Edge, 0, 5e+5)
var graph = [5e+5][]*Edge{}
var dists = [5e+5]int{}
var costs = [5e+5]int{}
var br = bufio.NewReaderSize(os.Stdin, 1<<20)

func Rdi() (n int) {
	for {
		b, err := br.ReadByte()
		if b == ' ' || b == '\n' || err != nil {
			return
		}
		n = n*10 + int(b-'0')
	}
}

func InitGraph() {
	V, E = Rdi(), Rdi()
	for i := 0; i < E; i++ {
		u, v, w := Rdi()-1, Rdi()-1, Rdi()
		edges = append(edges, &Edge{u, v, w})
		graph[u] = append(graph[u], edges[i])
		graph[v] = append(graph[v], edges[i])
	}
}

func Dijkstra() {
	for i := 0; i < V; i++ {
		dists[i] = math.MaxInt
	}

	dists[0] = 0
	waypoints := &PriorityQueue[[]int]{
		items:      [][]int{{0, 0}},
		comparator: func(lhs, rhs []int) bool { return lhs[0] < rhs[0] },
	}
	for 0 < waypoints.Len() {
		waypoint := heap.Pop(waypoints).([]int)
		w0, u := waypoint[0], waypoint[1]
		if dists[u] != w0 {
			continue
		}

		for _, edge := range graph[u] {
			v, w := edge.GetOpposite(u), edge.w
			if w0+w <= dists[v] {
				if w0+w < dists[v] {
					dists[v] = w0 + w
					costs[v] = w
					heap.Push(waypoints, []int{dists[v], v})
				} else if w < costs[v] {
					costs[v] = w
				}
			}
		}
	}
}

func main() {
	InitGraph()
	Dijkstra()

	totalCost := 0
	for _, cost := range costs[:V] {
		totalCost += cost
	}
	fmt.Printf("%d\n", totalCost)
}
