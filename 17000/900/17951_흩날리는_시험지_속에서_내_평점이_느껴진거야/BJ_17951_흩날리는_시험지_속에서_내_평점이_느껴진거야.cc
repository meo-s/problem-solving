// https://www.acmicpc.net/problem/17951

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <numeric>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

[[nodiscard]] bool Try(std::vector<i32> const& sum, i32 const k, i32 const score) noexcept
{
    auto num_groups = i32();
    auto it = std::next(std::cbegin(sum));
    for (; num_groups < k; ++num_groups)
    {
        it = std::lower_bound(it, std::cend(sum), score, [&it](auto const n, auto const threshold) {
            return n - *std::prev(it) < threshold;
        });
        if (it == std::cend(sum))
        {
            break;
        }
        it = std::next(it);
    }
    return num_groups == k;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);
    auto const k = Read<i32>(std::cin);
    auto sum = std::vector<i32>(static_cast<sz>(n + 1));
    rng::transform(rng::views::iota(i32(), n), std::next(std::begin(sum)),
                   [&sum](auto const i) { return sum[i] + Read<i32>(std::cin); });

    std::cout << [&]() {
        auto lo = i32();
        auto hi = sum.back() / k + 1;
        auto ans = i32();
        while (lo < hi)
        {
            auto const mid = (lo + hi) / 2;
            if (Try(sum, k, mid))
            {
                ans = mid;
                lo = mid + 1;
            }
            else
            {
                hi = mid;
            }
        }
        return ans;
    }() << std::endl;
    return EXIT_SUCCESS;
}
