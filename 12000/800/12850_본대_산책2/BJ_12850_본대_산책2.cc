// https://www.acmicpc.net/problem/12850

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>

using sz = std::size_t;
using i64 = std::int64_t;
template <typename T, sz M, sz N> using Matrix2D = std::array<std::array<T, M>, N>;

constexpr auto MODULAR = i64(1'000'000'007);

template <typename T, sz M, sz N, sz O>
void matmul(Matrix2D<T, M, O> const& m1, Matrix2D<T, O, N> const& m2, Matrix2D<T, M, N>& m_out) {
    for (auto m = sz(); m < M; ++m) {
        for (auto n = sz(); n < N; ++n) {
            m_out[m][n] = T();
            for (auto o = sz(); o < O; ++o) {
                m_out[m][n] = (m_out[m][n] + m1[m][o] * m2[o][n]) % MODULAR;
            }
        }
    }
}

template <typename T, sz M, sz N>
void matmod(Matrix2D<T, M, N> const& m, T const& v, Matrix2D<T, M, N>& m_out) {
    for (auto i = sz(); i < M; ++i) {
        for (auto j = sz(); j < N; ++j) {
            m_out[i][j] = m[i][j] % v;
        }
    }
}

template <typename T, sz M>
void matpow(Matrix2D<T, M, M> const& m, i64 const& n, Matrix2D<T, M, M>& m_out) {
    if (n == 0) {
        std::fill_n(reinterpret_cast<T*>(m_out.data()), M * M, T());
        for (auto i = sz(); i < M; ++i) {
            m_out[i][i] = 1;
        }
    } else if (n == 1) {
        std::copy_n(reinterpret_cast<T const*>(m.data()), M * M, reinterpret_cast<T*>(m_out.data()));
    } else if (n == 2) {
        matmul(m, m, m_out);
    } else {
        auto t = Matrix2D<T, M, M>();
        matpow(m, n / 2, t);
        if (n % 2 == 0) {
            matmul(t, t, m_out);
        } else {
            auto s = Matrix2D<T, M, M>();
            matmul(t, t, s);
            matmul(s, m, m_out);
        }
    }
}

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    // 0 정보과학관
    // 1 전산관
    // 2 미래관
    // 3 신양관
    // 4 한경직기념관
    // 5 진리관
    // 6 형남공학관
    // 7 학생회관
    auto ans = Matrix2D<i64, 8, 8>();
    auto g = decltype(ans){{
        {0, 1, 1, 0, 0, 0, 0, 0},
        {1, 0, 1, 1, 0, 0, 0, 0},
        {1, 1, 0, 1, 1, 0, 0, 0},
        {0, 1, 1, 0, 1, 1, 0, 0},
        {0, 0, 1, 1, 0, 1, 1, 0},
        {0, 0, 0, 1, 1, 0, 0, 1},
        {0, 0, 0, 0, 1, 0, 0, 1},
        {0, 0, 0, 0, 0, 1, 1, 0},
    }};
    matpow(g, Read<i64>(std::cin), ans);
    std::cout << ans[0][0] << std::endl;
    return 0;
}
