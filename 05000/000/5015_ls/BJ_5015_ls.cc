// https://www.acmicpc.net/problem/5015

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <limits>
#include <string>

using sz = std::size_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const pattern = Read<std::string>(std::cin);
  auto dp = std::array<sz, 101>();
  for (auto n = Read<sz>(std::cin); 0 < n; --n) {
    auto const filename = Read<std::string>(std::cin);
    std::fill_n(std::begin(dp), filename.length() + 1,
                std::numeric_limits<sz>::max());
    dp[0] = 0;

    for (auto pi = sz(1); pi <= pattern.length(); ++pi) {
      auto const pattern_ch = pattern[pi - 1];
      if (pattern_ch == '*') {
        for (auto i = sz(); i <= filename.length(); ++i) {
          if (dp[i] == pi - 1 || (0 < i && dp[i - 1] == pi)) {
            dp[i] = pi;
          }
        }
      } else {
        for (auto i = filename.length(); 0 < i; --i) {
          if (filename[i - 1] == pattern_ch && dp[i - 1] == pi - 1) {
            dp[i] = pi;
          }
        }
      }
    }
    if (dp[filename.length()] == pattern.length()) {
      std::cout << filename << '\n';
    }
  }

  std::cout.flush();
  return 0;
}
