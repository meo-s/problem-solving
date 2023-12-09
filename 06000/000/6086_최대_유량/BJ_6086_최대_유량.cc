// https://www.acmicpc.net/problem/6086

#include <algorithm>
#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <limits>
#include <queue>
#include <ranges>

using i32 = std::int32_t;

constexpr auto NUM_VERTICES = i32(('Z' - 'A' + 1) * 2);
static auto caps = std::array<std::array<i32, NUM_VERTICES>, NUM_VERTICES>();
static auto flows = std::array<std::array<i32, NUM_VERTICES>, NUM_VERTICES>();
static auto parents = std::array<i32, NUM_VERTICES>();

constexpr i32 ToInt(char const ch) noexcept {
  if ('A' <= ch && ch <= 'Z') {
    return ch - 'A';
  }
  if ('a' <= ch && ch <= 'z') {
    return ch - 'a' + ('Z' - 'A') + 1;
  }
  return -1;
}

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<i32>(std::cin);
  for (auto i = i32(); i < n; ++i) {
    auto const u = ToInt(Read<char>(std::cin));
    auto const v = ToInt(Read<char>(std::cin));
    auto const cap = Read<i32>(std::cin);
    caps[u][v] += cap;
    caps[v][u] += cap;
  }

  auto total_flow = i32();
  for (;;) {
    std::ranges::fill(std::views::all(parents), -1);
    parents[ToInt('A')] = 0;
    auto waypoints = std::deque<i32>();
    waypoints.emplace_back(ToInt('A'));

    while (!waypoints.empty() && parents[ToInt('Z')] == -1) {
      auto const u = waypoints.front();
      waypoints.pop_front();
      for (auto v = i32(); v < NUM_VERTICES; ++v) {
        if (parents[v] == -1 && 0 < caps[u][v] - flows[u][v]) {
          waypoints.emplace_back(v);
          parents[v] = u;
        }
      }
    }
    if (parents[ToInt('Z')] == -1) {
      break;
    }

    auto flow = std::numeric_limits<i32>::max();
    for (auto u = ToInt('Z'); u != ToInt('A'); u = parents[u]) {
      auto const p = parents[u];
      flow = std::min(flow, caps[p][u] - flows[p][u]);
    }
    for (auto u = ToInt('Z'); u != ToInt('A'); u = parents[u]) {
      auto const p = parents[u];
      flows[p][u] += flow;
      flows[u][p] -= flow;
    }

    total_flow += flow;
  }

  std::cout << total_flow << std::endl;
  return 0;
}
