// https://www.acmicpc.net/problem/1499

package main

import (
	"fmt"
	"math"
	"strconv"
)

func r(n uint64, i, j int) uint64 {
	var bitmask uint64 = ((1 << (j - i)) - 1) << i
	var reverse uint64 = 0

	for k := i; k < j; k++ {
		reverse |= ((n & (1 << k)) >> k) << (j - (k - i + 1))
	}

	return (n & ^bitmask) | reverse
}

func find(n, g uint64, length int, dp map[[3]uint64]int) int {
	if n == g {
		return 0
	}

	if _, ok := dp[[3]uint64{n, g, uint64(length)}]; !ok {
		dp[[3]uint64{n, g, uint64(length)}] = -1
		count := math.MaxInt

		for s := 0; s < length-1; s++ {
			for e := length; s+1 < e; e-- {
				bitmask := ((uint64(1) << (e - s)) - 1) << s

				if n&^bitmask != g&^bitmask {
					break
				}

				nextN := (r(n, s, e) & bitmask) >> s
				nextG := (g & bitmask) >> s
				if nextCount := find(nextN, nextG, e-s, dp); nextCount != -1 && nextCount+1 < count {
					count = nextCount + 1
				}
			}
		}

		if count != math.MaxInt {
			dp[[3]uint64{n, g, uint64(length)}] = count
		}
	}

	return dp[[3]uint64{n, g, uint64(length)}]
}

func main() {
	var a, b string
	fmt.Scan(&a, &b)

	na, _ := strconv.ParseUint(a, 2, 64)
	nb, _ := strconv.ParseUint(b, 2, 64)
	dp := map[[3]uint64]int{}
	fmt.Println(find(na, nb, len(a), dp))
}
