// https://www.acmicpc.net/problem/1014

#include <algorithm>
#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

using sz = std::size_t;
using u16 = std::uint16_t;
using i32 = std::int32_t;

constexpr auto WALL = 'x';

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  std::cin >> v;
  return v;
}

template <typename Callable>
void PlaceStudent(i32 const w,
                  u16 const base_bitmask,
                  Callable&& callback,
                  u16 const extra_bitmask = 0,
                  i32 const x = 0,
                  i32 const num_students = 0) {
  if (w <= x) {
    callback(extra_bitmask, num_students);
    return;
  }

  PlaceStudent(w, base_bitmask, callback, extra_bitmask, x + 1, num_students);
  if ((base_bitmask & (1 << x)) == 0) {
    auto next_extra_bitmask = extra_bitmask;
    if (x + 1 < w) {
      next_extra_bitmask |= 1 << (x + 1);
    }
    if (0 <= x - 1) {
      next_extra_bitmask |= 1 << (x - 1);
    }
    PlaceStudent(w, base_bitmask, callback, next_extra_bitmask, x + 2,
                 num_students + 1);
  }
}

void Solve() {
  auto const h = Read<i32>(std::cin);
  auto const w = Read<i32>(std::cin);
  auto classroom = std::vector<u16>(h);
  std::ranges::for_each(std::views::all(classroom), [&w](auto& row) {
    for (auto i = i32(); i < w; ++i) {
      if (Read<char>(std::cin) == WALL) {
        row |= 1 << i;
      }
    }
  });

  auto dp = std::array{std::vector<i32>(sz(1) << w, -1),
                       std::vector<i32>(sz(1) << w, -1)};
  dp[1][0] = 0;
  for (auto y = i32(); y < h; ++y) {
    std::swap(dp[0], dp[1]);
    std::ranges::fill(std::views::all(dp[1]), -1);
    for (auto i = u16(); i < (u16(1) << w); ++i) {
      if (dp[0][i] != -1) {
        PlaceStudent(
            w, classroom[y] | i,
            [&dp, i](auto const bitmask, auto const num_students) mutable {
              dp[1][bitmask] =
                  std::max(dp[1][bitmask], dp[0][i] + num_students);
            });
      }
    }
  }

  std::cout << std::ranges::max(std::views::all(dp[1])) << std::endl;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  for (auto t = Read<i32>(std::cin); 0 < t; --t) {
    Solve();
  }
  return 0;
}
