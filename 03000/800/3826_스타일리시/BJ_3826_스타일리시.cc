// https://www.acmicpc.net/problem/3826

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <string>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

auto CountBrackets(std::vector<std::string> const& lines) {
  auto count = std::vector<std::array<std::array<i32, 2>, 3>>(lines.size());
  for (auto i = sz(1); i < lines.size(); ++i) {
    count[i] = count[i - 1];
    for (auto const ch : lines[i - 1]) {
      switch (ch) {
        case '(':
          ++count[i][0][0];
          break;
        case ')':
          ++count[i][0][1];
          break;
        case '{':
          ++count[i][1][0];
          break;
        case '}':
          ++count[i][1][1];
          break;
        case '[':
          ++count[i][2][0];
          break;
        case ']':
          ++count[i][2][1];
          break;
      }
    }
  }
  return count;
}

constexpr auto CalculateIndentation(
    i32 const r, i32 const c, i32 const s,
    std::array<std::array<i32, 2>, 3> const& brackets) noexcept {
  return r * (brackets[0][0] - brackets[0][1]) +
         c * (brackets[1][0] - brackets[1][1]) +
         s * (brackets[2][0] - brackets[2][1]);
}

constexpr auto CalculateIndentation(
    std::tuple<i32, i32, i32> const& rcs,
    std::array<std::array<i32, 2>, 3> const& brackets) noexcept {
  return CalculateIndentation(std::get<0>(rcs), std::get<1>(rcs),
                              std::get<2>(rcs), brackets);
}

auto AnalyzeIndentStyle(std::vector<std::string> const& indented_lines) {
  auto indentations = std::vector<i32>(indented_lines.size());
  for (auto const i : rng::views::iota(sz(), indented_lines.size())) {
    while (indented_lines[i][indentations[i]] == '.') {
      ++indentations[i];
    }
  }

  auto rcs = std::vector<std::tuple<i32, i32, i32>>();
  auto const brackets = CountBrackets(indented_lines);
  for (auto const r : rng::views::iota(i32(1), i32(21))) {
    for (auto const c : rng::views::iota(i32(1), i32(21))) {
      for (auto const s : rng::views::iota(i32(1), i32(21))) {
        if (rng::all_of(rng::views::iota(sz(), indented_lines.size()),
                        [&](auto const i) {
                          return CalculateIndentation(r, c, s, brackets[i]) ==
                                 indentations[i];
                        })) {
          rcs.emplace_back(r, c, s);
        }
      }
    }
  }
  return rcs;
}

void FormatMangledLines(std::vector<std::string> const& mangled_lines,
                        std::vector<std::tuple<i32, i32, i32>> const& rcs) {
  auto const brackets = CountBrackets(mangled_lines);
  for (auto const i : rng::views::iota(sz(), mangled_lines.size())) {
    auto const indentation = CalculateIndentation(rcs[0], brackets[i]);
    auto const check = [&brackets = brackets[i], indentation](auto const& rcs) {
      return CalculateIndentation(rcs, brackets) == indentation;
    };
    std::cout << (rng::all_of(rcs | rng::views::drop(1), check)
                      ? indentation
                      : decltype(indentation)(-1))
              << ' ';
  }
  std::cout << '\n';
}

bool Solve() {
  auto const p = Read<sz>(std::cin);
  auto const q = Read<sz>(std::cin);
  if (p + q == 0) {
    return false;
  }

  auto indented_lines = std::vector<std::string>(p);
  std::copy_n(
      std::make_move_iterator(std::istream_iterator<std::string>(std::cin)), p,
      std::begin(indented_lines));

  auto mangled_lines = std::vector<std::string>(q);
  std::copy_n(
      std::make_move_iterator(std::istream_iterator<std::string>(std::cin)), q,
      std::begin(mangled_lines));

  FormatMangledLines(mangled_lines, AnalyzeIndentStyle(indented_lines));
  return true;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);
  while (Solve())
    ;
  return 0;
}
