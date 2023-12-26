// https://www.acmicpc.net/problem/20542

#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <string>
#include <vector>

namespace ra = std::ranges;

using i32 = std::int32_t;
using sz = std::size_t;
using i32 = std::int32_t;

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

  auto const n = Read<sz>(std::cin);
  auto const m = Read<sz>(std::cin);
  auto const s1 = Read<std::string>(std::cin) + ' ';
  auto const s2 = Read<std::string>(std::cin) + ' ';

  auto dp = std::vector<std::vector<i32>>(n + 1);
  for (auto& dp_row : dp) {
    dp_row.resize(m + 1, i32());
  }
  for (auto const i : ra::views::iota(sz(1), std::max(n, m) + 1)) {
    if (i <= n) {
      dp[i][0] = static_cast<i32>(i);
    }
    if (i <= m) {
      dp[0][i] = static_cast<i32>(i);
    }
  }
  for (auto const i : ra::views::iota(sz(1), n + 1)) {
    for (auto const j : ra::views::iota(sz(1), m + 1)) {
      auto score = i32(s1[i - 1] == s2[j - 1] ? 0 : 1);
      if ((s1[i - 1] == 'i' && (s2[j - 1] == 'j' || s2[j - 1] == 'l')) ||
          (s1[i - 1] == 'v' && s2[j - 1] == 'w')) {
        score = 0;
      }
      dp[i][j] = std::min({
          dp[i][j - 1] + 1,
          dp[i - 1][j] + 1,
          dp[i - 1][j - 1] + score,
      });
    }
  }
  std::cout << dp.back().back() << std::endl;
  return 0;
}
