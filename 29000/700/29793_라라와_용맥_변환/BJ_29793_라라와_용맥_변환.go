// https://www.acmicpc.net/problem/29793

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"unsafe"
)

var VEIN_TYPES = [3]byte{'S', 'R', 'W'}

func setup() (int, int, string) {
	var n, h int
	fmt.Scan(&n, &h)
	in := bufio.NewReaderSize(os.Stdin, 1<<20)
	defaultVeins, _, _ := in.ReadLine()
	return n, h, string(defaultVeins)
}

func min(values ...int) int {
	v := values[0]
	for i := 1; i < len(values); i++ {
		if values[i] < v {
			v = values[i]
		}
	}

	return v
}

func flatten(a *[3][3][3]int) *[3 * 3 * 3]int {
	return (*[3 * 3 * 3]int)(unsafe.Pointer(a))
}

func hunt(dp [][3][3][3]int, h int, defaultVeins string, pos int) {
	for v0, vein := range VEIN_TYPES {
		for v1 := range VEIN_TYPES {
			if v1 == v0 && 1 < h {
				continue
			}

			for v2 := range VEIN_TYPES {
				if (v2 == v0 && 2 < h) || (v2 == v1 && 1 < h) {
					continue
				}

				for v3 := range VEIN_TYPES {
					if (v3 == v1 && 2 < h) || (v3 == v2 && 1 < h) {
						continue
					}

					if dp[0][v1][v2][v3] != math.MaxInt {
						if vein != defaultVeins[pos] {
							dp[1][v0][v1][v2] = min(dp[1][v0][v1][v2], dp[0][v1][v2][v3]+1)
						} else {
							dp[1][v0][v1][v2] = min(dp[1][v0][v1][v2], dp[0][v1][v2][v3])
						}
					}
				}
			}
		}
	}
}

func solve(n, h int, defaultVeins string) {
	NEVAL := make([]int, 3*3*3)
	for i := range NEVAL {
		NEVAL[i] = math.MaxInt
	}

	dp := make([][3][3][3]int, 2)

	for pos := 0; pos < min(n, 3); pos++ {
		copy((*flatten(&dp[0]))[:3*3*3], (*flatten(&dp[1]))[:3*3*3])
		copy((*flatten(&dp[1]))[:3*3*3], NEVAL)
		hunt(dp, h, defaultVeins, pos)
	}

	if 3 < n {
		if 3 < h {
			copy((*flatten(&dp[1]))[:3*3*3], NEVAL)
		} else {
			for pos := 3; pos < n; pos++ {
				copy((*flatten(&dp[0]))[:3*3*3], (*flatten(&dp[1]))[:3*3*3])
				copy((*flatten(&dp[1]))[:3*3*3], NEVAL)
				hunt(dp, h, defaultVeins, pos)
			}
		}
	}

	ans := min((*flatten(&dp[1]))[:3*3*3]...)
	if ans == math.MaxInt {
		ans = -1
	}

	fmt.Println(ans)
}

func main() {
	solve(setup())
}
