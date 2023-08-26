// https://www.acmicpc.net/problem/29261

package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strconv"
)

type Fin struct{ *bufio.Reader }

const GOOS_IS_WINDOWS = (runtime.GOOS == "windows")
const NEVAL = -1

var P int
var primes []int
var isNotPrime []bool

func (fin Fin) ReadInt() (n int) {
	for {
		b, err := fin.ReadByte()
		if b < '0' || '9' < b || err != nil {
			if GOOS_IS_WINDOWS && b == '\r' {
				fin.ReadByte()
			}

			return
		}

		n = n*10 + int(b-'0')
	}
}

func Max[T int](a, b T) T {
	if b <= a {
		return a
	} else {
		return b
	}
}

func IsPrime(n int) bool {
	return !isNotPrime[n]
}

func LowerBound(a []int, n int) int {
	lo, hi := 0, len(a)
	for lo < hi {
		mid := (lo + hi) / 2
		if a[mid] < n {
			lo = mid + 1
		} else {
			hi = mid
		}
	}

	return lo
}

func Init(in Fin) {
	P = in.ReadInt()

	isNotPrime = make([]bool, P+1)
	isNotPrime[0] = true
	isNotPrime[1] = true
	isNotPrime[2] = false
	for i := 2; i <= P; i++ {
		if !isNotPrime[i] {
			primes = append(primes, i)
			for j := 2; i*j < len(isNotPrime); j++ {
				isNotPrime[i*j] = true
			}
		}
	}
}

func Solve() {
	dp := make([]int, P+1)

	for _, k := range primes {
		if P < k {
			break
		}

		for i := LowerBound(primes, k/2); i < len(primes) && primes[i] < k; i++ {
			if IsPrime(k - primes[i] - 1) {
				dp[k] = Max(dp[k], 2+dp[primes[i]]+dp[k-primes[i]-1])
				break
			}
		}
	}

	fmt.Println(strconv.Itoa(dp[P] + 1))
}

func main() {
	Init(Fin{bufio.NewReaderSize(os.Stdin, 1<<8)})
	Solve()
}
