// https://www.acmicpc.net/problem/27941

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strconv"
	"strings"
)

type bufferedReader struct {
	*bufio.Reader
}

func (br *bufferedReader) readInt() (n int) {
	const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
	sign := 1

	for {
		b, err := br.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if b == '-' {
				sign = -1
				continue
			}

			if GOOS_IS_WINDOWS && b == '\r' {
				br.ReadByte()
			}

			return sign * n
		}

		n = n*10 + int(b-'0')
	}
}

func setup() *[2047][11]int {
	in := bufferedReader{bufio.NewReaderSize(os.Stdin, 1<<20)}

	a := [2047][11]int{}
	for i := range a {
		for j := range a[i] {
			a[i][j] = in.readInt()
		}
	}

	return &a
}

func solve(a *[2047][11]int) {
	counter := map[[2]int]int{}
	for i := range a {
		for j := range a[i] {
			counter[[2]int{j, a[i][j]}]++
		}
	}

	exit := [11]int{}
	for k, v := range counter {
		if v != 1<<10 {
			exit[k[0]] = k[1]
		}
	}

	ans := strings.Builder{}
	for _, v := range exit {
		ans.WriteString(strconv.Itoa(v))
		ans.WriteByte(' ')
	}

	fmt.Println(ans.String())
}

func main() {
	solve(setup())
}
