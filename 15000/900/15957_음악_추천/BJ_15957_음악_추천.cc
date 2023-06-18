// https://www.acmicpc.net/problem/15957

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <memory>
#include <vector>
#include <tuple>
#include <unordered_map>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using i64 = int64_t;

struct SharedContext {
	i32 N = 0;
	i64 J = 0;
	vector<pair<i32, i32>> ranges;
	vector<vector<i32>> musics;
	vector<i32> singers;
	vector<tuple<i32, i32, i32>> queries;

	SharedContext()
		: ranges(0)
		, musics(0)
		, singers(0)
		, queries(0) {}
};

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

	void update(i32 const beg, i32 const end, i64 dvalue) {
		dvalue = dvalue / (end - beg);
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

template <typename T>
T rd() {
	T v; cin >> v;
	return v;
}

i32 dfs(vector<pair<i32, i32>>& ranges, vector<vector<i32>> const& g, i32 const u = 0, i32 const order = 1) {
	i32 last_order = order;
	for (auto const v : g[u]) {
		last_order = dfs(ranges, g, v, last_order + 1);
	}

	ranges[u] = {order, last_order + 1};
	return last_order;
}

void init(SharedContext& ctx) {
	i32 Q;
	cin >> ctx.N >> Q >> ctx.J;

	auto g = vector<vector<i32>>(ctx.N);
	for (i32 v = 1; v < ctx.N; ++v) {
		g[rd<i32>() - 1].emplace_back(v);
	}

	ctx.ranges.resize(ctx.N);
	dfs(ctx.ranges, g);

	ctx.musics.resize(ctx.N);
	ctx.singers.resize(ctx.N);
	for (i32 music_id = 0; music_id < ctx.N; ++music_id) {
		auto const singer_id = rd<i32>() - 1;
		ctx.singers[music_id] = singer_id;
		ctx.musics[singer_id].emplace_back(music_id);
	}

	ctx.queries.reserve(Q);
	for (i32 i = 0; i < Q; ++i) {
		i32 t, p, s; cin >> t >> p >> s;
		ctx.queries.emplace_back(t, p - 1, s);
	}

	sort(ctx.queries.begin(), ctx.queries.end(), [](auto const& lhs, auto const& rhs) { return get<0>(lhs) < get<0>(rhs); });
}

void execute_queries(SharedContext& ctx) {
	auto times = vector<i32>(ctx.N, -1);
	auto scores = FenwickTree(ctx.N);
	auto lb = vector<i32>(ctx.N, 0);
	auto ub = vector<i32>(ctx.N, i32(ctx.queries.size()));
	auto pendings = vector<vector<i32>>(ctx.queries.size());

	for (;;) {
		i32 num_pendings = 0;
		for (i32 i = 0; i < ctx.N; ++i) {
			if (0 < ctx.musics[i].size() && lb[i] < ub[i]) {
				pendings[(lb[i] + ub[i]) / 2].emplace_back(i);
				++num_pendings;
			}
		}

		if (num_pendings == 0) {
			break;
		}

		scores.clear();

		for (i32 qi = 0; 0 < num_pendings; ++qi) {
			auto const& [t, p, s] = ctx.queries[qi];
			auto const& [beg, end] = ctx.ranges[p];
			scores.update(beg, end, s);

			while (!pendings[qi].empty()) {
				auto const singer_id = pendings[qi].back();
				pendings[qi].pop_back();
				--num_pendings;

				i64 goal = i64(ctx.J * ctx.musics[singer_id].size());
				i64 score = 0;
				for (auto const music_id : ctx.musics[singer_id]) {
					score += scores.query(ctx.ranges[music_id].first);
					if (goal < score) {
						break;
					}
				}

				if (goal < score) {
					times[singer_id] = t;
					ub[singer_id] = qi;
				}
				else {
					lb[singer_id] = qi + 1;
				}
			}
		}
	}

	for (i32 music_id = 0; music_id < ctx.N; ++music_id) {
		cout << times[ctx.singers[music_id]] << '\n';
	}

	cout.flush();
}

int main(int argc, char const* argv[]) {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);

	auto ctx = SharedContext{};
	init(ctx);
	execute_queries(ctx);

	return 0;
}
