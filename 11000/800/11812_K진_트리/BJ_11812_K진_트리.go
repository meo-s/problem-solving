// https://www.acmicpc.net/problem/11812

package main

import (
	"bufio"
	"io"
	"os"
	"runtime"
	"strconv"
)

func abs(v int64) int64 {
	if v < 0 {
		return -v
	} else {
		return v
	}
}

func readInt(r io.ByteReader) (n int64) {
	const GOOSIsWindows = (runtime.GOOS == "windows")
	for {
		b, err := r.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOSIsWindows && b == '\r' {
				continue
			}
			return
		}
		n = n*10 + int64(b-'0')
	}
}

func queryDepth(nodeCounts []int64, node int64) int {
	lb, ub, depth := 0, len(nodeCounts), -1
	for lb < ub {
		mid := (lb + ub) / 2
		if node <= nodeCounts[mid] {
			ub, depth = mid, mid
		} else {
			lb = mid + 1
		}
	}
	return depth
}

func cacheNodeCounts(k, n int64) []int64 {
	nodeCounts, leafs := []int64{0}, int64(1)
	for nodeCounts[len(nodeCounts)-1] < n {
		nodeCounts = append(nodeCounts, nodeCounts[len(nodeCounts)-1]+leafs)
		leafs *= int64(k)
	}
	return nodeCounts
}

func distance(k int64, nodeCounts []int64, x, y int64) (dist int) {
	dx, dy := queryDepth(nodeCounts, x), queryDepth(nodeCounts, y)
	for dx < dy {
		y = (nodeCounts[dy-2] + 1) + (y-nodeCounts[dy-1]-1)/k
		dy--
		dist++
	}
	for dy < dx {
		x = (nodeCounts[dx-2] + 1) + (x-nodeCounts[dx-1]-1)/k
		dx--
		dist++
	}
	for x != y {
		x = (nodeCounts[dx-2] + 1) + (x-nodeCounts[dx-1]-1)/k
		y = (nodeCounts[dy-2] + 1) + (y-nodeCounts[dy-1]-1)/k
		dx--
		dy--
		dist += 2
	}
	return
}

func main() {
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	out := bufio.NewWriterSize(os.Stdout, 1<<16)
	defer out.Flush()

	n, k := readInt(in), readInt(in)
	if 1 < k {
		nodeCounts := cacheNodeCounts(k, n)
		for q := readInt(in); 0 < q; q-- {
			out.WriteString(strconv.Itoa(distance(k, nodeCounts, readInt(in), readInt(in))))
			out.WriteByte('\n')
		}
	} else {
		for q := readInt(in); 0 < q; q-- {
			x, y := readInt(in), readInt(in)
			out.WriteString(strconv.FormatInt(abs(x-y), 10))
			out.WriteByte('\n')
		}
	}
}
