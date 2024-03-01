// https://www.acmicpc.net/problem/3568

#include <algorithm>
#include <iostream>
#include <ranges>
#include <stack>
#include <string>
#include <string_view>

#define _R ::std::ranges::

template <char VALUE, char... OTHERS>
constexpr bool OneOf(char const ch)
{
    if (ch == VALUE)
    {
        return true;
    }
    if constexpr (0 < sizeof...(OTHERS))
    {
        return OneOf<OTHERS...>(ch);
    }
    else
    {
        return false;
    }
}

constexpr bool IsAlpha(char const ch) noexcept
{
    return ('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z');
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto decl = std::string{};
    std::getline(std::cin, decl);

    auto const common_type_end = _R find(_R cbegin(decl), _R cend(decl), ' ');
    auto const common_type = std::string_view(_R cbegin(decl), common_type_end);
    for (auto it = common_type_end; *it != ';';)
    {
        it = _R find_if(it, _R cend(decl), IsAlpha);
        auto const var_name_end = _R find_if_not(it, _R cend(decl), IsAlpha);
        auto const var_decl_end = _R find_if(var_name_end, _R cend(decl), OneOf<',', ';'>);
        auto extra_type = std::stack<char>{};
        for (auto it = var_name_end; it != var_decl_end; ++it)
        {
            if (*it != ' ' && *it != ']')
            {
                extra_type.push(*it);
            }
        }

        std::cout << common_type;
        while (!extra_type.empty())
        {
            std::cout << extra_type.top();
            if (extra_type.top() == '[')
            {
                std::cout << ']';
            }
            extra_type.pop();
        }
        std::cout << ' ' << std::string_view{it, var_name_end} << ";\n";

        it = var_decl_end;
    }

    return EXIT_SUCCESS;
}
