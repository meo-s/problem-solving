package main

import (
	"bufio"
	"os"
	"strconv"
)

type Trie struct {
	children []*Trie
	count    int
}

func NewTrie() *Trie {
	return &Trie{make([]*Trie, 'Z'-'A'+1, 'Z'-'A'+1), 0}
}

func Rdi(br *bufio.Reader) (n int) {
	for {
		b, err := br.ReadByte()
		if b == ' ' || b == '\n' || err != nil {
			return
		}
		n = n*10 + int(b-'0')
	}
}

func main() {
	br := bufio.NewReader(os.Stdin)
	bw := bufio.NewWriter(os.Stdout)
	defer bw.Flush()

	N := Rdi(br)
	trie := NewTrie()
	for i := 0; i < N; i++ {
		data, _, _ := br.ReadLine()

		node := trie
		nickname := make([]byte, 0, len(data))
		isNicknameFound := false
		for _, c := range data {
			if !isNicknameFound {
				nickname = append(nickname, c)
			}

			if node.children[c-'a'] == nil {
				node.children[c-'a'] = NewTrie()
				isNicknameFound = true
			}

			node = node.children[c-'a']
		}

		node.count++

		bw.Write(nickname)
		if 1 < node.count {
			bw.WriteString(strconv.Itoa(node.count))
		}
		bw.WriteByte('\n')
	}
}
