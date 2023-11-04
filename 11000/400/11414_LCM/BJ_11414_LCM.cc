// https://www.acmicpc.net/problem/11414

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <limits>
#include <list>
#include <numeric>
#include <type_traits>
#include <utility>

using i64 = std::int64_t;

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto in = std::istream_iterator<i64>(std::cin);
    auto const [a, b] = std::minmax({*in, *++in});
    auto divisors = std::list<i64>();
    auto const limit = static_cast<i64>(std::floor(std::sqrt(b - a)));
    for (std::remove_cvref_t<decltype(limit)> i = 1; i <= limit; ++i) {
        if ((b - a) % i == 0) {
            divisors.emplace_back(i);
        }
    }
    for (auto it = std::crbegin(divisors); it != std::crend(divisors);) {
        auto const divisor = *it++;
        if (divisor * divisor != b - a) {
            divisors.emplace_back((b - a) / divisor);
        }
    }

    auto ans = std::pair<i64, i64>{std::numeric_limits<i64>::max(), 1};
    for (auto const divisor : divisors) {
        auto const n = divisor - a % divisor;
        auto const lcm = std::lcm(a + n, b + n);
        if (lcm < std::get<0>(ans)) {
            ans = {lcm, n};
        } else if (lcm == std::get<0>(ans) && n < std::get<1>(ans)) {
            std::get<1>(ans) = n;
        }
    }

    std::cout << std::get<1>(ans) << std::endl;
    return 0;
}
