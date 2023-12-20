// https://www.acmicpc.net/problem/12911

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <numeric>
#include <ranges>
#include <vector>

namespace ranges = std::ranges;

using i32 = std::int32_t;
using u64 = std::uint64_t;

constexpr auto MODULAR = u64(1'000'000'007);
using ModPlus =
    decltype([](auto const x, auto const y) { return (x + y) % MODULAR; });

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

  auto n = Read<i32>(std::cin);
  auto const k = Read<i32>(std::cin);

  auto dp = std::array{
      std::vector<u64>(k + 1),
      std::vector<u64>(k + 1, 1),
  };
  dp[1][0] = 0;
  for (; 1 < n; --n) {
    std::swap(dp[0], dp[1]);
    ranges::fill(dp[1] | ranges::views::drop(1),
                 std::accumulate(std::cbegin(dp[0]), std::cend(dp[0]), u64(),
                                 ModPlus()));
    dp[1][1] = dp[0][1];
    for (auto i = i32(2); i <= k; ++i) {
      auto update_dp_table = [&dp, i](auto const j) mutable {
        dp[1][i] = (MODULAR + dp[1][i] - dp[0][j * i]) % MODULAR;
      };
      ranges::for_each(ranges::views::iota(2, k + 1) |
                           ranges::views::take_while(
                               [k, i](auto const j) { return i * j <= k; }),
                       update_dp_table);
    }
  }
  std::cout << std::accumulate(std::cbegin(dp[1]), std::cend(dp[1]), u64(),
                               ModPlus())
            << std::endl;
  return 0;
}
