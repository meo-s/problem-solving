// https://www.acmicpc.net/problem/17218

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <stack>
#include <string>
#include <tuple>
#include <vector>

namespace ranges = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using Index2D = std::pair<i32, i32>;

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

  auto const s =
      std::array{Read<std::string>(std::cin), Read<std::string>(std::cin)};

  auto dp = std::vector<std::vector<std::tuple<i32, Index2D, Index2D>>>(
      s[0].length() + 1);
  for (auto& dp_row : dp) {
    dp_row.resize(s[1].length() + 1, {0, {0, 0}, {-1, -1}});
  }
  for (auto const i : ranges::views::iota(sz(1), s[0].length() + 1)) {
    auto optimal_match = dp[i - 1][0];
    for (auto const j : ranges::views::iota(sz(1), s[1].length() + 1)) {
      using Comparator = decltype([](auto const& lhs, auto const& rhs) {
        return std::get<0>(lhs) < std::get<0>(rhs);
      });

      optimal_match = std::max(optimal_match, dp[i - 1][j - 1], Comparator());

      auto& it = dp[i][j];
      if (s[0][i - 1] == s[1][j - 1]) {
        std::get<0>(it) = std::get<0>(optimal_match) + 1;
        std::get<1>(it) = {static_cast<i32>(i), static_cast<i32>(j)};
        std::get<2>(it) = std::get<1>(optimal_match);
      } else {
        it = std::max(dp[i - 1][j], dp[i][j - 1], Comparator());
      }
    }
  }

  auto password = std::stack<char>();
  auto best_match = &dp.back().back();
  while (std::get<2>(*best_match) != Index2D(-1, -1)) {
    password.emplace(s[1][std::get<1>(*best_match).second - 1]);
    auto const& [i, j] = std::get<2>(*best_match);
    best_match = &dp[i][j];
  }
  while (!password.empty()) {
    std::cout << password.top();
    password.pop();
  }
  std::cout << std::endl;
  return 0;
}
