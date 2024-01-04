// https://www.acmicpc.net/problem/2671

package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func UnsafeMust[T any](value T, _ ...any) T {
	return value
}

func IsNoise(sound string) bool {
	if len(sound) == 0 {
		return false
	}
	if strings.HasPrefix(sound, "01") {
		return IsNoise(sound[2:])
	}
	if len(sound) < 4 || !strings.HasPrefix(sound, "100") {
		return true
	}
	sound = sound[3:]
	for 0 < len(sound) && sound[0] == '0' {
		sound = sound[1:]
	}
	for 0 < len(sound) && sound[0] == '1' {
		if !IsNoise(sound[1:]) {
			return false
		}
		sound = sound[1:]
	}
	return true
}

func main() {
	stdin := bufio.NewReaderSize(os.Stdin, 1<<9)
	if !IsNoise(string(UnsafeMust(stdin.ReadLine()))) {
		fmt.Println("SUBMARINE")
	} else {
		fmt.Println("NOISE")
	}
}
