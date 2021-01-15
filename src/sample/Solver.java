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
        int[][] vis = new int[x][y];
        int[][] deg = new int[x][y];
        ArrayList<Pair> all = new ArrayList<>();
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                vis[i][j] = 0;
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
            all.add(cur);
            //System.out.println("visiting " + cur.first + " " + cur.second);
            vis[cur.first][cur.second] = 1;
            q.remove(0);

            for(int dir : pipes[cur.first][cur.second].dirs) {
                Pair next = get_next(cur.first, cur.second, dir);
                if(next == null) {
                    //handle err?
                    System.out.println("Bad, next == null");
                    throw new RuntimeException();
                }

                if(next.first == x-2 && next.second == y) {
                    good = true;
                    deg[cur.first][cur.second] ++;
                }
                if(!good(next, x, y)) continue;
                if(!is_edge(cur, next, pipes)) continue;
                deg[next.first][next.second] ++;
                if(vis[next.first][next.second] != 0) continue;
                q.add(next);
                vis[next.first][next.second] = 2;
            }
        }
        if(good) {
            // System.out.println("good");
            boolean exists = false;
            for(Pair xo : all) {
                if(pipes[xo.first][xo.second].dirs.size() == 3) {
                    exists = true;
                    if(deg[xo.first][xo.second] != 3)
                    return Optional.empty();
                }

            }
            // if
            if(exists) {
                //check for it
                for(Pair xo : all) {
                    if(pipes[xo.first][xo.second].dirs.size() == 2 && deg[xo.first][xo.second] != 2) {
                        return Optional.empty();
                    }
                }
                return Optional.of(all);
            }
            return Optional.of(all);
        } else {
            // System.out.println("bad");
            return Optional.empty();
        }
    }
}
