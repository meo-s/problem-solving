// https://www.acmicpc.net/problem/13904

package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"runtime"
	"sort"
)

type DisjointSet struct {
	parents []int
}

func (s *DisjointSet) Init() {
	for i := range s.parents {
		s.parents[i] = i
	}
}

func (s *DisjointSet) Find(u int) int {
	if s.parents[u] != u {
		s.parents[u] = s.Find(s.parents[u])
	}
	return s.parents[u]
}

func (s *DisjointSet) Union(u int, v int) bool {
	up, vp := s.Find(u), s.Find(v)
	if up != vp {
		s.parents[v] = up
	}
	return up != vp
}

func NewDisjointSet(size int) *DisjointSet {
	s := &DisjointSet{make([]int, size)}
	s.Init()
	return s
}

func readInt(r io.ByteReader) (n int) {
	const GOOSIsWindows = (runtime.GOOS == "windows")
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOSIsWindows && b == '\r' {
				r.ReadByte()
			}
			return
		}
		n = n*10 + int(b-'0')
	}
}

func setup() [][2]int {
	in := bufio.NewReaderSize(os.Stdin, 1<<14)
	assignments := make([][2]int, readInt(in))
	for i := range assignments {
		assignments[i] = [2]int{readInt(in), readInt(in)}
	}
	sort.Slice(assignments, func(i, j int) bool {
		if assignments[i][1] != assignments[j][1] {
			return assignments[j][1] < assignments[i][1]
		}
		return assignments[i][0] < assignments[j][0]
	})
	return assignments
}

func solve(assignments [][2]int) {
	maxScore, latestIdleDay := 0, NewDisjointSet(1001)
	for _, assignment := range assignments {
		d, w := assignment[0], assignment[1]
		if idleDay := latestIdleDay.Find(d); idleDay != 0 {
			maxScore += w
			latestIdleDay.Union(idleDay-1, idleDay) // (idleDay) -> (idleDay-1)
		}
	}

	fmt.Println(maxScore)
}

func main() {
	solve(setup())
}
