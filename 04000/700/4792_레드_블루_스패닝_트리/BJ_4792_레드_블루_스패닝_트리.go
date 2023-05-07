// https://www.acmicpc.net/problem/4792

package main

import (
	"bufio"
	"os"
	"strconv"
	"strings"
)

type Edge struct {
	u, v        int
	isEssential bool
}

type DisjointSet struct {
	remains int
	parents []int
}

func (disjointSet *DisjointSet) Clear() {
	disjointSet.remains = len(disjointSet.parents)
	for i := range disjointSet.parents {
		disjointSet.parents[i] = i
	}
}

func (disjointSet *DisjointSet) Find(u int) int {
	if disjointSet.parents[u] != u {
		disjointSet.parents[u] = disjointSet.Find(disjointSet.parents[u])
	}
	return disjointSet.parents[u]
}

func (disjointSet *DisjointSet) Merge(u, v int) (isMerged bool) {
	up, vp := disjointSet.Find(u), disjointSet.Find(v)
	if up != vp {
		isMerged = true
		disjointSet.remains--
		disjointSet.parents[vp] = disjointSet.parents[up]
	}
	return
}

func NewDisjointSet(N int) *DisjointSet {
	return &DisjointSet{
		remains: 0,
		parents: make([]int, N),
	}
}

const (
	RED       = 0
	BLUE      = 1
	ESSENTIAL = 2
)

var V, E, K int
var e = [][]*Edge{make([]*Edge, 0, 1e+6), make([]*Edge, 0, 1e+6), make([]*Edge, 0, 1e+1)}
var br = bufio.NewReaderSize(os.Stdin, 1<<20)
var bw = bufio.NewWriterSize(os.Stdout, 1<<20)

func ReadEdge() (color int, edge *Edge) {
	line, _, _ := br.ReadLine()
	tokens := strings.Split(string(line), " ")
	u, _ := strconv.Atoi(tokens[1])
	v, _ := strconv.Atoi(tokens[2])
	edge = &Edge{u: u - 1, v: v - 1}
	if line[0] == 'R' {
		color = RED
	} else {
		color = BLUE
	}
	return
}

func InitGraph() bool {
	line, _, _ := br.ReadLine()
	tokens := strings.Split(string(line), " ")
	V, _ = strconv.Atoi(tokens[0])
	E, _ = strconv.Atoi(tokens[1])
	K, _ = strconv.Atoi(tokens[2])
	if V == 0 {
		return false
	}

	e[RED] = e[RED][:0]
	e[BLUE] = e[BLUE][:0]
	for i := 0; i < E; i++ {
		color, edge := ReadEdge()
		e[color] = append(e[color], edge)
	}

	return true
}

func Solve() (ans int) {
	disjointSet := NewDisjointSet(V)
	disjointSet.Clear()
	for _, edge := range e[RED] {
		disjointSet.Merge(edge.u, edge.v)
	}

	k := K
	e[ESSENTIAL] = e[ESSENTIAL][:0]
	for i := 0; 1 < disjointSet.remains && i < len(e[BLUE]); i++ {
		edge := e[BLUE][i]
		if disjointSet.Merge(edge.u, edge.v) {
			k -= 1
			edge.isEssential = true
			e[ESSENTIAL] = append(e[ESSENTIAL], edge)
		}
	}

	if 1 < disjointSet.remains || k < 0 {
		return
	}

	disjointSet.Clear()
	for _, edge := range e[ESSENTIAL] {
		disjointSet.Merge(edge.u, edge.v)
	}
	for i := 0; 0 < k && i < len(e[BLUE]); i++ {
		edge := e[BLUE][i]
		if !edge.isEssential && disjointSet.Merge(edge.u, edge.v) {
			k -= 1
		}
	}

	if k == 0 {
		ans = 1
	}

	return
}

func main() {
	defer bw.Flush()
	for InitGraph() {
		bw.WriteString(strconv.Itoa(Solve()))
		bw.WriteByte('\n')
	}
}
