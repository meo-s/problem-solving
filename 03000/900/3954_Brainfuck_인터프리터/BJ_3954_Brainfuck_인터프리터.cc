// https://www.acmicpc.net/problem/3954

#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <stack>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <utility>
#include <vector>

using namespace std;

using sz = size_t;
using u8 = uint8_t;
using i32 = int32_t;
using u32 = uint32_t;

class InputBuffer {
  public:
    InputBuffer(sz const I, string const& ibuf) : _I{I}, _ibuf{ibuf}, _consumed{0} {}
    u8 consume() { return _consumed < _I ? _ibuf[_consumed++] : 0xff; }

  private:
    sz _I;
    string _ibuf;
    sz _consumed;
};

class BrainfuckInterpreter {
  public:
    struct LoopInfo {
        sz entry_ticks;
        sz cp;
        sz continues;
    };

    struct ExecutionContext {
        ExecutionContext(sz const M, sz const I, string const& code_, string const& ibuf_)
            : memory(M), mp{0}, code{code_}, cp{0}, ibuf{I, ibuf_}, jumps{}, loop_stacks{}, ticks{0} {
            fill(memory.begin(), memory.end(), 0);
            init();
        }

        void init() {
            jumps.clear();
            auto ifs = stack<sz>{};
            for (sz i = 0; i < code.size(); ++i) {
                switch (code[i]) {
                case '[':
                    ifs.push(i);
                    break;
                case ']':
                    jumps.emplace(i, ifs.top());
                    jumps.emplace(ifs.top(), i);
                    ifs.pop();
                    break;
                }
            }
        }

        bool is_terminated() { return cp == code.length(); }

        vector<u8> memory;
        sz mp;

        string code;
        sz cp;

        InputBuffer ibuf;

        unordered_map<sz, sz> jumps;
        stack<LoopInfo> loop_stacks;

        sz ticks;
    };

    BrainfuckInterpreter() = default;

    static void tick(ExecutionContext& ctx) {
        ++ctx.ticks;

        switch (ctx.code[ctx.cp]) {
        case '+':
            ++ctx.memory[ctx.mp];
            break;
        case '-':
            --ctx.memory[ctx.mp];
            break;
        case '<':
            ctx.mp = min(ctx.mp - 1, ctx.memory.size() - 1);
            break;
        case '>':
            ctx.mp = (ctx.mp + 1) % ctx.memory.size();
            break;
        case ',':
            ctx.memory[ctx.mp] = ctx.ibuf.consume();
            break;
        case '[':
            if (ctx.memory[ctx.mp] != 0) {
                ctx.loop_stacks.push({ctx.ticks, ctx.cp, 0});
            } else {
                ctx.cp = ctx.jumps.at(ctx.cp);
            }
            break;
        case ']':
            if (ctx.memory[ctx.mp] != 0) {
                ctx.cp = ctx.loop_stacks.top().cp;
                ++ctx.loop_stacks.top().continues;
            } else {
                ctx.loop_stacks.pop();
            }
            break;
        }

        ++ctx.cp;
    }

    static bool run(ExecutionContext& ctx, sz const max_iterations = MAX_TICKS) {
        for (sz i = 0; i < max_iterations && !ctx.is_terminated(); ++i) {
            tick(ctx);
        }
        return ctx.is_terminated();
    }

    static pair<sz, sz> find_inf_loop(ExecutionContext& ctx) {
        auto loops = unordered_set<sz>{};

        auto loops_stacks = stack{ctx.loop_stacks};
        while (0 < loops_stacks.size()) {
            if (0 < loops_stacks.top().continues) {
                loops.emplace(loops_stacks.top().cp);
            }
            loops_stacks.pop();
        }

        for (sz i = 0; i < MAX_TICKS; ++i) {
            auto const n_loops = ctx.loop_stacks.size();
            auto const top_loop = ctx.loop_stacks.top();

            tick(ctx);
            if (ctx.loop_stacks.size() < n_loops) {
                loops.erase(top_loop.cp);
            }
        }

        sz inf_loop = 0;
        for (auto const cp : loops) {
            inf_loop = max(inf_loop, cp);
        }

        return {inf_loop, ctx.jumps.at(inf_loop)};
    }

  private:
    static sz const MAX_TICKS = 50'000'000;
};

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto it = istream_iterator<sz>{cin};

    for (auto t = *it; 0 < t; --t) {
        auto const M = *++it;
        (void)*++it;
        auto const I = *++it;

        while (cin.get() != '\n')
            ;

        auto code = string{};
        getline(cin, code);

        auto ibuf = string{};
        getline(cin, ibuf);

        auto ctx = BrainfuckInterpreter::ExecutionContext{M, I, code, ibuf};
        if (BrainfuckInterpreter::run(ctx)) {
            cout << "Terminates\n";
        } else {
            auto const inf_loop = BrainfuckInterpreter::find_inf_loop(ctx);
            cout << "Loops " << inf_loop.first << ' ' << inf_loop.second << '\n';
        }

        cout.flush();
    }

    return 0;
}
