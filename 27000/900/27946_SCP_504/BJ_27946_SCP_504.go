// https://www.acmicpc.net/problem/27946

package main

import (
	"bufio"
	"os"
	"strconv"
)

type Trie struct {
	count int
	words map[uint64]*Trie
}

func NewTrie() *Trie { return &Trie{words: map[uint64]*Trie{}} }

var N, Q int
var dict = NewTrie()
var br = bufio.NewReaderSize(os.Stdin, 1<<20)
var bw = bufio.NewWriterSize(os.Stdout, 1<<20)

func Rdi() (n int) {
	for {
		b, err := br.ReadByte()
		if b == ' ' || b == '\n' || err != nil {
			return
		}
		n = n*10 + int(b-'0')
	}
}

func ToBit(c byte) (bit uint64) {
	switch {
	case '0' <= c && c <= '9':
		bit = uint64(1) << (c - '0')
	case 'a' <= c && c <= 'z':
		bit = uint64(1) << (c - 'a' + 10)
	default:
		bit = uint64(1) << (c - 'A' + 36)
	}
	return
}

func ToBitmask(s []byte) (bitmask uint64) {
	for _, c := range s {
		bitmask |= ToBit(c)
	}
	return
}

func InitDictionary() {
	for i := 0; i < N; i++ {
		line, _, _ := br.ReadLine()
		wordmask := ToBitmask(line)

		node := dict
		bitmask := uint64(0)
		for _, c := range line {
			bit := ToBit(c)
			bitmask |= bit
			if _, hasNext := node.words[bitmask]; !hasNext {
				node.words[bitmask] = NewTrie()
			}

			node = node.words[bitmask]
			if wordmask == bitmask {
				node.count++
				break
			}
		}
	}
}

func ExecuteQuery() {
	ans := 0
	line, _, _ := br.ReadLine()
	node := dict
	bitmask := uint64(0)
	for _, c := range line {
		bitmask |= ToBit(c)
		if next, hasNext := node.words[bitmask]; hasNext {
			node = next
			ans += node.count
		}
	}

	bw.WriteString(strconv.Itoa(ans))
	bw.WriteByte('\n')
}

func main() {
	defer bw.Flush()

	N, Q = Rdi(), Rdi()
	InitDictionary()
	for i := 0; i < Q; i++ {
		ExecuteQuery()
	}
}
