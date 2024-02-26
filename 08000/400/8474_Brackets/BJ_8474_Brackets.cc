// https://www.acmicpc.net/problem/8474

#include <algorithm>
#include <concepts>
#include <cstdint>
#include <iostream>
#include <memory>
#include <ranges>
#include <string>
#include <type_traits>
#include <variant>

#define _R ::std::ranges::

using i32 = std::int32_t;

template <typename... Callables>
struct Overloaded : public Callables...
{
    using Callables::operator()...;
};

template <typename... Callables>
Overloaded(Callables...) -> Overloaded<Callables...>;

template <char VALUE, char... VALUES>
bool OneOf(char const ch)
{
    if (ch == VALUE)
    {
        return ch;
    }
    if constexpr (0 < sizeof...(VALUES))
    {
        return OneOf<VALUES...>(ch);
    }
    else
    {
        return false;
    }
}

struct Leaf;
struct Branch;
using Node = std::variant<std::nullptr_t, Leaf, Branch>;

struct Leaf
{
};

struct Branch
{
    std::unique_ptr<Node> lhs;
    std::unique_ptr<Node> rhs;
    char op;

    i32 Prior() const noexcept
    {
        switch (op)
        {
        case '+':
        case '-':
            return static_cast<i32>(0);
        case '*':
        case '/':
            return static_cast<i32>(1);
        default:
            return static_cast<i32>(2);
        }
    }
};

template <std::input_iterator Iter>
class ExpressionParser
{
    Iter _it;
    Iter const _end;

    std::unique_ptr<Node> Factor()
    {
        auto node = std::unique_ptr<Node>{};
        if (*_it == '(')
        {
            _R advance(_it, 1);
            node = Expression();
        }
        else
        {
            node = std::make_unique<Node>(Leaf{});
        }
        _R advance(_it, 1);
        return node;
    }

    std::unique_ptr<Node> Term()
    {
        auto node = Factor();
        while (_it != _end && OneOf<'*', '/'>(*_it))
        {
            char const op = *_it;
            _R advance(_it, 1);
            node = std::make_unique<Node>(Branch{std::move(node), Factor(), op});
        }
        return node;
    }

    std::unique_ptr<Node> Expression()
    {
        auto node = Term();
        while (_it != _end && OneOf<'+', '-'>(*_it))
        {
            char const op = *_it;
            _R advance(_it, 1);
            node = std::make_unique<Node>(Branch{std::move(node), Term(), op, false});
        }
        return node;
    }

  public:
    ExpressionParser(Iter const beg, Iter const end) : _it(beg), _end(end)
    {
    }

    std::unique_ptr<Node> Parse()
    {
        return Expression();
    }
};

i32 Prior(Node const& node)
{
    auto const visitor = Overloaded{[]([[maybe_unused]] std::nullptr_t) -> i32 { throw; },
                                    []([[maybe_unused]] Leaf) { return static_cast<i32>(2); },
                                    [](Branch const& node) { return node.Prior(); }};
    return std::visit(visitor, node);
}

template <typename Iter>
    requires(std::output_iterator<std::remove_cvref_t<Iter>, char>)
void Serialize(Node const& node, Iter&& out)
{
    auto visitor =
        Overloaded{[]([[maybe_unused]] std::nullptr_t) mutable {},
                   [&out]([[maybe_unused]] Leaf) mutable {
                       *out = 'x';
                       _R advance(out, 1);
                   },
                   [&out](Branch const& node) mutable {
                       auto between_brackets = [&](Node const& target) {
                           *out = '(';
                           _R advance(out, 1);
                           Serialize(target, out);
                           *out = ')';
                           _R advance(out, 1);
                       };

                       Prior(*node.lhs) < node.Prior() ? between_brackets(*node.lhs)
                                                       : Serialize(*node.lhs, out);

                       *out = node.op;
                       _R advance(out, 1);

                       if (OneOf<'-', '/'>(node.op) && std::holds_alternative<Branch>(*node.rhs))
                       {
                           auto const& rhs = std::get<Branch>(*node.rhs);
                           if (node.op == '-')
                           {
                               OneOf<'+', '-'>(rhs.op) ? between_brackets(*node.rhs)
                                                       : Serialize(*node.rhs, out);
                           }
                           else // if(node.op == '/')
                           {
                               between_brackets(*node.rhs);
                           }
                       }
                       else
                       {
                           Prior(*node.rhs) < node.Prior() ? between_brackets(*node.rhs)
                                                           : Serialize(*node.rhs, out);
                       }
                   }};
    std::visit(visitor, node);
}

template <typename Iter>
ExpressionParser(Iter&&, Iter&&) -> ExpressionParser<Iter>;

template <typename T>
auto Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    _R for_each(_R views::iota(i32(0), Read<i32>(std::cin)), []([[maybe_unused]] auto) {
        auto const e = Read<std::string>(std::cin);
        Serialize(*ExpressionParser(_R cbegin(e), _R cend(e)).Parse(),
                  std::ostream_iterator<char>(std::cout));
        std::cout << '\n';
    });

    return EXIT_SUCCESS;
}
