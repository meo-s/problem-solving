// https://www.acmicpc.net/problem/2176

package main

import (
	"bufio"
	"container/heap"
	"fmt"
	"math"
	"os"
	"runtime"
	"strconv"
)

type Fin struct {
	*bufio.Reader
}

type Edge struct {
	to, w int
}

type Item struct {
	value any
	index int
}

type PriorityQueue struct {
	data []Item
	less func(lhs, rhs any) bool
}

const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

var N, M int
var g [][]Edge

func (fin Fin) ReadInt() (n int) {
	for {
		b, err := fin.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				fin.ReadByte()
			}

			return
		}

		n = n*10 + int(b-'0')
	}
}

func (pq PriorityQueue) Len() int { return len(pq.data) }

func (pq PriorityQueue) Less(i, j int) bool {
	return pq.less(pq.data[i].value, pq.data[j].value)
}

func (pq PriorityQueue) Swap(i, j int) {
	pq.data[i], pq.data[j] = pq.data[j], pq.data[i]
	pq.data[i].index = i
	pq.data[j].index = j
}

func (pq *PriorityQueue) Push(v any) {
	pq.data = append(pq.data, Item{
		index: pq.Len(),
		value: v,
	})
}

func (pq *PriorityQueue) Pop() any {
	last := pq.Len() - 1

	item := pq.data[last]
	item.index = -1

	pq.data[last].value = nil
	pq.data = pq.data[:last]

	return item.value
}

func Dijkstra(u int) []int {
	distances := make([]int, N)
	waypoints := PriorityQueue{
		data: make([]Item, 0),
		less: func(lhs, rhs any) bool {
			return lhs.([2]int)[1] < rhs.([2]int)[1]
		},
	}

	for i := 0; i < len(distances); i++ {
		distances[i] = math.MaxInt
	}

	heap.Push(&waypoints, [2]int{u, 0})
	distances[u] = 0

	for 0 < waypoints.Len() {
		waypoint := heap.Pop(&waypoints).([2]int)
		u := waypoint[0]
		distance := waypoint[1]

		if distances[u] != distance {
			continue
		}

		for _, e := range g[u] {
			if distance+e.w < distances[e.to] {
				distances[e.to] = distance + e.w
				heap.Push(&waypoints, [2]int{e.to, distances[e.to]})
			}
		}
	}

	return distances
}

func Init() {
	br := Fin{bufio.NewReaderSize(os.Stdin, 1<<20)}

	N = br.ReadInt()
	M = br.ReadInt()

	g = make([][]Edge, N)
	for i := range g {
		g[i] = make([]Edge, 0, N)
	}

	for i := 0; i < M; i++ {
		u := br.ReadInt() - 1
		v := br.ReadInt() - 1
		w := br.ReadInt()
		g[u] = append(g[u], Edge{v, w})
		g[v] = append(g[v], Edge{u, w})
	}
}

func Solve() {
	distances := Dijkstra(1)
	waypoints := make(chan int, N)

	waypoints <- 0
	blocks := make([]int, N)

	for 0 < len(waypoints) {
		u := <-waypoints

		for _, e := range g[u] {
			if distances[e.to] < distances[u] {
				blocks[e.to]++
				if blocks[e.to] == 1 {
					waypoints <- e.to
				}
			}
		}
	}

	waypoints <- 0
	dp := make([]int, N)
	dp[0] = 1

	for 0 < len(waypoints) {
		u := <-waypoints

		for _, e := range g[u] {
			if distances[e.to] < distances[u] {
				dp[e.to] += dp[u]
				blocks[e.to]--
				if blocks[e.to] == 0 {
					waypoints <- e.to
				}
			}
		}
	}

	fmt.Println(strconv.Itoa(dp[1]))
}

func main() {
	Init()
	Solve()
}
