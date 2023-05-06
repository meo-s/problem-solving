package main

import (
	"bufio"
	"math"
	"os"
	"strconv"
)

type Edge struct {
	to   int
	cost int
}

func Rdi(br *bufio.Reader) (n int) {
	for {
		b, err := br.ReadByte()
		if b == ' ' || b == '\n' || err != nil {
			return
		}
		n = n*10 + (int)(b-'0')
	}
}

func cache(tree [][]Edge) ([]int, [][]int, [][]int) {
	V := len(tree)
	L := int(math.Log2(float64(V))) + 1
	depths := make([]int, len(tree), len(tree))
	jumps := make([][]int, V, V)
	costs := make([][]int, V, V)
	for i := 0; i < V; i++ {
		jumps[i] = make([]int, L, L)
		costs[i] = make([]int, L, L)
	}

	vertices := make(chan []int, len(tree))
	vertices <- []int{0, 0, 0}
	for 0 < len(vertices) {
		uwp := <-vertices
		u, w, p := uwp[0], uwp[1], uwp[2]
		jumps[u][0] = p
		costs[u][0] = w

		for _, edge := range tree[u] {
			if edge.to != p {
				depths[edge.to] = depths[u] + 1
				vertices <- []int{edge.to, edge.cost, u}
			}
		}
	}

	for i := 1; i < L; i++ {
		for u := 0; u < V; u++ {
			jumps[u][i] = jumps[jumps[u][i-1]][i-1]
			costs[u][i] = costs[jumps[u][i-1]][i-1] + costs[u][i-1]
		}
	}

	return depths, jumps, costs
}

func main() {
	var br = bufio.NewReader(os.Stdin)
	var bw = bufio.NewWriter(os.Stdout)
	defer bw.Flush()

	V := Rdi(br)
	L := int(math.Log2(float64(V))) + 1
	tree := make([][]Edge, V, V)
	for i := 1; i < V; i++ {
		u := Rdi(br) - 1
		v := Rdi(br) - 1
		w := Rdi(br)
		tree[u] = append(tree[u], Edge{v, w})
		tree[v] = append(tree[v], Edge{u, w})
	}

	depths, jumps, costs := cache(tree)
	Q := Rdi(br)
	for i := 0; i < Q; i++ {
		u := Rdi(br) - 1
		v := Rdi(br) - 1
		if depths[u] < depths[v] {
			u, v = v, u
		}

		w := 0
		for i := L - 1; 0 <= i; i-- {
			if depths[u] == depths[v] {
				break
			}
			if depths[v] <= depths[jumps[u][i]] {
				w += costs[u][i]
				u = jumps[u][i]
			}
		}
		for i := L - 1; 0 <= i; i-- {
			if jumps[u][i] != jumps[v][i] {
				w += costs[u][i] + costs[v][i]
				u = jumps[u][i]
				v = jumps[v][i]
			}
		}
		if u != v {
			w += costs[u][0] + costs[v][0]
		}

		bw.WriteString(strconv.Itoa(w))
		bw.WriteByte('\n')
	}
}
