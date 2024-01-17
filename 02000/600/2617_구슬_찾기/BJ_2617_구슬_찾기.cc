// https://www.acmicpc.net/problem/2617

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <numeric>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using Graph = std::vector<std::vector<i32>>;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

i32 Count(Graph const& g, std::vector<bool>& visited, i32 const u) {
  visited[u] = true;
  return std::accumulate(
      std::cbegin(g[u]), std::cend(g[u]), i32(),
      [&](auto const prev, auto const v) {
        return prev + (visited[v] ? i32() : Count(g, visited, v) + 1);
      });
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<sz>(std::cin);
  auto const m = Read<sz>(std::cin);
  auto lighter = Graph(n);
  auto heavier = Graph(n);
  rng::for_each(rng::views::iota(sz(), m), [&]([[maybe_unused]] auto const i) {
    auto const u = Read<i32>(std::cin) - 1;
    auto const v = Read<i32>(std::cin) - 1;  // v < u
    lighter[u].emplace_back(v);
    heavier[v].emplace_back(u);
  });

  auto visited = std::vector<bool>(n);
  auto count_wrap = [&](Graph const& g, i32 const u) mutable {
    std::fill(std::begin(visited), std::end(visited), false);
    return Count(g, visited, u);
  };

  auto const half_n = static_cast<i32>(n / 2);
  std::cout << rng::count_if(rng::views::iota(i32(), static_cast<i32>(n)),
                             [&](auto const i) {
                               return half_n < count_wrap(lighter, i) ||
                                      half_n < count_wrap(heavier, i);
                             })
            << std::endl;
  return 0;
}
