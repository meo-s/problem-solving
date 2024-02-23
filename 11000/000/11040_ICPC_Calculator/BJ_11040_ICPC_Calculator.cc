// https://www.acmicpc.net/problem/11040

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <stack>
#include <string>

using i32 = std::int32_t;
using i64 = std::int64_t;

#define _R ::std::ranges::

i64 Calc(char const op, i64 const lhs, i64 const rhs) noexcept
{
    switch (op)
    {
    case '+':
        return lhs + rhs;
    case '*':
        return lhs * rhs;
    default:
        std::exit(EXIT_FAILURE);
    }
}

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

    auto operands = std::stack<std::pair<i32, i64>>();
    auto operators = std::stack<std::pair<i32, char>>();

    auto eval_once = [&]() {
        auto val = operands.top().second;
        operands.pop();
        while (!operands.empty() && operands.top().first == operators.top().first + 1)
        {
            val = Calc(operators.top().second, val, operands.top().second);
            operands.pop();
        }
        operands.emplace(operators.top().first, val);
        operators.pop();
    };

    for (;;)
    {
        auto n = Read<i32>(std::cin);
        if (n == 0)
        {
            return EXIT_SUCCESS;
        }

        for (; 0 < n; --n)
        {
            auto const line = Read<std::string>(std::cin);
            auto const it = _R find_if(line, [](auto const ch) { return ch != '.'; });
            auto const depth = _R distance(_R cbegin(line), it);
            while (!operands.empty() && depth < operands.top().first)
            {
                eval_once();
            }

            if (std::isdigit(*it) != 0)
            {
                operands.emplace(depth, *it - '0');
            }
            else
            {
                operators.emplace(depth, *it);
            }
        }
        while (!operators.empty())
        {
            eval_once();
        }
        std::cout << operands.top().second << '\n';
        operands.pop();
    }
}
