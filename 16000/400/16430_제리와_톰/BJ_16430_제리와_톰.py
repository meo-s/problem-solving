# https://www.acmicpc.net/problem/16430

import math

a, b = map(int, input().split())
print((b - a) // math.gcd(b - a, b), b // math.gcd(b - a, b), sep=' ')
