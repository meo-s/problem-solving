// https://www.acmicpc.net/problem/12888

#include <cstdint>
#include <iostream>
#include <vector>

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);
  std::cout << []() {
    auto dp = std::vector<std::uint64_t>(Read<std::size_t>(std::cin) + 1, 1);
    for (auto i = std::size_t(2); i < dp.size(); ++i) {
      for (auto j = std::size_t(0); j <= i - 2; ++j) {
        dp[i] += 2 * dp[j];
      }
    }
    return dp.back();
  }() << std::endl;
  return 0;
}
