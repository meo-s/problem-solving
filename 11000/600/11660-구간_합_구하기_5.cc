// https://www.acmicpc.net/problem/11660

#include <array>
#include <iostream>
#include <iterator>

using namespace std;

auto matrix = array<array<int, 1025>, 1025>{};

int main(int argc, char* argv[]) {
  cin.tie(0);
  cout.sync_with_stdio(false);
  auto it = istream_iterator<int>{cin};

  auto const N = *it;
  auto M = *(++it);

  for (auto col = 1; col < N + 1; ++col) {
    for (auto row = 1; row < N + 1; ++row)
      matrix[row][col] = *(++it) + matrix[row - 1][col] + matrix[row][col - 1] - matrix[row - 1][col - 1];
  }

  while (0 < M--) {
    auto const x1 = *(++it), y1 = *(++it), x2 = *(++it), y2 = *(++it);
    cout << matrix[y2][x2] - matrix[y2][x1 - 1] - matrix[y1 - 1][x2] + matrix[y1 - 1][x1 - 1] << '\n';
  }
  
  return 0;
}
