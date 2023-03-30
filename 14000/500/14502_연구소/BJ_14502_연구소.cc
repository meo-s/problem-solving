// https://www.acmicpc.net/problem/14502

#include <algorithm>
#include <cstdint>
#include <deque>
#include <iostream>
#include <iterator>
#include <tuple>
#include <vector>

using namespace std;
using i32 = int32_t;

constexpr i32 dy[] = {1, -1, 0, 0};
constexpr i32 dx[] = {0, 0, 1, -1};


i32 bfs(i32 const H, i32 const W, vector<vector<i32>> const& m) {
    static auto visited = vector<vector<bool>>(0);
    if (visited.size() == 0) {
        visited.resize(H);
        for (i32 y = 0; y < H; ++y) {
            visited[y].resize(W);
        }
    }
    for (i32 y = 0; y < H; ++y) {
        fill(begin(visited[y]), end(visited[y]), false);
    }

    auto s = 0;
    auto pendings = deque<tuple<i32, i32>>{};
    for (i32 y = 0; y < H; ++y) {
        for (i32 x = 0; x < W; ++x) {
            switch (m[y][x]) {
            case 0: ++s; break;
            case 2: pendings.emplace_back(y, x); visited[y][x] = true;
            }
        }
    }
    while (0 < pendings.size()) {
        auto const[y, x] = pendings.front();
        pendings.pop_front();

        for (i32 i = 0; i < 4; ++i) {
            auto const ny = y + dy[i];
            auto const nx = x + dx[i];
            if (0 <= ny && ny < H && 0 <= nx && nx < W) {
                if (m[ny][nx] == 0 && !visited[ny][nx]) {
                    visited[ny][nx] = true;
                    pendings.emplace_back(ny, nx);
                    --s;
                }
            }
        }
    }

    return s;
}

i32 dfs(i32 H, i32 W, vector<vector<i32>>& m, i32 offset = 0, i32 depth = 0) {
    if (depth == 3) {
        return bfs(H, W, m);
    }

    i32 maxs = 0;
    for (i32 i = offset; i < H*W - (2 - depth); ++i) {
        if (m[i / W][i % W] == 0) {
            m[i / W][i % W] = 1;
            maxs = max(maxs, dfs(H, W, m, i + 1, depth + 1));
            m[i / W][i % W] = 0;
        }
    }

    return maxs;
}

int main(int argc, char const* argv[]) {
    auto in = istream_iterator<i32>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto H = *in;
    auto W = *++in;

    auto m = vector<vector<i32>>(H);
    for (auto& mr : m) {
        mr.reserve(W);
        for (i32 i = 0; i < W; ++i) {
            mr.emplace_back(*++in);
        }
    }

    cout << dfs(H, W, m) << endl;
    return 0;
}
