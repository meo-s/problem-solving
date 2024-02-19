// https://www.acmicpc.net/problem/13926

package main

import (
	"bufio"
	"errors"
	"fmt"
	"math/big"
	"os"
)

type BinaryOp func(*big.Int, *big.Int) (*big.Int, error)

type Node struct {
	lhs   *Node
	rhs   *Node
	extra interface{}
}

type ExpressionParser struct {
	e string
	i int
}

func ToBinaryOp(op byte) BinaryOp {
	switch op {
	case '+':
		return func(lhs, rhs *big.Int) (*big.Int, error) {
			return (&big.Int{}).Add(lhs, rhs), nil
		}
	case '-':
		return func(lhs, rhs *big.Int) (*big.Int, error) {
			return (&big.Int{}).Sub(lhs, rhs), nil
		}
	case '*':
		return func(lhs, rhs *big.Int) (*big.Int, error) {
			return (&big.Int{}).Mul(lhs, rhs), nil
		}
	case '/':
		return func(lhs, rhs *big.Int) (*big.Int, error) {
			if rhs.Cmp(big.NewInt(0)) == 0 {
				return nil, errors.New("division by zero")
			}
			return (&big.Int{}).Div(lhs, rhs), nil
		}
	default:
		panic(fmt.Sprintf("unexpected token. expected one of '+', '-', '*', '/', but '%c' was found", op))
	}
}

func UnsafeMust[T any](v T, vars ...any) T {
	return v
}

func (node *Node) Eval() (n *big.Int, err error) {
	if node.lhs != nil && node.rhs != nil {
		var lhs, rhs *big.Int
		if lhs, err = node.lhs.Eval(); err != nil {
			return
		}
		if rhs, err = node.rhs.Eval(); err != nil {
			return
		}
		return node.extra.(BinaryOp)(lhs, rhs)
	} else if node.lhs == nil && node.rhs == nil {
		if n, ok := node.extra.(*big.Int); ok {
			return n, nil
		}
	}
	return nil, errors.New("invariant of expression tree was broken")
}

func (parser *ExpressionParser) next() {
	parser.i++
}

func (parser *ExpressionParser) token() byte {
	return parser.e[parser.i]
}

func (parser *ExpressionParser) done() bool {
	return parser.token() == '$'
}

func (parser *ExpressionParser) number() (*big.Int, error) {
	if parser.token() < '0' || '9' < parser.token() {
		return nil, fmt.Errorf("unexpected token. expected '0'-'9', but '%c' was found", parser.token())
	}

	j := parser.i
	for '0' <= parser.token() && parser.token() <= '9' {
		parser.next()
	}
	return UnsafeMust((&big.Int{}).SetString(parser.e[j:parser.i], 10)), nil
}

func (parser *ExpressionParser) factor() (*Node, error) {
	if token := parser.token(); token == '(' {
		parser.next()
		if node, err := parser.expression(); err != nil {
			return nil, err
		} else {
			if parser.token() != ')' {
				return nil, fmt.Errorf("unexpected token. expected ')', but '%c' was found", parser.token())
			}
			parser.next()
			return node, nil
		}
	} else {
		if n, err := parser.number(); err == nil {
			return &Node{nil, nil, n}, nil
		} else {
			return nil, err
		}
	}
}

func (parser *ExpressionParser) term() (*Node, error) {
	node, err := parser.factor()
	if err != nil {
		return nil, err
	}

	for parser.token() == '*' || parser.token() == '/' {
		op := parser.token()
		parser.next()
		if rhs, err := parser.factor(); err == nil {
			node = &Node{node, rhs, ToBinaryOp(op)}
		} else {
			return nil, err
		}
	}
	return node, nil
}

func (parser *ExpressionParser) expression() (*Node, error) {
	node, err := parser.term()
	if err != nil {
		return nil, err
	}

	for parser.token() == '+' || parser.token() == '-' {
		op := parser.token()
		parser.next()
		if rhs, err := parser.term(); err == nil {
			node = &Node{node, rhs, ToBinaryOp(op)}
		} else {
			return nil, err
		}
	}
	return node, nil
}

func Parse(e string) (*Node, error) {
	parser := &ExpressionParser{e + "$", 0}
	if exp_tree, err := parser.expression(); err != nil {
		return nil, err
	} else if !parser.done() {
		return nil, fmt.Errorf("unexpected token. expected '\\0', but '%c' was found", parser.token())
	} else {
		return exp_tree, nil
	}
}

func main() {
	stdio := bufio.NewReaderSize(os.Stdin, 1<<10)
	if exp_tree, err := Parse(string(UnsafeMust(stdio.ReadLine()))); err != nil {
		fmt.Println("ROCK")
	} else {
		if val, err := exp_tree.Eval(); err != nil {
			fmt.Println("ROCK")
		} else {
			fmt.Println(val)
		}
	}
}
