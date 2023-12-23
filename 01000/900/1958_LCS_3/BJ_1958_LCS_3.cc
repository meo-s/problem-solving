// https://www.acmicpc.net/problem/1958

#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <string>

using sz = std::size_t;
using i32 = std::int32_t;

constexpr auto DP_LEN = sz(101);
auto dp = std::array<std::array<std::array<i32, DP_LEN>, DP_LEN>, DP_LEN>();

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

  auto const s = std::array{
      Read<std::string>(std::cin),
      Read<std::string>(std::cin),
      Read<std::string>(std::cin),
  };

  auto lcs_len = i32();
  for (auto i = sz(1); i <= s[0].length(); ++i) {
    for (auto j = sz(1); j <= s[1].length(); ++j) {
      for (auto k = sz(1); k <= s[2].length(); ++k) {
        auto const w =
            (s[0][i - 1] == s[1][j - 1] && s[1][j - 1] == s[2][k - 1] ? 1 : 0);
        dp[i][j][k] = std::max({
            dp[i - 1][j][k],
            dp[i][j - 1][k],
            dp[i][j][k - 1],
            dp[i - 1][j - 1][k - 1] + w,
        });
        lcs_len = std::max(lcs_len, dp[i][j][k]);
      }
    }
  }
  std::cout << lcs_len << std::endl;
  return 0;
}
