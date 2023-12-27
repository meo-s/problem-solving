// https://www.acmicpc.net/problem/15927

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <string>

namespace ra = std::ranges;

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
  std::cout << []() {
    auto s = Read<std::string>(std::cin);
    for (auto i = i32(), j = i32(s.length() - 1); i <= j; ++i, --j) {
      if (s[i] != s[j]) {
        return static_cast<i32>(s.length());
      }
    }
    return ra::any_of(s, [&s](auto const ch) { return s[0] != ch; })
               ? static_cast<i32>(s.length() - 1)
               : i32(-1);
  }() << std::endl;
  return 0;
}
