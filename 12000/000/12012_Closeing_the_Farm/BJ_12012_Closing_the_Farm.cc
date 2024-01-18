// https://www.acmicpc.net/problem/12012

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <numeric>
#include <ranges>
#include <stack>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using Graph = std::vector<std::vector<i32>>;

class DisjointSet {
  mutable std::vector<i32> _parents;
  i32 disjoint_set_count;

 public:
  DisjointSet(i32 const n)
      : _parents(static_cast<sz>(n)), disjoint_set_count(n) {
    std::iota(std::begin(_parents), std::end(_parents), i32());
  }

  [[nodiscard]] i32 Find(i32 const u) const noexcept {
    return _parents[u] != u ? _parents[u] = Find(_parents[u]) : u;
  }

  bool Merge(i32 const u, i32 const v) {
    auto const up = Find(u);
    auto const vp = Find(v);
    if (up != vp) {
      _parents[vp] = up;
      --disjoint_set_count;
    }
    return up != vp;
  }

  [[nodiscard]] i32 Count() const noexcept { return disjoint_set_count; }
};

void RoamFarm(Graph const& g,
              std::vector<bool> const& is_available,
              DisjointSet& disjoint_set,
              std::vector<bool>& visited,
              i32 const u) noexcept {
  visited[u] = true;
  for (auto const v : g[u]) {
    if (is_available[v]) {
      disjoint_set.Merge(u, v);
      if (!visited[v]) {
        RoamFarm(g, is_available, disjoint_set, visited, v);
      }
    }
  }
}

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main([[maybe_unused]] int argc, [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<i32>(std::cin);
  auto const m = Read<i32>(std::cin);
  auto g = Graph(static_cast<sz>(n));
  rng::for_each(rng::views::iota(i32(), m), [&g](auto const) mutable {
    auto const u = Read<i32>(std::cin) - 1;
    auto const v = Read<i32>(std::cin) - 1;
    g[u].emplace_back(v);
    g[v].emplace_back(u);
  });

  auto closed_barns = std::stack<i32>();
  auto is_available = std::vector<bool>(static_cast<sz>(n), true);
  rng::for_each(rng::views::iota(i32(), n), [&](auto const) mutable {
    closed_barns.emplace(Read<i32>(std::cin) - 1);
    is_available[closed_barns.top()] = false;
  });

  auto disjoint_set = DisjointSet(n);
  auto visited = std::vector<bool>(static_cast<sz>(n));
  rng::for_each(rng::views::iota(i32(), n), [&](auto const u) mutable {
    if (is_available[u] && !visited[u]) {
      RoamFarm(g, is_available, disjoint_set, visited, u);
    }
  });

  auto connection_history = std::stack<bool>();
  for (auto i = n; 0 < i; --i) {
    auto const u = closed_barns.top();
    closed_barns.pop();
    is_available[u] = true;
    for (auto const v : g[u]) {
      if (is_available[v]) {
        RoamFarm(g, is_available, disjoint_set, visited, u);
        break;
      }
    }
    connection_history.emplace(disjoint_set.Count() == i);
  }
  while (!connection_history.empty()) {
    std::cout << (connection_history.top() ? "YES" : "NO") << '\n';
    connection_history.pop();
  }
  std::cout.flush();
  return 0;
}
