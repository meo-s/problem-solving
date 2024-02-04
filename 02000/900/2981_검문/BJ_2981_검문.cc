// https://www.acmicpc.net/problem/2981

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <iterator>
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

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);

    auto nums = std::vector<i32>(static_cast<sz>(n));
    std::copy_n(std::istream_iterator<i32>(std::cin), nums.size(), std::begin(nums));
    rng::sort(nums);

    auto gcd = nums[1] - nums[0];
    rng::for_each(rng::views::iota(i32(2), static_cast<i32>(n)),
                  [&](auto const i) { gcd = std::gcd(gcd, nums[i] - nums[i - 1]); });

    auto ans = std::vector<i32>();
    for (auto i = i32(2); i * i <= gcd; ++i)
    {
        if (gcd % i == 0)
        {
            ans.emplace_back(i);
            if (gcd / i != i)
            {
                ans.emplace_back(gcd / i);
            }
        }
    }
    rng::sort(ans);
    ans.emplace_back(gcd);
    rng::copy(ans, std::ostream_iterator<i32>(std::cout, " "));
    std::endl(std::cout);
    return EXIT_SUCCESS;
}
