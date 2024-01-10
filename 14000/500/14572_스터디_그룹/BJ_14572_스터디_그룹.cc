// https://www.acmicpc.net/problem/14572

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;

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

  auto n = Read<i32>(std::cin);
  auto const k = Read<i32>(std::cin);
  auto const d = Read<i32>(std::cin);

  auto students = std::vector<std::pair<i32, std::vector<i32>>>();
  while (0 <= --n) {
    auto problems = std::vector<i32>(Read<sz>(std::cin));
    auto const d = Read<i32>(std::cin);
    rng::for_each(problems, [](auto& problem) { std::cin >> problem; });
    students.emplace_back(d, std::move(problems));
  };

  auto problems = std::vector<i32>(k, i32());
  rng::sort(students, std::less<i32>(), &decltype(students)::value_type::first);
  auto lb = i32();
  auto ub = i32();
  auto ans = i64();
  while (ub < static_cast<i32>(students.size())) {
    rng::for_each(students[ub].second,
                  [&problems](auto const i) mutable { ++problems[i - 1]; });
    while (d < students[ub].first - students[lb].first) {
      rng::for_each(students[lb++].second,
                    [&problems](auto const i) mutable { --problems[i - 1]; });
    }

    ans = std::max(
        ans, (ub - lb + 1) * rng::count_if(problems, [lb, ub](auto const v) {
               return 0 < v && v <= ub - lb ? i64(1) : i64();
             }));
    ++ub;
  }
  std::cout << ans << std::endl;
  return EXIT_SUCCESS;
}
