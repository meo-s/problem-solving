// https://www.acmicpc.net/problem/2758

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <numeric>

using i32 = std::int32_t;
using u64 = std::uint64_t;

constexpr auto MAX_N = std::size_t(10);
constexpr auto MAX_M = std::size_t(2000);

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

  auto dp = std::array<std::array<u64, MAX_M + 1>, MAX_N>();
  for (auto t = Read<i32>(std::cin); 0 < t; --t) {
    auto n = Read<i32>(std::cin);
    auto const m = Read<i32>(std::cin);
    std::fill_n(std::begin(dp[0]) + 1, m, u64(1));
    for (auto k = i32(1); k < n; ++k) {
      auto const it_one = std::cbegin(dp[k - 1]) + 1;
      auto sum = std::accumulate(it_one, it_one + m, u64());
      std::fill_n(std::begin(dp[k]) + 1, m, u64());
      for (auto i = m; 0 < i; --i) {
        if (i * 2 <= m) {
          dp[k][i * 2] = sum;
        }
        if (i * 2 + 1 <= m) {
          dp[k][i * 2 + 1] = sum;
        }
        sum -= dp[k - 1][i];
      }
    }

    std::cout << std::accumulate(std::cbegin(dp[n - 1]) + 1,
                                 std::cbegin(dp[n - 1]) + 1 + m, u64())
              << '\n';
  }

  std::cout.flush();
  return 0;
}
