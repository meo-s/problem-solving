# [백준 17626번: Four Squares](https://www.acmicpc.net/problem/17626)
#coding_test/tier/silver/3
#coding_test/type/dynamic_programming
  
자연수 $N$이 주어지면 $N$을 제곱수들의 합으로 표현할 때 필요한 제곱수들의 최소 개수를 구하는 문제이다.  

## 시행 착오

자연수 $N$을 제곱수들의 합으로 표현할 때의 최소 개수를 $C_N$이라고 하자.  

처음 생각한 방법은 자연수 $N$이 주어질 때, $N$이 제곱수가 아니라면 $i \in \mathbb{N} \cap [1, N-1]$인 $i$를 1부터 차례로 증가시켜가며 $C_i + C_{N-i}$의 최솟값을 찾는 방식이였다. 하지만 시간 초과로 실패하였다.  

``` py
import sys
import math

readline = lambda: sys.stdin.readline().strip()

squares = [0, 1] + [float('inf')] * 49999
for i in range(2, len(squares)):
    if int(math.sqrt(i)) ** 2 == n:
        squares[i] = 1
        continue
    for j in range(1, i // 2 + 1):
        squares[i] = min(squares[i], squares[i] + squares[i - j])

print(squares[int(readline())])
```

다음으로 1개의 제곱수가 필요한 수들의 조합으로 2개의 제곱수가 필요한 수들을 찾고, 이어서 1개의 제곱수가 필요한 수와 2개의 제곱수가 필요한 수의 조합으로 3개의 제곱수가 필요한 수를 찾는 방식을 시도하였다. 이미 모든 자연수는 4개 이하의 제곱수들의 합으로 표현될 수 있다는 전제 조건이 있었으므로, 4개의 제곱수가 필요한 경우는 찾을 필요가 없다.  

``` py
import sys
import math
import itertools
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
counts = [0] + [4] * N
sets = defaultdict(list)
for i in range(1, int(math.sqrt(N)) + 1):
    sets[1].append(i**2)
    counts[i**2] = 1

for set1, set2 in [(1, 1), (1, 2)]:
    for n1, n2 in itertools.product(sets[set1], sets[set2]):
        if n1 + n2 <= N and set1 + set2 < counts[n1 + n2]:
            sets[set1 + set2].append(n1 + n2)
            counts[n1 + n2] = set1 + set2

print(counts[N])
```

문제 해결 이후에 다른 사람들의 답안을 보고, 첫번째 방식에서 $C_i+C_{i-j}$가 아닌 $C_i + C_{i-j^2}$의 최솟값을 구하면 된다는 사실을 깨달았다. 어차피 제곱수의 합으로 표현되니, 굳이 제곱수가 아닌 $j$의 경우는 따지지 않아도 되는것..

## 소스 코드

``` py
import sys

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
counts = [0] + [float('inf')] * N
for i in range(1, N + 1):
    j = 1
    while j**2 <= i:
        counts[i] = min(counts[i], counts[i - j**2])
        j += 1
    counts[i] += 1

print(counts[N])
```
