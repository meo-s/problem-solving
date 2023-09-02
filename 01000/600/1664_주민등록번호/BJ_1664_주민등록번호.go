// https://www.acmicpc.net/problem/1955

package main

import (
	"fmt"
	"strconv"
)

func check(constraint string, offset, len, v int) bool {
	for i, n := len-1, 1; 0 <= i; i, n = i-1, n*10 {
		if constraint[offset+i] != 'X' {
			if int(constraint[offset+i]-'0') != (v%(n*10))/n {
				return false
			}
		}
	}
	return true
}

func ValidYear(constraint string, year int) bool {
	return check(constraint, 4, 4, year)
}

func ValidMonth(constraint string, month int) bool {
	return check(constraint, 2, 2, month)
}

func ValidDay(constraint string, day int) bool {
	return check(constraint, 0, 2, day)
}

func LeapYear(year int) bool {
	return (year%4 == 0 && year%100 != 0) || year%400 == 0
}

func Days(year, month int) int {
	if month == 2 && LeapYear(year) {
		return 29
	}
	return [12]int{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}[month-1]
}

func Solve(constraint string) {
	dp := [2][19]uint64{}

	for year := 1; year < 10000; year++ {
		if !ValidYear(constraint, year) {
			continue
		}

		for month := 1; month <= 12; month++ {
			if !ValidMonth(constraint, month) {
				continue
			}

			for day := 1; day <= Days(year, month); day++ {
				if ValidDay(constraint, day) {
					z5 := year / 1000
					z6 := year / 100 % 10
					z7 := year / 10 % 10
					z8 := year % 10
					z3 := month / 10
					z4 := month % 10
					z1 := day / 10
					z2 := day % 10
					dp[1][(z1*10+z2*9+z3*8+z4*7+z5*6+z6*5+z7*4+z8*3)%19]++
				}
			}
		}
	}

	coefficients := [10]int{2, 10, 9, 8, 7, 6, 5, 4, 3, 2}
	for k := 0; k < 10; k++ {
		for i := 0; i < 19; i++ {
			dp[0][i] = dp[1][i]
			dp[1][i] = 0
		}

		for i := 0; i < 19; i++ {
			for ak := 0; ak < 10; ak++ {
				if constraint[8+k] == 'X' || int(constraint[8+k]-'0') == ak {
					dp[1][(i+coefficients[k]*ak)%19] += dp[0][i]
				}
			}
		}
	}

	ans := uint64(0)
	for s, count := range dp[1] {
		if s < 10 && (constraint[18] == 'X' || int(constraint[18]-'0') == s) {
			ans += count
		}
		if 10 <= s && (constraint[18] == 'X' || int(constraint[18]-'0') == (19-s)) {
			ans += count
		}
	}

	fmt.Println(strconv.FormatUint(ans, 10))
}

func main() {
	var constraint string
	fmt.Scan(&constraint)
	Solve(constraint)
}
