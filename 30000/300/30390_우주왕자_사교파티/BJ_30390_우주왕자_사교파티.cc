// https://www.acmicpc.net/problem/30390

#include <algorithm>
#include <cmath>
#include <iostream>
#include <iterator>
#include <list>

int main(int argc, char const* argv[]) {
    std::ios::sync_with_stdio(false);
    std::cout.tie(nullptr);

    auto in = std::istream_iterator<long long>(std::cin);
    auto const [a, b] = std::minmax({*in, *++in});
    auto const k = *++in;

    auto divisors = std::list<long long>();
    for (int i = 1; i <= static_cast<long long>(std::ceil(std::sqrt(a + b))); ++i) {
        if ((a + b) % i == 0) {
            divisors.emplace_back(i);
        }
    }
    for (auto it = std::crbegin(divisors); it != std::crend(divisors);) {
        auto const divisor = *it++;
        if ((a + b) % divisor == 0 && divisor * divisor != a + b) {
            divisors.emplace_back((a + b) / divisor);
        }
    }

    for (auto it = std::crbegin(divisors); it != std::crend(divisors); ++it) {
        auto const r = a % *it;
        if (std::min(r, *it - r) <= k) {
            std::cout << *it << std::endl;
            break;
        }
    }

    return 0;
}
