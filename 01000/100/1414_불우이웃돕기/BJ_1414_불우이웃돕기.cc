// https://www.acmicpc.net/problem/1414

#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <limits>
#include <numeric>
#include <queue>
#include <ranges>
#include <vector>
#include <cmath>

namespace ra = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using Graph = std::vector<std::vector<i32>>;
constexpr auto INF = std::numeric_limits<i32>::max();

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

Graph InitGraph(i32& total_cable_len) {
  auto const n = Read<sz>(std::cin);
  auto g = std::vector<std::vector<i32>>(n);
  for (auto u = sz(); u < n; ++u) {
    g[u].resize(n, INF);
  }
  total_cable_len = 0;
  for (auto u = sz(); u < n; ++u) {
    for (auto v = sz(); v < n; ++v) {
      auto const ch = Read<char>(std::cin);
      if (ch != '0') {
        if ('a' <= ch && ch <= 'z') {
          g[u][v] = static_cast<i32>(ch - 'a' + 1);
        } else {
          g[u][v] = static_cast<i32>(ch - 'A' + 27);
        }
        total_cable_len += g[u][v];
        g[u][v] = g[v][u] = std::min(g[u][v], g[v][u]);
      }
    }
  }
  return g;
}

i32 Prim(Graph const& g) {
  auto cuts =
      std::priority_queue<std::pair<i32, i32>, std::vector<std::pair<i32, i32>>,
                          decltype([](auto const& lhs, auto const& rhs) {
                            return rhs.second < lhs.second;
                          })>();
  auto connected = std::vector<bool>(g.size());
  auto connect = [&](auto const u) mutable {
    connected[u] = true;
    for (auto const v : ra::views::iota(sz(), g.size())) {
      if (g[u][v] != INF && !connected[v]) {
        cuts.emplace(static_cast<i32>(v), g[u][v]);
      }
    }
  };

  connect(0);
  auto mst_cost = i32();
  auto num_edges = sz();
  while (num_edges < g.size() - 1 && !cuts.empty()) {
    auto const [u, w] = cuts.top();
    cuts.pop();
    if (!connected[u]) {
      ++num_edges;
      mst_cost += w;
      connect(u);
    }
  }
  if (num_edges != g.size() - 1) {
    std::cout << -1 << std::endl;
    std::exit(0);
  }
  return mst_cost;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto total_cable_len = i32();
  auto g = InitGraph(total_cable_len);
  std::cout << total_cable_len - Prim(g) << std::endl;
  return 0;
}
