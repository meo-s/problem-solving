// https://www.acmicpc.net/problem/12865

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <tuple>
#include <vector>

using namespace std;
using i32 = int;

int main(int argc, char const* argv[]) {
    auto in = istream_iterator<i32>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto N = *in;
    auto K = *++in;
    auto items = vector<tuple<i32, i32>>(0);
    items.reserve(N);
    for (i32 i = 0; i < N; ++i) {
        auto const w = *++in;
        auto const v = *++in;
        if (w <= K) {
            items.emplace_back(w, v);
        }
    }

    auto dp = vector<i32>(K + 1);
    auto maxv = 0;
    for (auto[w, v] : items) {
        for (i32 i = K; 0 <= i; --i) {
            if (0 <= i - w) {
                if ((i - w == 0 || 0 < dp[i - w]) && dp[i] < dp[i - w] + v) {
                    dp[i] = dp[i - w] + v;
                    maxv = max(maxv, dp[i]);
                }
            }
        }
    }

    cout << maxv << endl;
    return 0;
}
