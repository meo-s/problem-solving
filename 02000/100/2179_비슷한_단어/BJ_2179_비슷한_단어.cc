// https://www.acmicpc.net/problem/2179

#include <algorithm>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <ranges>
#include <string>
#include <tuple>
#include <unordered_map>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;

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

  auto words = std::vector<std::string>(Read<sz>(std::cin));
  std::copy_n(
      std::make_move_iterator(std::istream_iterator<std::string>(std::cin)),
      words.size(), std::begin(words));

  auto prefixes = std::unordered_map<std::string, sz>();
  auto best_match = std::tuple<sz, sz, sz>(0, 0, 1);
  rng::for_each(
      rng::views::iota(sz(), words.size()),
      [&, buf = std::string(101, '\0')](sz const index) mutable {
        buf = words[index];
        for (;; buf.pop_back()) {
          if (buf.length() == 0 || buf.length() < std::get<0>(best_match)) {
            break;
          }

          auto const it = prefixes.find(buf);
          if (it == prefixes.end()) {
            prefixes.emplace(buf, index);
            continue;
          }

          if (buf.length() == std::get<0>(best_match)) {
            if (std::get<1>(best_match) <= it->second) {
              continue;
            }
          }
          best_match = {buf.length(), it->second, index};
        }
      });
  std::cout << words[std::get<1>(best_match)] << '\n'
            << words[std::get<2>(best_match)] << std::endl;
  return EXIT_SUCCESS;
}
