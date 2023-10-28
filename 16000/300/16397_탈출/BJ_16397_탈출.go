// https://www.acmicpc.net/problem/16397

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

func must[T any](v T, _ ...any) T {
	return v
}

func main() {
	ntg := must(bufio.NewReaderSize(os.Stdin, 1<<5).ReadLine())
	tokens := strings.Split(string(ntg), " ")
	n := must(strconv.Atoi(tokens[0]))
	t := must(strconv.Atoi(tokens[1]))
	g := must(strconv.Atoi(tokens[2]))

	count := make([]int, 100_000)
	for i := range count {
		count[i] = math.MaxInt
	}

	led := make(chan int, len(count))
	led <- n
	count[n] = 0
	for 0 < len(led) && count[g] == math.MaxInt {
		n := <-led
		if n+1 < len(count) && count[n+1] == math.MaxInt {
			count[n+1] = count[n] + 1
			if count[n+1] < t {
				led <- n + 1
			}
		}
		if n*2 < len(count) {
			nextN := n * 2
			for i := 10_000; 0 < i; i /= 10 {
				if nextN/i != 0 {
					nextN -= i
					break
				}
			}
			if count[nextN] == math.MaxInt {
				count[nextN] = count[n] + 1
				if count[nextN] < t {
					led <- nextN
				}
			}
		}
	}

	if count[g] == math.MaxInt {
		fmt.Println("ANG")
	} else {
		fmt.Println(strconv.Itoa(count[g]))
	}
}
