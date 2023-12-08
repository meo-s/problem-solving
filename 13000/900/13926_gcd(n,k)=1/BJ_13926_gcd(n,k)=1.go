// https://www.acmicpc.net/problem/13926

package main

import (
	"fmt"
	"math"
	"math/big"
	"math/rand"
)

type (
	MillerRabin struct{}
	PollardRho  struct{}
)

func Abs(v int64) int64 {
	if v < 0 {
		return -v
	} else {
		return v
	}
}

func GCD(a, b int64) int64 {
	for a%b != 0 {
		r := a % b
		a = b
		b = r
	}
	return b
}

func ModPow(n0, k, modular int64) int64 {
	n, m := big.NewInt(n0), big.NewInt(modular)
	nk := big.NewInt(1)
	for 0 < k {
		if k%2 != 0 {
			nk.Mul(nk, n)
			nk.Mod(nk, m)
		}
		n.Mul(n, n)
		n.Mod(n, m)
		k >>= 1
	}
	return nk.Int64()
}

func (MillerRabin) TestOnce(n int64, a int64) bool {
	for m := n - 1; ; m >>= 1 {
		am := ModPow(a, m, n)
		if am+1 == n {
			return true
		}
		if m%2 == 0 {
			return am == 1
		}
	}
}

func (millerRabin MillerRabin) TestAll(n int64, primes []int64) bool {
	for _, prime := range primes {
		if n <= prime {
			break
		}
		if !millerRabin.TestOnce(n, prime) {
			return false
		}
	}
	return true
}

func (millerRabin MillerRabin) Test(n int64) bool {
	if int64(math.MaxUint32) < n {
		return millerRabin.TestAll(n, []int64{2, 3, 5, 7, 11, 13, 17, 19, 21, 23, 29, 31, 37})
	} else {
		return millerRabin.TestAll(n, []int64{2, 7, 61})
	}
}

func (pollardRho PollardRho) Factorize(n int64, factors *[]int64) {
	if n == 1 {
		return
	}
	if n%2 == 0 {
		(*factors) = append(*factors, 2)
		pollardRho.Factorize(n/2, factors)
		return
	}
	if (MillerRabin{}).Test(n) {
		(*factors) = append(*factors, n)
		return
	}

	c := big.NewInt(rand.Int63n(32) + 1)
	f := func(x int64) int64 {
		y := big.NewInt(x)
		y.Mul(y, y)
		y.Add(y, c)
		y.Mod(y, big.NewInt(n))
		return y.Int64()
	}

	a := rand.Int63n(n-2) + 1
	b := a
	var gcd int64
	for {
		a = f(a)
		b = f(f(b))
		gcd = GCD(Abs(a-b), n)
		if gcd != 1 {
			break
		}
	}
	if gcd == n {
		pollardRho.Factorize(n, factors)
		return
	}
	pollardRho.Factorize(n/gcd, factors)
	pollardRho.Factorize(gcd, factors)
}

func main() {
	var n int64
	fmt.Scan(&n)
	var factors []int64
	PollardRho{}.Factorize(n, &factors)

	nu := big.NewInt(1)
	de := big.NewInt(1)
	usedFactors := map[int64]any{}
	for _, factor := range factors {
		if _, ok := usedFactors[factor]; !ok {
			usedFactors[factor] = nil
			nu.Mul(nu, big.NewInt(factor-1))
			de.Mul(de, big.NewInt(factor))
			gcd := (&big.Int{}).GCD(nil, nil, nu, de)
			nu.Div(nu, gcd)
			de.Div(de, gcd)
		}
	}

	ans := big.NewInt(n)
	ans.Div(ans, de)
	ans.Mul(ans, nu)
	fmt.Println(ans)
}
