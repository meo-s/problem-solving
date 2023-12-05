// https://www.acmicpc.net/problem/9024

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <vector>

using i32 = std::int32_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  std::cin >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto s = std::vector<i32>(1'000'000);
  for (auto t = Read<i32>(std::cin); 0 < t--;) {
    auto const n = Read<i32>(std::cin);
    auto const k = Read<i32>(std::cin);
    s.resize(n);
    std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(s));
    std::sort(std::begin(s), std::end(s));

    auto ans = std::pair{std::numeric_limits<i32>::max(), 0};
    auto right = n - 1;
    for (i32 left = 0; left < right; ++left) {
      do {
        auto const diff = std::abs(k - (s[left] + s[right]));
        if (diff < ans.first) {
          ans = {diff, 1};
        } else if (diff == ans.first) {
          ++ans.second;
        }
        if (right - 1 <= left ||
            diff < std::abs(k - (s[left] + s[right - 1]))) {
          break;
        }
        --right;
      } while (true);
    }

    std::cout << ans.second << '\n';
  }

  std::cout.flush();
  return 0;
}
