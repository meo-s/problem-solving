// https://www.acmicpc.net/problem/1010

#include <iostream>
#include <iterator>
#include <vector>

using namespace std;
using sz = size_t;

class CobinationCache {
private:
    constexpr static int NOT_CACHED = -1;

public:
    CobinationCache(sz const n, sz const r) : dp{n + 1} {
        for (auto& dpr : dp) {
            dpr.reserve(r + 1);
            for (int i = 0; i <= r; ++i) {
                dpr.emplace_back(-1);
            }
        }
    }

    int query(int n, int r) {
        if (dp[n][r] == NOT_CACHED) {
            dp[n][r] = n != r && r != 0 ? query(n - 1, r) + query(n - 1, r - 1) : 1;
        }
        return dp[n][r];
    }

private:
    vector<vector<int>> dp;
};

int main(int argc, char* argv[]) {
    auto in = istream_iterator<int>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto nr = CobinationCache{30, 30};
    auto t = *in;
    while (0 <= --t) {
        auto const N = *++in;
        auto const M = *++in;
        cout << nr.query(M, N) << '\n';
    }

    cout.flush();
    return 0;
}
