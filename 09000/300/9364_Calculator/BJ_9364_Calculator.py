# https://www.acmicpc.net/problem/9364

import re
import sys


rule = re.compile(r'(\+|-)?(\d*)(X\^?([^+-]*))?')
readline = lambda: sys.stdin.readline().rstrip()
for t in range(int(readline())):
	x = int(readline())
	s = readline()
	val = 0
	while 0 < len(s):
		res = rule.match(s)
		_, end = res.span()
		s = s[end:]
		sign = (1, -1)[int(res.group(1) == '-')]
		c = int(res.group(2) or '1')
		e = int(res.group(4) or '1')
		val += sign * c * (x ** e if res.group(3) else 1)
	print(f'Case #{t + 1}: {val}')
