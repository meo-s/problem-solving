// https://www.acmicpc.net/problem/29614

package main

import (
	"fmt"
	"strconv"
)

func main() {
	var SCORES = map[string]float64{
		"A+": 4.5,
		"A":  4.0,
		"B+": 3.5,
		"B":  3.0,
		"C+": 2.5,
		"C":  2.0,
		"D+": 1.5,
		"D":  1.0,
		"F":  0.0,
	}

	var grades string
	fmt.Scan(&grades)

	totalGrade := 0.0
	gradeCount := 0
	for i := 0; i < len(grades); {
		ni := i + 1
		if i+1 < len(grades) && grades[i+1] == '+' {
			ni++
		}

		totalGrade += SCORES[grades[i:ni]]
		gradeCount++
		i = ni
	}

	fmt.Println(strconv.FormatFloat(totalGrade/float64(gradeCount), 'f', -1, 64))
}
