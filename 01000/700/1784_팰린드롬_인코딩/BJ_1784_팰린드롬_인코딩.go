// https://www.acmicpc.net/problem/1784

package main

import "fmt"

func Init() string {
	var s string
	fmt.Scan(&s)
	return s
}

func Solve(s string) {
	st := make([]byte, 0, len(s))
	st = append(st, s[0])

	s = s[1:]
	for 0 < len(s) && st[len(st)-1] == s[0] {
		s = s[1:]
	}

	for 0 < len(s) {
		st = append(st, s[0])
		s = s[1:]

		if st[len(st)-1] == st[len(st)-2] {
			st = st[:len(st)-1]
			break
		}
	}

	fmt.Printf("%d\n", len(st))
}

func main() {
	Solve(Init())
}
