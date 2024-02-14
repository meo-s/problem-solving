// https://www.acmicpc.net/problem/6603

#include <algorithm>
#include <array>
#include <cstdint>
#include <cstdlib>
#include <deque>
#include <iostream>
#include <iterator>
#include <ranges>
#include <stack>

namespace rng = std::ranges;

using i32 = std::int32_t;
using BallMachine = std::array<i32, 12>;

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

void Enumerate(BallMachine const& balls, i32 const k, i32 const offset = i32()) noexcept
{
    class UnsafeStack : public std::stack<i32, std::deque<i32>>
    {
      public:
        std::deque<i32> const& GetContainer() const noexcept
        {
            return c;
        }
    };

    constexpr static auto MAX_DEPTH = static_cast<UnsafeStack::size_type>(6);
    thread_local static auto indices = UnsafeStack();
    if (indices.size() == MAX_DEPTH)
    {
        rng::copy(indices.GetContainer(), std::ostream_iterator<i32>(std::cout, " "));
        std::cout << '\n';
    }
    else
    {
        rng::for_each(
            rng::views::iota(offset, k - static_cast<i32>(MAX_DEPTH - indices.size() - 1)),
            [&](auto const i) {
                indices.emplace(balls[i]);
                Enumerate(balls, k, i + 1);
                indices.pop();
            });
    }
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto balls = BallMachine();
    for (auto test_case = i32();; ++test_case)
    {
        auto const k = Read<i32>(std::cin);
        if (k == i32())
        {
            break;
        }

        if (i32() < test_case)
        {
            std::cout << '\n';
        }
        std::copy_n(std::istream_iterator<i32>(std::cin), k, rng::begin(balls));
        Enumerate(balls, k);
    }

    return EXIT_SUCCESS;
}
