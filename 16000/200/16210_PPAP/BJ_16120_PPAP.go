// https://www.acmicpc.net/problem/16120

package main

import (
	"bufio"
	"bytes"
	"fmt"
	"os"
)

type Stack[T any] []T

func (st Stack[T]) Len() int {
	return len(st)
}

func (st *Stack[T]) Top(n int) []T {
	l := len(*st)
	return (*st)[l-n : l]
}

func (st *Stack[T]) Push(v T) {
	*st = append(*st, v)
}

func (st *Stack[T]) Pop(n int) {
	*st = (*st)[:len(*st)-n]
}

func compressPPAP(st *Stack[byte]) bool {
	if 4 <= st.Len() {
		if bytes.Equal(st.Top(4), []byte{'P', 'P', 'A', 'P'}) {
			st.Pop(4)
			st.Push('P')
			return true
		}
	}
	return false
}

func main() {
	s, _, _ := bufio.NewReaderSize(os.Stdin, 1<<20).ReadLine()

	st := new(Stack[byte])
	for _, c := range s {
		st.Push(c)
		for compressPPAP(st) {
		}
	}

	if st.Len() == 1 && st.Top(1)[0] == 'P' {
		fmt.Println("PPAP")
	} else {
		fmt.Println("NP")
	}
}
