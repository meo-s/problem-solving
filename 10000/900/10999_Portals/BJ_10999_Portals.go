// https://www.acmicpc.net/problem/10999

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
func (pq *PriorityQueue[T]) Less(i, j int) bool { return pq.comparator(pq.items[i], pq.items[j]) }
func (pq *PriorityQueue[T]) Swap(i, j int)      { pq.items[i], pq.items[j] = pq.items[j], pq.items[i] }
func (pq *PriorityQueue[T]) Top() T             { return pq.items[pq.Len()-1] }
func (pq *PriorityQueue[T]) Push(v any)         { pq.items = append(pq.items, v.(T)) }
func (pq *PriorityQueue[T]) Pop() any {
	v := pq.Top()
	pq.items = pq.items[:pq.Len()-1]
	return v
}

const (
	WALL        byte = '#'
	OPEN_SQUARE byte = '.'
)

var dy = []int{-1, 0, 1, 0}
var dx = []int{0, 1, 0, -1}

var H, W int
var m = [1000][1000]byte{}
var dists = [1000][1000]int{}
var portals = [1000][1000][][]int{}
var portalCosts = [1000][1000]int{}

var br = bufio.NewReader(os.Stdin)

func InitMap() (sy, sx, gy, gx int) {
	line, _, _ := br.ReadLine()
	tokens := strings.Split(string(line), " ")
	H, _ = strconv.Atoi(tokens[0])
	W, _ = strconv.Atoi(tokens[1])
	for y := 0; y < H; y++ {
		for x := 0; x < W; x++ {
			m[y][x], _ = br.ReadByte()
			if m[y][x] == 'C' || m[y][x] == 'S' {
				if m[y][x] == 'C' {
					gy, gx = y, x
				} else {
					sy, sx = y, x
				}
				m[y][x] = OPEN_SQUARE
			}
		}

		br.ReadByte() // \n
	}
	return
}

func InitPortals() {
	waypoints := make(chan []int, H*W)
	for y := 0; y < H; y++ {
		for x := 0; x < W; x++ {
			if m[y][x] != WALL {
				if y == 0 || y == H-1 || x == 0 || x == W-1 {
					portalCosts[y][x] = 1
					waypoints <- []int{y, x}
				}

				portals[y][x] = make([][]int, len(dy))
				continue
			}

			for i := range dy {
				ny := y + dy[i]
				nx := x + dx[i]
				if 0 <= ny && ny < H && 0 <= nx && nx < W {
					if m[ny][nx] == OPEN_SQUARE && portalCosts[ny][nx] == 0 {
						portalCosts[ny][nx] = 1
						waypoints <- []int{ny, nx}
					}
				}
			}
		}
	}
	for 0 < len(waypoints) {
		waypoint := <-waypoints
		y, x := waypoint[0], waypoint[1]
		for i := range dy {
			ny := y + dy[i]
			nx := x + dx[i]
			if 0 <= ny && ny < H && 0 <= nx && nx < W {
				if m[ny][nx] == OPEN_SQUARE && portalCosts[ny][nx] == 0 {
					portalCosts[ny][nx] = portalCosts[y][x] + 1
					waypoints <- []int{ny, nx}
				}
			}
		}
	}
	for y := 0; y < H; y++ {
		for x := 0; x < W; x++ {
			if m[y][x] != WALL {
				for _, i := range []int{0, 3} { // UP & LEFT
					py := y + dy[i]
					px := x + dx[i]
					if (0 <= py && 0 <= px) && m[py][px] != WALL {
						portals[y][x][i] = portals[py][px][i]
					} else {
						portals[y][x][i] = []int{y, x}
					}
				}
			}
		}
	}
	for y := H - 1; 0 <= y; y-- {
		for x := W - 1; 0 <= x; x-- {
			for _, i := range []int{1, 2} { // RIGHT & DOWN
				if m[y][x] != WALL {
					py := y + dy[i]
					px := x + dx[i]
					if (py < H && px < W) && m[py][px] != WALL {
						portals[y][x][i] = portals[py][px][i]
					} else {
						portals[y][x][i] = []int{y, x}
					}
				}
			}
		}
	}
}

func Dijkstra(sy, sx int) {
	for y := 0; y < H; y++ {
		for x := 0; x < W; x++ {
			dists[y][x] = math.MaxInt32
		}
	}

	waypoints := &PriorityQueue[[]int]{}
	waypoints.comparator = func(lhs, rhs []int) bool { return lhs[0] < rhs[0] }
	dists[sy][sx] = 0

	heap.Push(waypoints, []int{0, sy, sx})
	for !waypoints.IsEmpty() {
		waypoint := heap.Pop(waypoints).([]int)
		w0, y, x := waypoint[0], waypoint[1], waypoint[2]
		if w0 != dists[y][x] {
			continue
		}

		for i := range dy {
			ny := y + dy[i]
			nx := x + dx[i]
			if 0 <= ny && ny < H && 0 <= nx && nx < W {
				if m[ny][nx] == OPEN_SQUARE && w0+1 < dists[ny][nx] {
					dists[ny][nx] = w0 + 1
					heap.Push(waypoints, []int{dists[ny][nx], ny, nx})
				}
			}
		}
		for i := range portals[y][x] {
			ny := portals[y][x][i][0]
			nx := portals[y][x][i][1]
			if ny != y || nx != x {
				if w0+portalCosts[y][x] < dists[ny][nx] {
					dists[ny][nx] = w0 + portalCosts[y][x]
					heap.Push(waypoints, []int{dists[ny][nx], ny, nx})
				}
			}
		}
	}

}

func main() {
	sy, sx, gy, gx := InitMap()
	InitPortals()
	Dijkstra(sy, sx)
	fmt.Printf("%d\n", dists[gy][gx])
}
