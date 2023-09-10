// https://www.acmicpc.net/problem/2424

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
)

const (
	ISLAND   = 'I'
	PIRATE   = 'V'
	SEA      = '.'
	SUA      = 'Y'
	TREASURE = 'T'
)

var dy = [4]int{-1, 0, 1, 0}
var dx = [4]int{0, 1, 0, -1}

type sharedContext struct {
	h, w int
	m    [][]byte
}

type bufferedReader struct {
	*bufio.Reader
}

func (br *bufferedReader) ReadInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")

	for {
		b, err := br.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				br.ReadByte()
			}
			return
		}

		n = n*10 + int(b-'0')
	}
}

func setup() sharedContext {
	in := bufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}

	h := in.ReadInt()
	w := in.ReadInt()

	m := make([][]byte, h)
	for y := range m {
		line, _, _ := in.ReadLine()
		m[y] = make([]byte, len(line))
		copy(m[y], line)
	}

	return sharedContext{h, w, m}
}

func validCoordinate(ctx sharedContext, y, x int) bool {
	return 0 <= y && y < ctx.h && 0 <= x && x < ctx.w
}

func block(ctx sharedContext, blocked [][][4]bool, y, x int) {
	for i := range dy {
		blocked[y][x][i] = true

		ny, nx := y+dy[i], x+dx[i]
		for validCoordinate(ctx, ny, nx) && ctx.m[ny][nx] != ISLAND && !blocked[ny][nx][i] {
			blocked[ny][nx][i] = true
			ny += dy[i]
			nx += dx[i]
		}
	}
}

func notBlocked(blocked [][][4]bool, y, x int) bool {
	return !(blocked[y][x][0] || blocked[y][x][1] || blocked[y][x][2] || blocked[y][x][3])
}

func moveSua(ctx sharedContext, ty, tx int, blocked [][][4]bool, waypoints chan [2]int, visited [][]bool) bool {
	numWaypoints := len(waypoints)

	for k := 0; k < numWaypoints; k++ {
		coordinate := <-waypoints
		y := coordinate[0]
		x := coordinate[1]

		if !notBlocked(blocked, y, x) {
			continue
		}

		if y == ty && x == tx {
			return true
		}

		for i := range dy {
			ny := coordinate[0] + dy[i]
			nx := coordinate[1] + dx[i]

			if !validCoordinate(ctx, ny, nx) {
				continue
			}

			if visited[ny][nx] || !notBlocked(blocked, ny, nx) || (ctx.m[ny][nx] != SEA && ctx.m[ny][nx] != TREASURE) {
				continue
			}

			visited[ny][nx] = true
			waypoints <- [2]int{ny, nx}
		}
	}

	return false
}

func movePirate(ctx sharedContext, blocked [][][4]bool, waypoints chan [2]int, visited [][]bool) {
	numWaypoints := len(waypoints)

	for k := 0; k < numWaypoints; k++ {
		coordinate := <-waypoints
		y := coordinate[0]
		x := coordinate[1]

		visited[y][x] = true
		block(ctx, blocked, y, x)

		for i := range dy {
			ny := y + dy[i]
			nx := x + dx[i]

			if validCoordinate(ctx, ny, nx) && !visited[ny][nx] && (ctx.m[ny][nx] != ISLAND) {
				visited[ny][nx] = true
				block(ctx, blocked, ny, nx)
				waypoints <- [2]int{ny, nx}
			}
		}
	}
}

func solve(ctx sharedContext) {
	sy, sx := -1, -1
	py, px := -1, -1
	ty, tx := -1, -1
	for y := 0; y < ctx.h; y++ {
		for x := 0; x < ctx.w; x++ {
			switch ctx.m[y][x] {
			case SUA:
				sy, sx = y, x
			case PIRATE:
				py, px = y, x
			case TREASURE:
				ty, tx = y, x
			}
		}
	}

	blocked := make([][][4]bool, ctx.h)
	suaVisited := make([][]bool, ctx.h)
	pirateVisited := make([][]bool, ctx.h)
	for y := 0; y < ctx.h; y++ {
		blocked[y] = make([][4]bool, ctx.w)
		suaVisited[y] = make([]bool, ctx.w)
		pirateVisited[y] = make([]bool, ctx.w)
	}

	suaWin := false

	suaWaypoints := make(chan [2]int, ctx.h*ctx.w)
	suaWaypoints <- [2]int{sy, sx}
	defer close(suaWaypoints)

	pirateWaypoints := make(chan [2]int, ctx.h*ctx.w)
	pirateWaypoints <- [2]int{py, px}
	defer close(pirateWaypoints)

	for 0 < len(suaWaypoints) {
		suaWin = moveSua(ctx, ty, tx, blocked, suaWaypoints, suaVisited)
		if suaWin {
			break
		}

		movePirate(ctx, blocked, pirateWaypoints, pirateVisited)
	}

	if suaWin {
		fmt.Println("YES")
	} else {
		fmt.Println("NO")
	}
}

func main() {
	solve(setup())
}
