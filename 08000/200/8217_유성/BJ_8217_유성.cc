// https://www.acmicpc.net/problem/8217

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <memory>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using i64 = int64_t;

class FenwickTree {

	vector<i64> tree;

	void update(i32 index, i64 const dvalue) {
		while (index < i32(tree.size())) {
			tree[index] += dvalue;
			index += index & -index;
		}
	}

public:

	FenwickTree(sz const size)
		: tree(size + 1) {}

	void update(i32 const beg, i32 const end, i64 const dvalue) {
		update(beg, dvalue);
		update(end, -dvalue);
	}

	i64 query(i32 index) {
		i64 value = 0;
		while (0 < index) {
			value += tree[index];
			index -= index & -index;
		}
		return value;
	}

	void clear() {
		fill(tree.begin(), tree.end(), 0);
	}

};

i32 N, M, Q;
vector<vector<i32>> areas(0);
vector<i32> goals(0);
vector<tuple<i32, i32, i32>> meteors(0);
unique_ptr<FenwickTree> tree;

template <typename T>
T rd() {
	T v; cin >> v;
	return v;
}

void init() {
	cin >> N >> M;
	areas.resize(N);
	for (i32 area = 1; area <= M; ++area) {
		areas[rd<i32>() - 1].emplace_back(area);
	}

	goals.reserve(N);
	for (i32 i = 0; i < N; ++i) {
		goals.emplace_back(rd<i32>());
	}

	cin >> Q;
	meteors.reserve(Q);
	for (i32 i = 0; i < Q; ++i) {
		auto const l = rd<i32>();
		auto const r = rd<i32>();
		auto const a = rd<i32>();
		meteors.emplace_back(l, r, a);
	}
}

void execute_queries() {
	vector<i32> ans(N);
	vector<i32> lb(N);
	vector<i32> ub(N, Q);
	vector<vector<i32>> pendings(Q);
	for (;;) {
		i32 num_pendings = 0;

		for (i32 country = 0; country < N; ++country) {
			if (lb[country] < ub[country]) {
				pendings[(lb[country] + ub[country]) / 2].emplace_back(country);
				++num_pendings;
			}
		}

		if (num_pendings == 0) {
			break;
		}

		if (tree == nullptr) {
			tree.reset(new FenwickTree(M));
		}
		else {
			tree->clear();
		}

		for (i32 k = 0; k < Q && 0 < num_pendings; ++k) {
			auto const& [l, r, a] = meteors[k];
			if (l <= r) {
				tree->update(l, r + 1, a);
			}
			else {
				tree->update(l, M + 1, a);
				tree->update(1, r + 1, a);
			}

			while (0 < pendings[k].size()) {
				auto const country = pendings[k].back();
				pendings[k].pop_back();
				--num_pendings;

				i64 num_samples = 0;
				for (auto const area : areas[country]) {
					num_samples += tree->query(area);
					if (goals[country] <= num_samples) {
						break;
					}
				}

				if (goals[country] <= num_samples) {
					ub[country] = k;
					ans[country] = k + 1;
				}
				else {
					lb[country] = k + 1;
				}
			}
		}
	}

	for (i32 country = 0; country < N; ++country) {
		if (ans[country] == 0) {
			cout << "NIE\n";
		}
		else {
			cout << ans[country] << '\n';
		}
	}

	cout.flush();
}

int main(int argc, char const* argv[]) {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);

	init();
	execute_queries();

	return 0;
}
