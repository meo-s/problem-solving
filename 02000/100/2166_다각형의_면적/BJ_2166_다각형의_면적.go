// https://www.acmicpc.net/problem/2166

package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strconv"
)

func readInt(r io.ByteReader) (n int64) {
	sign := int64(1)
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			switch b {
			case '-':
				sign = -1
				continue
			case '\r':
				continue
			default:
				return sign * n
			}
		}
		n = n*10 + int64(b-'0')
	}
}

func abs(v int64) int64 {
	if v < 0 {
		return -v
	} else {
		return v
	}
}

func ccw(u [2]int64, v [2]int64, w [2]int64) int64 {
	return (v[0]-u[0])*(w[1]-u[1]) - (v[1]-u[1])*(w[0]-u[0])
}

func setup() [][2]int64 {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	vertices := make([][2]int64, 0, readInt(in))
	for i := 0; i < cap(vertices); i++ {
		vertices = append(vertices, [2]int64{readInt(in), readInt(in)})
	}
	return vertices
}

func solve(vertices [][2]int64) {
	area := int64(0)
	for i := 1; i+1 < len(vertices); i++ {
		area += ccw(vertices[0], vertices[i], vertices[i+1])
	}
	fmt.Println(strconv.FormatFloat(float64(abs(area))/2, 'f', 1, 64))
}

func main() {
	solve(setup())
}
