// https://www.acmicpc.net/problem/1981

#include <iostream>
#include <iterator>
#include <vector>
#include <queue>
#include <utility>
#include <deque>
#include <cmath>

using namespace std;

using sz = size_t;
using i32 = int32_t;

constexpr int dy[] = {1, -1, 0, 0};
constexpr int dx[] = {0, 0, 1, -1};

i32 simulate(vector<vector<int>> const& m, vector<vector<int>>& v, i32 const ub, i32 const threshold) {
    if (ub <= m[0][0]) {
        return -1;
    }

    for (auto& vr : v) {
        fill(vr.begin(), vr.end(), -1);
    }

    v[0][0] = m[0][0];

    auto pendings = deque<pair<int, int>>{};
    pendings.emplace_back(0, 0);
    while (0 < pendings.size()) {
        auto const[y, x] = pendings.front();
        pendings.pop_front();

        for (i32 i = 0; i < 4; ++i) {
            i32 const ny = y + dy[i];
            i32 const nx = x + dx[i];
            if (0 <= ny && ny < m.size() && 0 <= nx && nx < m.size()) {
                if (m[ny][nx] < ub) {
                    if (threshold <= (ub - 1) - m[ny][nx]) {
                        continue;
                    }
                    if (v[ny][nx] < min(v[y][x], m[ny][nx])) {
                        v[ny][nx] = min(v[y][x], m[ny][nx]);
                        pendings.emplace_back(ny, nx);
                    }
                }
            }
        }
    }

    return v.back().back();
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<int>{cin};

    auto const N = *in;
    auto m = vector<vector<int>>(N);
    auto v = vector<vector<int>>(N);
    for (i32 y = 0; y < N; ++y) {
        m[y].reserve(N);
        v[y].resize(N);
        for (i32 i = 0; i < N; ++i) {
            m[y].emplace_back(*++in);
        }
    }

    auto min_diff = 201;
    auto const hi = max(m[0][0], m[N - 1][N - 1]);
    for (i32 ub = 201; hi < ub; --ub) {
        auto const minv = simulate(m, v, ub, min_diff);
        if (minv == -1) {
            continue;
        }

        min_diff = min(min_diff, (ub - 1) - minv);
    }

    cout << min_diff << endl;
    return 0;
}
