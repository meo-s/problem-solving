// https://www.acmicpc.net/problem/1533

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <vector>

using namespace std;
using sz = size_t;
using u64 = uint64_t;
using Mat = vector<u64>;
constexpr sz L = 5;
constexpr u64 M = 1'000'003;

void matmul(sz const N, Mat const& lhs, Mat const& rhs, Mat& out) {
    for (sz r = 0; r < N; ++r) {
        for (sz c = 0; c < N; ++c) {
            out[r * N + c] = 0;
            for (sz i = 0; i < N; ++i) {
                out[r * N + c] = (out[r * N + c] + lhs[r * N + i] * rhs[i * N + c]) % M;
            }
        }
    }
}

void matpow(sz const N, Mat const& m, u64 const k, Mat& out) {
    switch (k) {
    case 1:
        copy(m.begin(), m.end(), out.begin());
        break;
    case 2:
        matmul(N, m, m, out);
        break;
    default:
        auto tmp = Mat(m.size());
        matpow(N, m, k / 2, tmp);
        matpow(N, tmp, 2, out);
        if (k % 2 == 1) {
            copy(out.begin(), out.end(), tmp.begin());
            matmul(N, tmp, m, out);
        }
    }
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    sz N, S, G;
    cin >> N >> S >> G;
    u64 T;
    cin >> T;
    cin.get(); // LF

    auto edges = Mat(L * N * L * N);
    for (sz u = 0; u < N; ++u) {
        for (sz i = 0; i < L - 1; ++i) {
            edges[(u * L + i) * (L * N) + (u * L) + (i + 1)] = 1;
        }
        for (sz v = 0; v < N; ++v) {
            auto w = cin.get() - '0';
            if (w != 0) edges[(u * L + (w - 1)) * (L * N) + v * L] = 1;
        }
        cin.get(); // LF
    }

    auto sol = Mat(edges.size());
    matpow(L * N, edges, T, sol);
    cout << sol[(S - 1) * L * (L * N) + (G - 1) * L] << endl;

    return 0;
}
