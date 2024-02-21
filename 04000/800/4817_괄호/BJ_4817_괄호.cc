// https://www.acmicpc.net/problem/4817

#include <concepts>
#include <cstdint>
#include <iostream>
#include <istream>
#include <iterator>
#include <memory>
#include <ranges>
#include <string>
#include <variant>

#define _R ::std::ranges::

struct [[nodiscard]] Variable
{
    char ch{};
};

struct [[nodiscard]] Operator
{
    char ch{};
    bool emit{false};

    int Prior() const noexcept
    {
        return ch == '+' ? 0 : 1;
    }
};

class [[nodiscard]] Node
{
    std::unique_ptr<Node> _lhs{};
    std::unique_ptr<Node> _rhs{};
    std::variant<Variable, Operator> _extra{};

  public:
    template <typename T>
    Node(std::unique_ptr<Node> lhs, std::unique_ptr<Node> rhs, T const extra) noexcept
        : _lhs(std::move(lhs)), _rhs(std::move(rhs)), _extra(extra)
    {
    }

    bool IsVar() const noexcept
    {
        return _lhs == nullptr && _rhs == nullptr;
    }

    Operator const& GetOp() const
    {
        return std::get<Operator>(_extra);
    }

    void Serialize(std::output_iterator<char> auto dst) const
    {
        if (IsVar())
        {
            *dst = std::get<Variable>(_extra).ch;
            ++dst;
            return;
        }

        auto const& op = GetOp();

        if (_lhs->IsVar() || (op.Prior() <= _lhs->GetOp().Prior()))
        {
            _lhs->Serialize(dst);
        }
        else
        {
            *dst = '(';
            _lhs->Serialize(++dst);
            *dst = ')';
            ++dst;
        }

        if (!op.emit)
        {
            *dst = op.ch;
            ++dst;
        }

        if (_rhs->IsVar() || op.Prior() <= _rhs->GetOp().Prior())
        {
            _rhs->Serialize(dst);
        }
        else
        {
            *dst = '(';
            _rhs->Serialize(++dst);
            *dst = ')';
            ++dst;
        }
    }

    template <std::input_iterator Iter>
    friend class ExpressionParser;
};

template <std::input_iterator Iter>
class [[nodiscard]] ExpressionParser
{
    Iter _it;
    Iter const _end;

    ExpressionParser(Iter const beg, Iter const end) : _it(beg), _end(end)
    {
    }

    std::unique_ptr<Node> Factor()
    {
        auto node = std::unique_ptr<Node>();
        if (*_it == '(')
        {
            _R advance(_it, 1);
            node = Expression();
            _R advance(_it, 1); // )
        }
        else
        {
            node = std::make_unique<Node>(nullptr, nullptr, Variable{*_it});
            _R advance(_it, 1);
        }
        return node;
    }

    std::unique_ptr<Node> Term()
    {
        auto node = Factor();
        while (_it != _end)
        {
            switch (*_it)
            {
            case '*':
                _R advance(_it, 1);
                node = std::make_unique<Node>(std::move(node), Factor(), Operator{'*'});
                continue;
            case '(':
                _R advance(_it, 1);
                node = std::make_unique<Node>(std::move(node), Expression(), Operator{'*', true});
                _R advance(_it, 1);
                continue;
            }
            if (std::isalpha(*_it) != 0)
            {
                auto const var = Variable{*_it};
                _R advance(_it, 1);
                node = std::make_unique<Node>(std::move(node),
                                              std::make_unique<Node>(nullptr, nullptr, var),
                                              Operator{'*', true});
            }
            else
            {
                break;
            }
        }
        return node;
    }

    std::unique_ptr<Node> Expression()
    {
        auto node = Term();
        while (_it != _end && *_it == '+')
        {
            _R advance(_it, 1);
            node = std::make_unique<Node>(std::move(node), Term(), Operator{'+'});
        }
        return node;
    }

    template <typename R>
    friend std::unique_ptr<Node> Parse(R const& rng);
};

template <typename R>
std::unique_ptr<Node> Parse(R const& rng)
{
    return ExpressionParser(_R cbegin(rng), _R cend(rng)).Expression();
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

    for (;;)
    {
        if (auto const exp = Read<std::string>(std::cin); !std::cin.eof())
        {
            Parse(exp)->Serialize(std::ostream_iterator<char>(std::cout));
            std::cout << '\n';
        }
        else
        {
            return EXIT_SUCCESS;
        }
    }
}
