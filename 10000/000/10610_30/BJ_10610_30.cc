// https://www.acmicpc.net/problem/10610

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto count = std::array<i32, 10>();
  for (;;) {
    auto const ch = std::cin.get();
    if (std::cin.eof() || ch == '\n') {
      break;
    }
    ++count[ch - '0'];
  }

  auto determinant = i32();
  for (auto i = sz(); i < count.size(); ++i) {
    determinant += static_cast<i32>(i * count[i]);
  }
  if (determinant % 3 != 0 || count[0] == 0) {
    std::cout << -1;
  } else {
    rng::for_each(rng::views::iota(sz(), count.size()) | rng::views::reverse,
                  [&count](auto const i) {
                    if (0 < count[i]) {
                      auto const ch = static_cast<char>('0' + i);
                      rng::fill_n(std::ostream_iterator<char>(std::cout),
                                  count[i], ch);
                    }
                  });
  }
  std::cout << std::endl;
  return 0;
}
