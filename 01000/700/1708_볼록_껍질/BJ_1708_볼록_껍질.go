// https://www.acmicpc.net/problem/1708

package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
)

const NoPoint = -1

type (
	Vec2     [2]int
	IntStack []int
)

func (v Vec2) Sub(other Vec2) Vec2 {
	return [2]int{v[0] - other[0], v[1] - other[1]}
}

func (v Vec2) Cross(other Vec2) int {
	return v[0]*other[1] - v[1]*other[0]
}

func (st IntStack) Empty() bool {
	return len(st) == 0
}

func (st IntStack) Top(index int) int {
	return st[(len(st)-1)-index]
}

func (st IntStack) Bottom(index int) int {
	return st[index]
}

func (st *IntStack) Pop() int {
	n := len(*st) - 1
	v := (*st)[n]
	*st = (*st)[:n]
	return v
}

func (st *IntStack) Push(v int) {
	*st = append(*st, v)
}

func readInt(r io.ByteReader) (n int) {
	sign := 1
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			switch {
			case b == '-':
				sign = -1
				continue
			case b == '\r':
				continue
			default:
				return sign * n
			}
		}
		n = n*10 + int(b-'0')
	}
}

func setup() ([]Vec2, [2][]int) {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)

	n := readInt(in)
	points := make([]Vec2, 0, n)
	for i := 0; i < n; i++ {
		points = append(points, Vec2{readInt(in) + 40000, readInt(in)})
	}

	majorPoints := [2][]int{make([]int, 80001), make([]int, 80001)} // x \in [-40000, 40000]
	for x := 0; x <= 80000; x++ {
		majorPoints[0][x] = NoPoint
		majorPoints[1][x] = NoPoint
	}
	for i := 0; i < n; i++ {
		x, y := points[i][0], points[i][1]
		if majorPoints[0][x] == NoPoint || y < points[majorPoints[0][x]][1] {
			majorPoints[0][x] = i
		}
		if majorPoints[1][80000-x] == NoPoint || points[majorPoints[1][80000-x]][1] < y {
			majorPoints[1][80000-x] = i
		}
	}

	return points, majorPoints
}

func selectVertices(points []Vec2, majorPoints []int, vertices *IntStack) {
	for i := 0; i < len(majorPoints); i++ {
		if majorPoints[i] == NoPoint {
			continue
		}

		if 2 <= len(*vertices) {
			v0 := points[vertices.Bottom(0)]
			vi := points[majorPoints[i]]
			v0vi := vi.Sub(v0)
			for 2 <= len(*vertices) {
				vt := points[vertices.Top(0)]
				vs := points[vertices.Top(1)]
				vsvi := vi.Sub(vs)
				vsvt := vt.Sub(vs)
				v0vt := vt.Sub(v0)
				if vsvi.Cross(vsvt) == 0 {
					vertices.Pop()
					continue
				}
				if v0vt.Cross(v0vi) < 0 || vsvt.Cross(vsvi) < 0 {
					vertices.Pop()
					continue
				}
				break
			}
		}

		if len(*vertices) == 0 || majorPoints[i] != vertices.Bottom(0) {
			vertices.Push(majorPoints[i])
		}
	}
}

func solve(points []Vec2, majorPoints [2][]int) {
	convexHull, vertexCount := make([]bool, len(points)), 0
	vertices := make(IntStack, 0, len(points))
	for i := range majorPoints {
		selectVertices(points, majorPoints[i], &vertices)
		for !vertices.Empty() {
			if vertex := vertices.Pop(); !convexHull[vertex] {
				vertexCount++
				convexHull[vertex] = true
			}
		}
	}
	fmt.Println(vertexCount)
}

func main() {
	solve(setup())
}
