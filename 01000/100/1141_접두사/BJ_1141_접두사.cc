// https://www.acmicpc.net/problem/1141

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <string>
#include <vector>

namespace ranges = std::ranges;

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

  auto const n = Read<std::size_t>(std::cin);
  auto words = std::vector<std::string>(n);
  std::copy_n(std::istream_iterator<std::string>(std::cin), n,
              std::begin(words));

  std::sort(
      std::begin(words), std::end(words),
      [](auto const& lhs, auto const& rhs) { return rhs.size() < lhs.size(); });

  auto sets = std::vector<std::vector<std::string>>(0);
  for (auto const& word : words) {
    auto is_outsider = true;
    for (auto& set : sets) {
      auto const is_prefix = ranges::any_of(
          ranges::views::all(set),
          [&word](auto const& e) { return e.starts_with(word); });
      if (!is_prefix) {
        set.emplace_back(word);
        is_outsider = false;
        break;
      }
    }
    if (is_outsider) {
      sets.emplace_back();
      sets.back().emplace_back(word);
    }
  }
  std::cout << ranges::max(sets | ranges::views::transform([](auto const& set) {
                             return set.size();
                           }))
            << std::endl;
  return 0;
}
