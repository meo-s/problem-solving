// https://www.acmicpc.net/problem/24511

#include <algorithm>
#include <deque>
#include <iostream>
#include <iterator>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;

int main(int const argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>{cin};
    auto is_stack = vector<bool>(sz(*in));
    copy_n(++in, is_stack.size(), is_stack.begin());
    auto state = deque<i32>();
    for (sz i = 0; i < is_stack.size(); i++) {
        auto const v = *++in;
        if (!is_stack[i]) {
            state.emplace_back(v);
        }
    }

    for (i32 i = *++in; 0 < i; --i) {
        state.emplace_front(*++in);
        cout << state.back() << ' ';
        state.pop_back();
    }
    cout << endl;

    return 0;
}
