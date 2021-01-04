package sample;

import java.util.*;

public class Solver {

    static class Pair {
        public int first, second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
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

    static boolean good(Pair par, int x, int y, Boolean[][] vis) {
        return par != null && par.first >= 0 && par.first < x && par.second >= 0 && par.second < y && !vis[par.first][par.second];
    }
    static boolean is_edge(Pair from, Pair to, Pipe[][] pipes) {
        Pair poss1 = get_next(to.first, to.second, pipes[to.first][to.second].on);
        Pair poss2 = get_next(to.first, to.second, pipes[to.first][to.second].tw);
        return ((poss1 != null && poss1.first == from.first && poss1.second == from.second)||(poss2 != null && poss2.first == from.first && poss2.second == from.second));
    }
    static Optional<ArrayList<Pair>> connected(Pipe[][] pipes, int x, int y) {
        // from (-1, 0) to (x-2, y)
        // parent of (i,j)
        Pair[][] p = new Pair[x][y];
        Boolean[][] vis = new Boolean[x][y];
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                vis[i][j] = false;
            }
        }
        List<Pair> q = new LinkedList<>();
        if(pipes[0][0].dirs.get(0) != 0)
            return Optional.empty();
        q.add(new Pair(0, 0));
        boolean good = false;
        while(!q.isEmpty()) {
            Pair cur = q.get(0);
            System.out.println("visiting " + cur.first + " " + cur.second);
            vis[cur.first][cur.second] = true;
            q.remove(0);
            Pair one = get_next(cur.first, cur.second, pipes[cur.first][cur.second].on);
            Pair tw = get_next(cur.first, cur.second, pipes[cur.first][cur.second].tw);
            if(tw == null && one == null) continue;

            if((one != null && one.first == x-2 && one.second == y) || (tw != null && tw.first == x-2 && tw.second == y)) {
                good = true;
                break;
            }
            if(good(one, x, y, vis) && is_edge(cur, one, pipes)) {
                p[one.first][one.second] = cur;
                q.add(one);
            } else if(good(tw, x, y, vis) && is_edge(cur, tw, pipes)){
                p[tw.first][tw.second] = cur;
                q.add(tw);
            }
        }
        if(good) {
            System.out.println("good");
            // go from (x-2, y-1) to beg
            ArrayList<Pair> res = new ArrayList<>();
            Pair cur = new Pair(x-2, y-1);
            while(cur != null) {
                res.add(cur);
                cur = p[cur.first][cur.second];
            }
            return Optional.of(res);
        } else {
            System.out.println("bad");
            return Optional.empty();
        }
    }
}
