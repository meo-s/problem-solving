// https://www.acmicpc.net/problem/6497

package main

import (
	"bufio"
	"io"
	"os"
	"sort"
	"strconv"
)

type DisjointSet []int

func (s *DisjointSet) Clear() {
	for i := range *s {
		(*s)[i] = i
	}
}

func (s *DisjointSet) Find(u int) int {
	if (*s)[u] != u {
		(*s)[u] = s.Find((*s)[u])
	}
	return (*s)[u]
}

func (s *DisjointSet) Merge(u, v int) bool {
	up, vp := s.Find(u), s.Find(v)
	if up != vp {
		(*s)[vp] = up
	}
	return up != vp
}

func readInt(r io.ByteReader) (n int) {
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if b == '\r' {
				continue
			}
			return
		}
		n = n*10 + int(b-'0')
	}
}

func main() {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	out := bufio.NewWriterSize(os.Stdout, 1<<12)
	defer out.Flush()

	for {
		n, m := readInt(in), readInt(in)
		if n+m == 0 {
			break
		}

		g := make([][3]int, 0, m)
		totalCost := 0
		for i := 0; i < m; i++ {
			g = append(g, [3]int{readInt(in), readInt(in), readInt(in)})
			totalCost += g[len(g)-1][2]
		}
		sort.Slice(g, func(i, j int) bool {
			return g[i][2] < g[j][2]
		})

		mst, cost, connections := make(DisjointSet, n), 0, 0
		mst.Clear()
		for i := 0; i < len(g) && connections < n-1; i++ {
			if mst.Merge(g[i][0], g[i][1]) {
				cost += g[i][2]
				connections++
			}
		}

		out.WriteString(strconv.Itoa(totalCost - cost))
		out.WriteByte('\n')
	}
}
