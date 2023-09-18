// https://www.acmicpc.net/problem/2066

package main

import (
	"bufio"
	"fmt"
	"os"
)

type dpTable [5][5][5][5][5][5][5][5][5]float64

func setup() [9][4]byte {
	in := bufio.NewReaderSize(os.Stdin, 1<<8)

	cards := [9][4]byte{}
	for i := 0; i < 9; i++ {
		line, _ := in.ReadBytes('\n')
		for j := 0; j < 4; j++ {
			cards[i][j] = line[j*3]
		}
	}

	return cards
}

func min(a, b int) int {
	if a <= b {
		return a
	} else {
		return b
	}
}

func choosePairs(cards [9][4]byte, dp *dpTable, indices [9]int) {
	p := dp[indices[0]][indices[1]][indices[2]][indices[3]][indices[4]][indices[5]][indices[6]][indices[7]][indices[8]]

	if p != 0 {
		branches := 0

		for x := 0; x < 9; x++ {
			for y := x + 1; y < 9; y++ {
				if indices[x] < 4 && indices[y] < 4 && cards[x][3-indices[x]] == cards[y][3-indices[y]] {
					branches++
				}
			}
		}

		for x := 0; x < 9; x++ {
			for y := x + 1; y < 9; y++ {
				if indices[x] < 4 && indices[y] < 4 && cards[x][3-indices[x]] == cards[y][3-indices[y]] {
					next := indices
					next[x]++
					next[y]++
					dp[next[0]][next[1]][next[2]][next[3]][next[4]][next[5]][next[6]][next[7]][next[8]] += p / float64(branches)
				}
			}
		}
	}
}

func solve(cards [9][4]byte) {
	dp := dpTable{}
	dp[0][0][0][0][0][0][0][0][0] = 1.0

	for k := 0; k <= 4*9-2; k += 2 {
		for a := min(k, 4); 0 <= a; a-- {
			for b := min(k-a, 4); 0 <= b; b-- {
				for c := min(k-a-b, 4); 0 <= c; c-- {
					for d := min(k-a-b-c, 4); 0 <= d; d-- {
						for e := min(k-a-b-c-d, 4); 0 <= e; e-- {
							for f := min(k-a-b-c-d-e, 4); 0 <= f; f-- {
								for g := min(k-a-b-c-d-e-f, 4); 0 <= g; g-- {
									for h := min(k-a-b-c-d-e-f-g, 4); 0 <= h; h-- {
										i := k - a - b - c - d - e - f - g - h
										if i <= 4 {
											choosePairs(cards, &dp, [9]int{a, b, c, d, e, f, g, h, i})
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	fmt.Printf("%f\n", float64(dp[4][4][4][4][4][4][4][4][4]))
}

func main() {
	solve(setup())
}
