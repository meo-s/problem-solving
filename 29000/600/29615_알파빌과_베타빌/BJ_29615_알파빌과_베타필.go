// https://www.acmicpc.net/problem/29615

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strconv"
)

type BufferedReader struct {
	*bufio.Reader
}

type SharedContext struct {
	n, m    int
	waiting []int
	friends map[int]any
}

func (br BufferedReader) ReadInt() (n int) {
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

func Init() SharedContext {
	in := BufferedReader{bufio.NewReaderSize(os.Stdin, 1<<16)}

	N := in.ReadInt()
	M := in.ReadInt()

	waiting := make([]int, 0, N)
	for i := 0; i < N; i++ {
		waiting = append(waiting, in.ReadInt())
	}

	friends := map[int]any{}
	for i := 0; i < M; i++ {
		friends[in.ReadInt()] = nil
	}

	return SharedContext{N, M, waiting, friends}
}

func Solve(ctx SharedContext) {
	swapCount := 0
	for i := 0; i < ctx.m; i++ {
		if _, isFriend := ctx.friends[ctx.waiting[i]]; !isFriend {
			swapCount++
		}
	}

	fmt.Println(strconv.Itoa(swapCount))
}

func main() {
	Solve(Init())
}
