// https://www.acmicpc.net/problem/6380

#include <algorithm>
#include <deque>
#include <iostream>
#include <iterator>
#include <ranges>
#include <string>
#include <string_view>
#include <unordered_set>

#define _R ::std::ranges::

template <char CH, char... OTHERS>
bool OneOf(char const ch) noexcept
{
    if (ch == CH)
    {
        return true;
    }
    if constexpr (0 < sizeof...(OTHERS))
    {
        return OneOf<OTHERS...>(ch);
    }
    return false;
}

class [[nodiscard]] Token
{
    std::istream& _is;
    std::deque<char> _tokens{};

  public:
    Token(std::istream& is) : _is(is)
    {
    }

    [[nodiscard]] bool Empty() const noexcept
    {
        return _tokens.empty();
    }

    char Next()
    {
        if (!_tokens.empty())
        {
            _tokens.pop_front();
        }
        if (_tokens.empty())
        {
            _tokens.emplace_back(static_cast<char>(_is.get()));
        }
        return _tokens.front();
    }

    template <char... DELIMS>
    char NextUntil()
    {
        auto& it = *this;
        while (!OneOf<DELIMS...>(*it))
        {
            Next();
        }
        return *it;
    }

    template <char... CHAR_SET>
    char NextWhile()
    {
        auto& it = *this;
        while (OneOf<CHAR_SET...>(*it))
        {
            Next();
        }
        return *it;
    }

    [[nodiscard]] char operator[](std::size_t const index)
    {
        while (_tokens.size() <= index)
        {
            _tokens.emplace_back(static_cast<char>(_is.get()));
        }
        return _tokens[index];
    }

    [[nodiscard]] char operator*()
    {
        if (_tokens.empty())
        {
            Next();
        }
        return _tokens.front();
    }

    Token& operator++()
    {
        Next();
        return *this;
    }
};

enum class TagType
{
    NONE,
    PROCESSING_INSTRUCTION,
    ELEMENT,
};

class XMLParser
{
    Token _token;
    std::string _s{};
    std::deque<std::string> _scope{};
    std::unordered_set<std::string> _attributes{};
    TagType _tag_type{};

  public:
    XMLParser(std::istream& is) : _token(is)
    {
    }

    bool ParseName()
    {
        _s.clear();
        _token.NextWhile<' ', '\n'>();
        if (std::isalpha(*_token) == 0 && std::isdigit(*_token) == 0 && *_token != '-')
        {
            return false;
        }
        while (std::isalpha(*_token) != 0 || std::isdigit(*_token) != 0 || *_token == '-')
        {
            _s.push_back(*_token);
            _token.Next();
        }
        return *_token != '<';
    }

    void ParseTagValue()
    {
        _token.NextUntil<'<'>();
    }

    bool ParseAttributes()
    {
        _attributes.clear();
        do
        {
            _token.NextWhile<' ', '\n'>();
            if (*_token == '/' || *_token == '>')
            {
                return true;
            }
            if (!ParseName() || _token.NextWhile<' ', '\n'>() != '=')
            {
                return false;
            }
            _token.Next();
            if (_token.NextWhile<' ', '\n'>() != '"')
            {
                return false;
            }
            _token.Next();
            _token.NextUntil<'"'>();
            _token.Next();

            auto const res = _attributes.emplace(_s);
            if (!res.second)
            {
                return false;
            }
        } while (true);
    }

    std::pair<bool, bool> ParseStartTag()
    {
        _token.Next();
        if (!ParseName())
        {
            return {false, false};
        }

        if (_R find(_scope, _s) != _R cend(_scope))
        {
            return {false, false};
        }
        _scope.emplace_back(_s);
        if (!ParseAttributes())
        {
            return {false, false};
        }

        if (*_token == '/')
        {
            if (_token.Next() != '>')
            {
                return {false, false};
            }
            _token.Next();
            return {true, true};
        }
        _token.Next();
        return {true, false};
    }

    bool ParseEndTag()
    {
        _token.Next();
        _token.Next();
        if (!ParseName())
        {
            return false;
        }

        if (_token.NextWhile<' ', '\n'>() != '>')
        {
            return false;
        }

        if (_s != _scope.back())
        {
            return false;
        }
        _scope.pop_back();
        _token.Next();
        return *_token;
    }

    bool ParseElementTag()
    {
        if (auto const [ok, closed] = ParseStartTag(); !ok || closed)
        {
            return closed;
        }
        for (;;)
        {
            if (_token.NextWhile<' ', '\n'>() != '<')
            {
                ParseTagValue();
            }
            else
            {
                if (_token[1] == '/' || _token[1] == '?')
                {
                    break;
                }
                if (!ParseElementTag())
                {
                    return false;
                }
            }
        }
        if (_token[0] != '<' || _token[1] != '/')
        {
            return false;
        }
        return ParseEndTag();
    }

    void ParseProcessingInstruction()
    {
        _token.Next(); // <
        _token.Next(); // ?
        _token.NextWhile<' ', '\n'>();
        ParseName();
        _token.NextUntil<'\n'>();
    }

  public:
    TagType GetTag() const noexcept
    {
        return _tag_type;
    }

    bool Eof() const noexcept
    {
        return GetTag() == TagType::PROCESSING_INSTRUCTION && _s == "end";
    }

    bool NextTag()
    {
        _scope.clear();
        if (_token.NextWhile<' ', '\n'>() != '<')
        {
            _tag_type = TagType::NONE;
            return false;
        }
        if (_token[1] == '?')
        {
            ParseProcessingInstruction();
            _tag_type = TagType::PROCESSING_INSTRUCTION;
            return true;
        }
        if (!ParseElementTag())
        {
            return false;
        }
        _tag_type = TagType::ELEMENT;
        return true;
    }

    void Recover()
    {
        for (;;)
        {
            while (_token[0] != '<' || _token[1] != '?')
            {
                _token.Next();
            }
            if (NextTag() && GetTag() == TagType::PROCESSING_INSTRUCTION)
            {
                break;
            }
            _token.Next();
        }
    }
};

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    for (auto parser = XMLParser(std::cin);;)
    {
        if (parser.Eof())
        {
            std::exit(EXIT_SUCCESS);
        }
        if (parser.NextTag())
        {
            if (parser.GetTag() == TagType::PROCESSING_INSTRUCTION)
            {
                continue;
            }
            if (parser.NextTag())
            {
                if (parser.GetTag() == TagType::PROCESSING_INSTRUCTION)
                {
                    std::cout << "well-formed\n";
                    continue;
                }
            }
        }

        parser.Recover();
        std::cout << "non well-formed\n";
    }
    return EXIT_SUCCESS;
}
