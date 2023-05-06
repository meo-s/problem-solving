// https://www.acmicpc.net/problem/15552

package main

import (
	"bufio"
	"os"
	"strconv"
	"strings"
)

func main() {
	var br = bufio.NewReader(os.Stdin)
	var bw = bufio.NewWriter(os.Stdout)
	defer bw.Flush()

	br.ReadLine()
	for {
		line, _, err := br.ReadLine()
		if err != nil {
			break
		}

		tokens := strings.Split(string(line), " ")
		A, _ := strconv.Atoi(tokens[0])
		B, _ := strconv.Atoi(tokens[1])
		bw.WriteString(strconv.Itoa(A + B))
		bw.WriteByte('\n')
	}
}
