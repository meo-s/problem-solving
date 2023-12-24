// https://www.acmicpc.net/problem/6137

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
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

  auto num_writtens = std::size_t();
  auto write = [&num_writtens](char const ch) mutable {
    std::cout << ch;
    if (++num_writtens % 80 == 0) {
      std::cout << '\n';
    }
  };

  auto s = std::vector<char>(Read<std::size_t>(std::cin));
  std::copy_n(std::istream_iterator<char>(std::cin), s.size(), std::begin(s));
  auto head = std::size_t();
  auto tail = s.size();
  while (head < tail) {
    auto unresolved = true;
    for (auto i = std::size_t(1); i <= tail && head + (i - 1) < tail - i; ++i) {
      if (s[head + (i - 1)] == s[tail - i]) {
        continue;
      }
      if (s[head + (i - 1)] < s[tail - i]) {
        write(s[head++]);
      } else {
        write(s[--tail]);
      }
      unresolved = false;
      break;
    }
    if (unresolved) {
      write(s[--tail]);
    }
  }
  std::cout.flush();
  return 0;
}
