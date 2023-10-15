// https://www.acmicpc.net/problem/1662

package main

import "fmt"

func uncompress(s string, offset int) (int, int) {
	length := 0
	for i := offset; i < len(s); i++ {
		switch s[i] {
		case ')':
			return length, i
		case '(':
			repeats := int(s[i-1] - '0')
			var qLength int
			qLength, i = uncompress(s, i+1)
			length = (length - 1) + repeats*qLength
		default:
			length++
		}
	}
	return length, len(s)
}

func main() {
	var s string
	fmt.Scan(&s)
	ans, _ := uncompress(s, 0)
	fmt.Println(ans)
}
