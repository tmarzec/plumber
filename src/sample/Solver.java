package sample;

import java.util.*;

public class Solver {

    static class Pair {
        public int first, second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "first=" + first +
                    ", second=" + second +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return first == pair.first &&
                    second == pair.second;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
    static Pair get_next(int i, int j, int to) {
        switch (to){
            case 0:
                return new Pair(i-1, j);
            case 1:
                return new Pair(i, j+1);
            case 2:
                return new Pair(i+1, j);
            case 3:
                return new Pair(i, j-1);
        }
        return null;
    }
    static boolean is_edge(Pair from, Pair to, Pipe[][] pipes) {
        if(from.first == to.first -1 && from.second == to.second) {
            return pipes[to.first][to.second].dirs.contains(0) && pipes[from.first][from.second].dirs.contains(2);
        }
        else if(from.first == to.first && from.second == to.second - 1) {
            return pipes[to.first][to.second].dirs.contains(3) && pipes[from.first][from.second].dirs.contains(1);
        }
        return is_edge(to, from, pipes);
    }
    static boolean good(Pair par, int x, int y) {
        return par != null && par.first >= 0 && par.first < x && par.second >= 0 && par.second < y;
    }
    static Optional<ArrayList<Pair>> connected(Pipe[][] pipes, int x, int y) {
        // from (-1, 0) to (x-2, y)
        // parent of (i,j)
        Set<Pair>[][] p = new HashSet[x][y];
        int[][] vis = new int[x][y];
        int[][] deg = new int[x][y];
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                vis[i][j] = 0;
                p[i][j] = new HashSet<>();
            }
        }
        List<Pair> q = new LinkedList<>();
        if(pipes[0][0].dirs.get(0) != 0)
            return Optional.empty();
        q.add(new Pair(0, 0));
        boolean good = false;
        deg[0][0] = 1;
        while(!q.isEmpty()) {
            Pair cur = q.get(0);
            System.out.println("visiting " + cur.first + " " + cur.second);
            if(cur.first == 4 && cur.second == 9) {
                System.out.println("alkamakkf");
            }
            vis[cur.first][cur.second] = 1;
            q.remove(0);

            for(int dir : pipes[cur.first][cur.second].dirs) {
                Pair next = get_next(cur.first, cur.second, dir);
                if(next == null) {
                    //handle err?
                    System.out.println("Bad, next == null");
                    throw new RuntimeException();
                }

                //System.out.println("Next is: " + next.first + " " + next.second);
                if(next.first == x-2 && next.second == y) {
                    good = true;
                    deg[cur.first][cur.second] ++;
                }
                if(!good(next, x, y)) continue;
                if(!is_edge(cur, next, pipes)) continue;
                deg[next.first][next.second] ++;
                if(vis[next.first][next.second] != 1) p[next.first][next.second].add(cur);
                if(vis[next.first][next.second] != 0) continue;
                q.add(next);
                vis[next.first][next.second] = 2;
            }
        }
        if(good) {
            System.out.println("good");
            // go from (x-2, y-1) to beg
            Set<Pair> res = new HashSet<>();
            List<Pair> qq = new LinkedList<>();
            Pair cur = new Pair(x-2, y-1);
            qq.add(cur);
            for(int i = 0; i < x; i++) {
                for(int j = 0; j < y; j++) {
                    //deg[i][j] = 0;
                    vis[i][j] = 0;
                }
            }
            while(!qq.isEmpty()) {
                Pair z = qq.get(0);
                if(vis[z.first][z.second] == 1) {
                    qq.remove(0);
                    continue;
                }
                vis[z.first][z.second] = 1;
                res.add(z);
                qq.remove(0);
                for(Pair xo : p[z.first][z.second])
                {

                    if(vis[xo.first][xo.second] == 0) {
                        qq.add(xo);
                    }
                    //vis[xo.first][xo.second] = 1;
                }
                qq.addAll(p[z.first][z.second]);
            }
            boolean exists = false;
            for(Pair xo : res) {
                if(pipes[xo.first][xo.second].dirs.size() == 3) {
                    exists = true;
                    if(deg[xo.first][xo.second] != 3)
                    return Optional.empty();
                }

            }
            // if
            if(exists) {
                //check for it
                for(int i = 0; i < x; i++) {
                    for(int j = 0; j < y; j++) {
                        Pair xo = new Pair(i,j);
                        if(pipes[xo.first][xo.second].dirs.size() == 2 && deg[xo.first][xo.second] != 2) {
                            return Optional.empty();
                        }
                    }
                }
                ArrayList<Pair> reto = new ArrayList<>();
                // put there all nodes
                for(int i = 0; i < x; i++) {
                    for(int j = 0; j < y; j++) {
                        if(pipes[i][j].dirs.size() > 0) reto.add(new Pair(i, j));
                    }
                }
                return Optional.of(reto);
            }
            ArrayList<Pair> reto = new ArrayList<>(res);
            return Optional.of(reto);
        } else {
            System.out.println("bad");
            return Optional.empty();
        }
    }
}
