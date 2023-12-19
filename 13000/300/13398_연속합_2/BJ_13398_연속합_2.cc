// https://www.acmicpc.net/problem/13398

#include <algorithm>
#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <vector>

namespace ranges = std::ranges;

using i32 = std::int32_t;
using i64 = std::int64_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const seq_len = Read<i32>(std::cin);
  auto dp = std::vector<std::array<i64, 2>>(seq_len, {INT32_MIN, INT32_MIN});
  dp[0][0] = Read<i32>(std::cin);
  for (auto i = i32(1); i < seq_len; ++i) {
    auto const n = Read<i32>(std::cin);
    dp[i][0] = std::max(dp[i - 1][0] + n, i64(n));
    dp[i][1] = std::max(dp[i - 1][1] + n, dp[i - 1][0]);
  }
  std::cout << ranges::max(ranges::views::all(dp) |
                           ranges::views::transform([](auto const& v) {
                             return std::max(v[0], v[1]);
                           }))
            << std::endl;
  return 0;
}
