// https://www.acmicpc.net/problem/14719

package main

import (
	"bufio"
	"os"
	"strconv"
	"strings"
)

type Stack[T interface{}] []T

func (st *Stack[T]) Size() int     { return len(*st) }
func (st *Stack[T]) IsEmpty() bool { return st.Size() == 0 }
func (st *Stack[T]) Top() T        { return (*st)[st.Size()-1] }
func (st *Stack[T]) Push(v T)      { *st = append(*st, v) }
func (st *Stack[T]) Pop() (ret any) {
	if st.IsEmpty() {
		ret = nil
	} else {
		ret = st.Top()
		*st = (*st)[:st.Size()-1]
	}
	return
}

func ternary[T any](cond bool, tval T, fval T) T {
	if cond {
		return tval
	} else {
		return fval
	}
}

func main() {
	var br = bufio.NewReader(os.Stdin)
	var bw = bufio.NewWriter(os.Stdout)
	defer bw.Flush()

	var W int
	{
		line, _, _ := br.ReadLine()
		tokens := strings.Split(string(line), " ")
		W, _ = strconv.Atoi(tokens[1])
	}

	walls := make([]int, W, W)
	{
		line, _, _ := br.ReadLine()
		tokens := strings.Split(string(line), " ")
		for i := 0; i < W; i++ {
			walls[i], _ = strconv.Atoi(tokens[i])
		}
	}

	st := &Stack[int]{}
	waters := make([]int, W, W)
	for i := range walls {
		if 0 < walls[i] {
			if !st.IsEmpty() {
				for 1 < st.Size() && walls[st.Top()] < walls[i] {
					st.Pop()
				}

				h := ternary(walls[st.Top()] < walls[i], walls[st.Top()], walls[i])
				for j := st.Top() + 1; j < i; j++ {
					waters[j] = h - walls[j]
				}

				if walls[st.Top()] < walls[i] {
					st.Pop()
				}
			}

			st.Push(i)
		}
	}

	ans := 0
	for i := range waters {
		ans += waters[i]
	}

	bw.WriteString(strconv.Itoa(ans))
	bw.WriteByte('\n')
}
