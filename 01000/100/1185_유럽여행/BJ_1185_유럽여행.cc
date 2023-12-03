// https://www.acmicpc.net/problem/1185

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <numeric>
#include <ranges>
#include <tuple>
#include <vector>

using sz = size_t;
using i32 = std::int32_t;
using Edge = std::tuple<i32, i32, i32>;

class DisjointSet {
  mutable std::vector<i32> _parents;

 public:
  DisjointSet(sz const size) : _parents(size) { Clear(); }

  void Clear() { std::iota(std::begin(_parents), std::end(_parents), 0); }

  i32 Find(i32 const u) const {
    if (_parents[u] != u) {
      _parents[u] = Find(_parents[u]);
    }
    return _parents[u];
  }

  bool Merge(i32 const u, i32 const v) {
    auto const up = Find(u);
    auto const vp = Find(v);
    if (up != vp) {
      _parents[vp] = up;
    }
    return up != vp;
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
  std::cin.tie(nullptr);

  auto const n = Read<sz>(std::cin);
  auto const p = Read<sz>(std::cin);

  auto vertices = std::vector<i32>(n);
  std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(vertices));

  auto edges = std::vector<Edge>(p);
  for (auto& edge : edges) {
    std::get<0>(edge) = Read<i32>(std::cin) - 1;
    std::get<1>(edge) = Read<i32>(std::cin) - 1;
    std::get<2>(edge) = Read<i32>(std::cin);
  }
  std::sort(std::begin(edges), std::end(edges),
            [&costs = vertices](auto const& lhs, auto const& rhs) {
              auto const& [ul, vl, wl] = lhs;
              auto const& [ur, vr, wr] = rhs;
              return costs[ul] + costs[vl] + 2 * wl <
                     costs[ur] + costs[vr] + 2 * wr;
            });

  auto total_cost = std::ranges::min(std::views::all(vertices));
  auto disjoint_set = DisjointSet(n);
  for (auto const& edge : edges) {
    auto const& [u, v, w] = edge;
    if (disjoint_set.Merge(u, v)) {
      total_cost += vertices[u] + vertices[v] + 2 * w;
    }
  }
  std::cout << total_cost << std::endl;
  return 0;
}
