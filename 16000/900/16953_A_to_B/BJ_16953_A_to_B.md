# [백준 16953번: A -> B](https://www.acmicpc.net/problem/16953)
#coding_test/tier/silver/2
#coding_test/type/greedy
#coding_test/type/graph
#coding_test/type/bfs

*풀이에 도움이 된 문제*  

- [백준 1464번: 1로 만들기](../../01000/400/1463-1로_만들기.md)  

## 문제 요약

$A, B \in \mathbb{N} \cap [1, 10^9], A \lt B$가 주어진다. $A$에는 다음과 같은 두 가지 연산을 수행할 수 있다.  

- 2를 곱한다.  
- 1을 수의 가장 오른쪽에 추가한다.  

이때, $A$를 $B$로 만들기 위해 필요한 최소 연산 횟수를 구하는 문제이다.  

## 시행 착오

처음에는 BFS를 사용하는 방법으로 문제에 접근했다. 첫 제출은 거리를 저장하기 위해 길이가 $10^9+1$인 배열을 사용하여 메모리 초과로 실패하였다. 그래서 거리를 저장하는 배열을 사용하지 않고 덱에 현재의 $A$값과 연산 횟수 $C$의 쌍을 저장하는 방식으로 풀이에 성공하였다.  

풀이 통과 이후 다른 사람의 풀이를 보다가 그리디 방식으로도 해결 할 수 있는 것을 알았다. $B$의 위치에 따라 할 수 있는 연산이 하나로 제한되기 때문이다.

- $B$의 끝자리 $1$이라면, 반드시 $B=\lfloor B/10 \rfloor$의 연산만 수행할 수 있다.  
- $B$가 짝수라면, 반드시 $B=B/2$의 연산만 수행할 수 있다.
- 그 외의 경우에는 $A$를 $B$로 만들 수 없다.


## 소스 코드

### BFS 풀이

``` python
from collections import deque

x, goal = map(int, input().split())

goal_dist = None
points = deque([(x, 1)])
while 0 < len(points):
    x, dist = points.popleft()
    if x == goal:
        goal_dist = dist
        break

    for x_next in [x * 2, x * 10 + 1]:
        if 1 <= x_next <= goal:
            points.append((x_next, dist + 1))

print([-1, goal_dist][goal_dist is not None])
```

### 그리디 풀이

``` python
dist = 1
while x < goal:
    dist += 1
    if goal % 10 == 1:
        goal //= 10
    elif goal & 1 == 0:
        goal //= 2
    else:
        break

print([-1, dist][x == goal])
```
