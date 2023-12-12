// https://www.acmicpc.net/problem/5557

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <numeric>
#include <ranges>
#include <vector>

namespace ranges = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using u64 = std::uint64_t;

constexpr auto MIN_SUM = i32(0);
constexpr auto MAX_SUM = i32(20);

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
  auto numbers = std::vector<i32>(n);
  std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(numbers));

  auto dp = std::array{std::vector<u64>(MAX_SUM - MIN_SUM + 1),
                       std::vector<u64>(MAX_SUM - MIN_SUM + 1)};
  dp[1][numbers[0]] = 1;
  for (auto const number : ranges::views::all(numbers) |
                               ranges::views::drop(1) |
                               ranges::views::take(n - 2)) {
    std::swap(dp[0], dp[1]);
    ranges::fill(std::views::all(dp[1]), 0);
    for (auto i = 0; i <= MAX_SUM - MIN_SUM; ++i) {
      if (0 < dp[0][i]) {
        if (i + number <= MAX_SUM - MIN_SUM) {
          dp[1][i + number] += dp[0][i];
        }
        if (0 <= i - number) {
          dp[1][i - number] += dp[0][i];
        }
      }
    }
  }
  std::cout << dp[1][numbers.back()] << std::endl;
  return 0;
}
