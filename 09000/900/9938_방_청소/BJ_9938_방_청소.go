// https://www.acmicpc.net/problem/9938

package main

import (
	"bufio"
	"io"
	"os"
	"runtime"
)

const Full = -1

type Cabinet struct {
	parents []int
}

func (s *Cabinet) Init() {
	for i := range s.parents {
		s.parents[i] = i
	}
}

func (s *Cabinet) QueryEmptySpace(u int) int {
	if s.parents[u] != u && s.parents[u] != Full {
		s.parents[u] = s.QueryEmptySpace(s.parents[u])
	}
	return s.parents[u]
}

func (s *Cabinet) Pass(u, v int) {
	up, vp := s.QueryEmptySpace(u), s.QueryEmptySpace(v)
	if up == vp || vp == Full {
		if up != Full {
			s.parents[up] = Full
		}
		if vp != Full {
			s.parents[vp] = Full
		}
	} else {
		s.parents[up] = vp
	}
}

func (s *Cabinet) IsFull(u int) bool {
	return s.QueryEmptySpace(u) == Full
}

func NewDisjointSet(size int) *Cabinet {
	s := &Cabinet{make([]int, size)}
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

func main() {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	out := bufio.NewWriterSize(os.Stdout, 1<<20)
	defer out.Flush()

	n := readInt(in)
	cabinet := NewDisjointSet(readInt(in))
	for ; 0 < n; n-- {
		a, b := readInt(in)-1, readInt(in)-1
		switch {
		case cabinet.QueryEmptySpace(a) == a:
			cabinet.Pass(a, b)
			out.WriteString("LADICA\n")
		case cabinet.QueryEmptySpace(b) == b:
			cabinet.Pass(b, a)
			out.WriteString("LADICA\n")
		case !cabinet.IsFull(a):
			cabinet.Pass(cabinet.QueryEmptySpace(a), b)
			out.WriteString("LADICA\n")
		case !cabinet.IsFull(b):
			cabinet.Pass(cabinet.QueryEmptySpace(b), a)
			out.WriteString("LADICA\n")
		default:
			out.WriteString("SMECE\n")
		}
	}
}
