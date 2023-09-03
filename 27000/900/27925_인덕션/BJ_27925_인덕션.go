// https://www.acmicpc.net/problem/27925

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

var N int
var dishes []int

func Min(x, y int) int {
	if x <= y {
		return x
	} else {
		return y
	}
}

func Abs(x int) int {
	if 0 <= x {
		return x
	} else {
		return -x
	}
}

func Cost(x, y int) int {
	return Min(Abs(y-x), 10-Abs(y-x))
}

func Init() {
	stdin := bufio.NewReaderSize(os.Stdin, 1<<20)

	line, _, _ := stdin.ReadLine()
	N, _ = strconv.Atoi(string(line))

	line, _, _ = stdin.ReadLine()
	tokens := strings.Split(string(line), " ")
	for _, token := range tokens {
		t, _ := strconv.Atoi(token)
		dishes = append(dishes, t)
	}
}

func Solve() {
	dp := make([][10][10][10]int, N+1)
	for k := 0; k < len(dp); k++ {
		for x := 0; x < 10; x++ {
			for y := 0; y < 10; y++ {
				for z := 0; z < 10; z++ {
					dp[k][x][y][z] = math.MaxInt / 2
				}
			}
		}
	}

	dp[0][0][0][0] = 0
	for k, t := range dishes {
		for x := 0; x < 10; x++ {
			for y := 0; y < 10; y++ {
				for z := 0; z < 10; z++ {
					dp[k+1][t][y][z] = Min(dp[k+1][t][y][z], dp[k][x][y][z]+Cost(x, t))
					dp[k+1][x][t][z] = Min(dp[k+1][x][t][z], dp[k][x][y][z]+Cost(y, t))
					dp[k+1][x][y][t] = Min(dp[k+1][x][y][t], dp[k][x][y][z]+Cost(z, t))
				}
			}
		}
	}

	minCost := math.MaxInt
	for x := 0; x < 10; x++ {
		for y := 0; y < 10; y++ {
			for z := 0; z < 10; z++ {
				minCost = Min(minCost, dp[N][dishes[N-1]][y][z])
				minCost = Min(minCost, dp[N][x][dishes[N-1]][z])
				minCost = Min(minCost, dp[N][x][y][dishes[N-1]])
			}
		}
	}

	fmt.Println(strconv.Itoa(minCost))
}

func main() {
	Init()
	Solve()
}
