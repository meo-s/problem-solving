// https://www.acmicpc.net/problem/16234

#include <algorithm>
#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <numeric>
#include <queue>
#include <ranges>
#include <vector>

namespace ranges = std::ranges;

using sz = size_t;
using i32 = std::int32_t;
template <typename T>
using Vector2D = std::vector<std::vector<T>>;

constexpr auto DELTAS =
    std::array{std::pair(i32(-1), i32(0)), std::pair(i32(0), i32(1)),
               std::pair(i32(1), i32(0)), std::pair(i32(0), i32(-1))};

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

void FindAlliance(i32 const n,
                  i32 const l,
                  i32 const r,
                  Vector2D<i32> const& populations,
                  i32 const y0,
                  i32 const x0,
                  Vector2D<bool>& visited,
                  std::vector<std::pair<i32, i32>>& alliance) {
  alliance.clear();

  auto coordinates = std::deque<std::pair<i32, i32>>();
  coordinates.emplace_back(y0, x0);
  visited[y0][x0] = true;
  while (!coordinates.empty()) {
    auto const [y, x] = coordinates.front();
    coordinates.pop_front();
    alliance.emplace_back(y, x);

    for (auto const& [dy, dx] : DELTAS) {
      auto const ny = y + dy;
      auto const nx = x + dx;
      if (ny < 0 || n <= ny || nx < 0 || n <= nx || visited[ny][nx]) {
        continue;
      }
      if (auto const population_diff =
              std::abs(populations[ny][nx] - populations[y][x]);
          l <= population_diff && population_diff <= r) {
        coordinates.emplace_back(ny, nx);
        visited[ny][nx] = true;
      }
    }
  }
}

void Migrate(Vector2D<i32>& populations,
             std::vector<std::pair<i32, i32>> const& alliance) {
  auto const total_population = std::accumulate(
      std::cbegin(alliance), std::cend(alliance), i32(),
      [&populations](i32 const sum, std::pair<i32, i32> const& coordinate) {
        return sum + populations[coordinate.first][coordinate.second];
      });

  for (auto const& [y, x] : alliance) {
    populations[y][x] = total_population / static_cast<i32>(alliance.size());
  }
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<i32>(std::cin);
  auto const l = Read<i32>(std::cin);
  auto const r = Read<i32>(std::cin);
  auto populations = std::vector<std::vector<i32>>(n);
  auto visited = std::vector<std::vector<bool>>(n);
  for (auto const y : ranges::views::iota(i32(), n)) {
    populations[y].resize(n);
    visited[y].resize(n);
    std::copy_n(std::istream_iterator<i32>(std::cin), n,
                std::begin(populations[y]));
  }

  auto day = 0;
  auto migrated = true;
  auto alliance = std::vector<std::pair<i32, i32>>();
  while (migrated) {
    ++day;
    migrated = false;
    for (auto const y : ranges::views::iota(i32(), n)) {
      std::fill_n(std::begin(visited[y]), n, false);
    }
    for (auto const y : ranges::views::iota(i32(), n)) {
      for (auto const x : ranges::views::iota(i32(), n)) {
        if (!visited[y][x]) {
          FindAlliance(n, l, r, populations, y, x, visited, alliance);
          if (1 < alliance.size()) {
            Migrate(populations, alliance);
            migrated = true;
          }
        }
      }
    }
  }

  std::cout << day - 1 << std::endl;
  return 0;
}
