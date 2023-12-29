// https://www.acmicpc.net/problem/1563

#include <array>
#include <cstdint>
#include <iostream>
#include <vector>

using i32 = std::int32_t;
constexpr auto MODULAR = i32(1'000'000);

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

  auto const n = Read<i32>(std::cin);
  auto dp = std::vector<std::array<std::array<i32, 3>, 2>>(n + 1);
  dp[0] = {{{1, 0, 0}, {0, 0, 0}}};
  for (auto i = i32(1); i <= n; ++i) {
    auto const& prev = dp[i - 1];
    dp[i][0][0] = prev[0][0] + prev[0][1] + prev[0][2];
    dp[i][0][1] = prev[0][0];
    dp[i][0][2] = prev[0][1];
    dp[i][1][0] = dp[i][0][0] + prev[1][0] + prev[1][1] + prev[1][2];
    dp[i][1][1] = prev[1][0];
    dp[i][1][2] = prev[1][1];
    dp[i][0][0] %= MODULAR;
    dp[i][1][0] %= MODULAR;
  }
  std::cout << (dp[n][0][0] + dp[n][0][1] + dp[n][0][2] + dp[n][1][0] +
                dp[n][1][1] + dp[n][1][2]) %
                   MODULAR
            << std::endl;
  return 0;
}
