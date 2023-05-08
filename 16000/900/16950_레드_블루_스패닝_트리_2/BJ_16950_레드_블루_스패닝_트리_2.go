// https://www.acmicpc.net/problem/16950

package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type DisjointSet struct {
	remains int
	parents []int
}

func (set *DisjointSet) Clear() {
	set.remains = len(set.parents)
	for i := range set.parents {
		set.parents[i] = i
	}
}

func (set *DisjointSet) Find(u int) int {
	if set.parents[u] != u {
		set.parents[u] = set.Find(set.parents[u])
	}
	return set.parents[u]
}

func (set *DisjointSet) Merge(u, v int) (isMerged bool) {
	up, vp := set.Find(u), set.Find(v)
	if up != vp {
		isMerged = true
		set.remains--
		set.parents[vp] = up
	}
	return
}

func NewDisjointSet(N int) (set *DisjointSet) {
	set = &DisjointSet{parents: make([]int, N)}
	set.Clear()
	return
}

type Edge struct {
	u, v        int
	isEssential bool
}

var V, E, K int
var g = [][]*Edge{make([]*Edge, 0, 1e+6), make([]*Edge, 0, 1e+6)}
var mst []*Edge
var br = bufio.NewReaderSize(os.Stdin, 2<<20)
var bw = bufio.NewWriterSize(os.Stdout, 1<<20)

const (
	RED  = 0
	BLUE = 1
)

func ReadEdge() (color int, edge *Edge) {
	line, _, _ := br.ReadLine()
	tokens := strings.Split(string(line), " ")
	u, _ := strconv.Atoi(tokens[1])
	v, _ := strconv.Atoi(tokens[2])
	edge = &Edge{u: u - 1, v: v - 1}
	if line[0] == 'B' {
		color = BLUE
	}
	return
}

func InitGraph() {
	line, _, _ := br.ReadLine()
	tokens := strings.Split(string(line), " ")
	V, _ = strconv.Atoi(tokens[0])
	E, _ = strconv.Atoi(tokens[1])
	K, _ = strconv.Atoi(tokens[2])
	for i := 0; i < E; i++ {
		color, edge := ReadEdge()
		g[color] = append(g[color], edge)
	}
}

func ComposeMst() {
	set := NewDisjointSet(V)
	for _, e := range g[RED] {
		set.Merge(e.u, e.v)
	}

	if K < set.remains-1 {
		return
	}

	mst = make([]*Edge, 0, V-1)
	for i := 0; 1 < set.remains && i < len(g[BLUE]); i++ {
		e := g[BLUE][i]
		if set.Merge(e.u, e.v) {
			e.isEssential = true
			mst = append(mst, e)
		}
	}

	if 1 < set.remains {
		mst = nil
	} else {
		set.Clear()
		k := K - len(mst)
		for _, e := range mst {
			set.Merge(e.u, e.v)
		}
		for i := 0; 0 < k && i < len(g[BLUE]); i++ {
			e := g[BLUE][i]
			if !e.isEssential && set.Merge(e.u, e.v) {
				k--
				mst = append(mst, e)
			}
		}

		if 0 < k {
			mst = nil
		} else {
			for i := 0; len(mst) < V-1; i++ {
				e := g[RED][i]
				if set.Merge(e.u, e.v) {
					mst = append(mst, e)
				}
			}
		}
	}
}

func main() {
	defer bw.Flush()
	InitGraph()
	ComposeMst()
	if mst == nil {
		bw.WriteString("0\n")
	} else {
		for _, e := range mst {
			bw.WriteString(fmt.Sprintf("%d %d\n", e.u+1, e.v+1))
		}
		bw.WriteByte('\n')
	}
}
