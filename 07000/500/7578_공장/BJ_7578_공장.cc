// https://www.acmicpc.net/problem/7578

#include <cstdint>
#include <iostream>
#include <queue>
#include <unordered_map>
#include <utility>
#include <vector>

using i32 = std::int32_t;
using i64 = std::int64_t;

class FenwickTree {
  std::vector<i32> _nodes;

 public:
  FenwickTree(i32 const size) : _nodes(size) {}

  void Update(i32 n, i32 const v) noexcept {
    while (n < static_cast<i32>(_nodes.size())) {
      _nodes[n - 1] += v;
      n += n & -n;
    }
  }

  i32 Query(i32 n) const noexcept {
    auto v = i32();
    while (0 < n) {
      v += _nodes[n - 1];
      n -= n & -n;
    }
    return v;
  }
};

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cout.tie(nullptr);

  auto n = Read<i32>(std::cin);
  auto indices = std::unordered_map<i32, i32>();
  for (auto i = i32(); i < n; ++i) {
    indices.emplace(Read<i32>(std::cin), i);
  }

  using MinHeap =
      std::priority_queue<std::pair<i32, i32>, std::vector<std::pair<i32, i32>>,
                          decltype([](auto const& lhs, auto const& rhs) {
                            return rhs.first < lhs.first;
                          })>;

  auto ans = i64();
  auto items = MinHeap();
  auto counter = FenwickTree(n);
  for (auto i = i32(); i < n; ++i) {
    items.emplace(indices[Read<i32>(std::cin)], i);
  }
  for (auto i = i32(); i < n; ++i) {
    counter.Update(items.top().second + 1, 1);
    ans += i - counter.Query(items.top().second);
    items.pop();
  }
  std::cout << ans << std::endl;
  return 0;
}
