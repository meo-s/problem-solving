// https://www.acmicpc.net/problem/15614

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <queue>
#include <unordered_map>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using u32 = uint32_t;

constexpr u32 INF = numeric_limits<u32>::max();

struct Comparator {
	template <typename ...TArgs>
	bool operator()(tuple<TArgs...> const& lhs, tuple<TArgs...> const& rhs) {
		return get<0>(rhs) < get<0>(lhs);
	}
};

struct DisjointSet {
	vector<u32> parents;

	DisjointSet(sz const N) : parents(N) {
		for (u32 i = 0; i < N; ++i) parents[i] = i;
	}

	u32 query(u32 const u) {
		if (parents[u] != u) parents[u] = query(parents[u]);
		return parents[u];
	}

	void merge(u32 const u, u32 const v) {
		if (query(u) != query(v)) parents[query(v)] = query(u);
	}
};

int main(int const arc, char const* argv[]) {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);

	auto it = istream_iterator<u32>{cin};
	auto const V = *it;
	auto const S = *++it;
	auto const E = *++it;
	auto const L = static_cast<u32>(log2(S)) + 1;

	auto s2i = vector<u32>(V + 1);
	auto stations = vector<u32>(S);
	auto is_station = vector<bool>(V + 1);
	for (i32 i = 0; i < S; ++i) {
		auto const s = *++it;
		s2i[s] = i;
		stations[i] = s;
		is_station[s] = true;
	}

	auto g = vector<unordered_map<u32, u32>>(V + 1);
	for (i32 i = 0; i < E; ++i) {
		auto const u = *++it;
		auto const v = *++it;
		auto const w = *++it;
		if (auto const iter = g[u].find(v); iter == g[u].end() || w < iter->second) {
			g[u][v] = w;
			g[v][u] = w;
		}
	}

	auto dijkstra = [&](u32 const start, vector<u32>& dists, vector<tuple<u32, u32, u32>>& edges) {
		dists[start] = 0;

		auto waypoints = priority_queue<tuple<u32, u32, u32>, vector<tuple<u32, u32, u32>>, Comparator>{};
		waypoints.emplace(0, start, start);
		while (0 < waypoints.size()) {
			auto [wu, u, p] = waypoints.top(); waypoints.pop();
			if (dists[u] == wu) {
				wu = is_station[u] ? 0 : wu;
				for (auto const& [v, w] : g[u]) {
					if (v != p && wu + w < dists[v]) {
						dists[v] = wu + w;
						if (!is_station[v]) waypoints.emplace(dists[v], v, p);
						else {
							edges.emplace_back(s2i[p], s2i[v], dists[v]);
							waypoints.emplace(dists[v], v, v);
						}
					}
				}
			}
		}
	};

	auto dists = vector<u32>(g.size(), INF);
	auto edges = vector<tuple<u32, u32, u32>>{};
	for (i32 i = 1; i <= V; ++i) if (is_station[i] && dists[i] == INF) dijkstra(i, dists, edges);
	sort(edges.begin(), edges.end(), [](auto const& lhs, auto const& rhs) { return get<2>(lhs) < get<2>(rhs); });

	auto mst = vector<vector<pair<u32, u32>>>(S);
	auto disjoint_set = DisjointSet(S);
	for (auto const& [u, v, w] : edges) {
		if (disjoint_set.query(u) != disjoint_set.query(v)) {
			disjoint_set.merge(u, v);
			mst[u].emplace_back(v, w);
			mst[v].emplace_back(u, w);
		}
	}
	
	auto costs = vector<vector<u32>>(S);
	auto jumps = vector<vector<u32>>(S);
	for (i32 i = 0; i < S; ++i) {
		jumps[i].reserve(L);
		costs[i].reserve(L);
	}

	auto depths = vector<u32>(S, INF);
	for (i32 i = 0; i < S; ++i) {
		if (depths[i] == INF) {
			depths[i] = 0;
			jumps[i].emplace_back(i);
			costs[i].emplace_back(0);

			auto vertices = deque<pair<i32, i32>>{};
			vertices.emplace_back(i, INF);
			while (0 < vertices.size()) {
				auto const [u, p] = vertices.front(); vertices.pop_front();
				for (auto const& [v, w] : mst[u]) {
					if (v != p) {
						jumps[v].emplace_back(u);
						costs[v].emplace_back(w);
						depths[v] = depths[u] + 1;
						if (!mst[v].empty()) vertices.emplace_back(v, u);
					}
				}
			}
		}
	}

	for (i32 i = 1; i < L; ++i) {
		for (i32 u = 0; u < S; ++u) {
			jumps[u].emplace_back(jumps[jumps[u][i - 1]][i - 1]);
			costs[u].emplace_back(max(costs[jumps[u][i - 1]][i - 1], costs[u][i - 1]));
		}
	}

	auto Q = *++it;
	while (0 < Q--) {
		auto u = s2i[*++it];
		auto v = s2i[*++it];
		auto const b = *++it;

		auto ans = disjoint_set.query(u) == disjoint_set.query(v);
		if (ans) {
			for (i32 i = L - 1; 0 <= i; --i) {
				if (!ans || depths[u] <= depths[v]) break;
				if (depths[v] <= depths[jumps[u][i]]) {
					ans = ans && costs[u][i] <= b;
					u = jumps[u][i];
				}
			}
			for (i32 i = L - 1; 0 <= i; --i) {
				if (!ans || depths[v] <= depths[u]) break;
				if (depths[u] <= depths[jumps[v][i]]) {
					ans = ans && costs[v][i] <= b;
					v = jumps[v][i];
				}
			}
			for (i32 i = L - 1; 0 <= i; --i) {
				if (jumps[u][i] != jumps[v][i]) {
					ans = ans && costs[u][i] <= b && costs[v][i] <= b;
					u = jumps[u][i];
					v = jumps[v][i];
				}
			}
			if (u != v) ans = ans && costs[u][0] <= b && costs[v][0] <= b;
		}

		cout << (ans ? "TAK" : "NIE") << '\n';
	}

	cout.flush();
	return 0;
}
