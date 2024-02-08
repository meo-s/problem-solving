// https://www.acmicpc.net/problem/16637

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <string>

using sz = std::size_t;
using i64 = std::int64_t;

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

[[nodiscard]] constexpr i64 Evaluate(char const op, i64 const lhs, i64 const rhs)
{
    switch (op)
    {
    case '+':
        return lhs + rhs;
    case '-':
        return lhs - rhs;
    case '*':
        return lhs * rhs;
    default:
        throw;
    }
}

i64 Evaluate(std::string const& exp)
{
    auto const EvaluateImpl = [&](auto&& callee, sz const offset, i64 const lhs) -> i64 {
        if (offset == exp.length())
        {
            return lhs;
        }

        auto const tmp = Evaluate(exp[offset], lhs, static_cast<i64>(exp[offset + 1] - '0'));
        auto value = callee(callee, offset + 2, tmp);
        if (offset + 3 < exp.length())
        {
            auto const sub_lhs = static_cast<i64>(exp[offset + 1] - '0');
            auto const sub_rhs = static_cast<i64>(exp[offset + 3] - '0');
            auto const rhs = Evaluate(exp[offset + 2], sub_lhs, sub_rhs);
            auto const tmp = Evaluate(exp[offset], lhs, rhs);
            value = std::max(value, callee(callee, offset + 4, tmp));
        }
        return value;
    };
    return EvaluateImpl(EvaluateImpl, static_cast<sz>(1), static_cast<i64>(exp[0] - '0'));
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    (void)Read<sz>(std::cin);
    std::cout << Evaluate(Read<std::string>(std::cin)) << std::endl;
    return EXIT_SUCCESS;
}
