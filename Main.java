import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Main {
    private static final int N = 5;
    private static final int INF = Integer.MAX_VALUE / 4;
    private static final int[][] graph = new int[N][N];

    public static void main(String[] args) {
        addEdge('A', 'B', 5);
        addEdge('A', 'D', 1);
        addEdge('B', 'C', 2);
        addEdge('B', 'E', 4);
        addEdge('C', 'E', 3);
        addEdge('D', 'E', 6);

        System.out.println("практична №3. варіант 8 (№18 У ЖУРНАЛІ)");
        System.out.println("неорієнтований зважений граф, початок A, ціль E");
        printMatrix();
        printAdjacencyList();
        bfs(0);
        dfs(0);
        dijkstra(0, 4);
    }

    private static void addEdge(char from, char to, int weight) {
        int a = from - 'A';
        int b = to - 'A';
        graph[a][b] = weight;
        graph[b][a] = weight;
    }

    private static char name(int vertex) {
        return (char) ('A' + vertex);
    }

    private static String vertices(boolean[] visited) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (visited[i]) {
                result.add(String.valueOf(name(i)));
            }
        }
        return result.toString();
    }

    private static String queueState(Queue<Integer> queue) {
        List<String> result = new ArrayList<>();
        for (int v : queue) {
            result.add(String.valueOf(name(v)));
        }
        return result.toString();
    }

    private static String number(int value) {
        return value == INF ? "∞" : String.valueOf(value);
    }

    private static void printMatrix() {
        System.out.println("\n1. матриця суміжності (0 = немає ребра)");
        System.out.print("    ");
        for (int i = 0; i < N; i++) {
            System.out.printf("%4c", name(i));
        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf("%4c", name(i));
            for (int j = 0; j < N; j++) {
                System.out.printf("%4d", graph[i][j]);
            }
            System.out.println();
        }
    }

    private static void printAdjacencyList() {
        System.out.println("\n2. список суміжності");
        for (int i = 0; i < N; i++) {
            List<String> neighbors = new ArrayList<>();
            for (int j = 0; j < N; j++) {
                if (graph[i][j] > 0) {
                    neighbors.add(name(j) + "(" + graph[i][j] + ")");
                }
            }
            System.out.println(name(i) + ": " + String.join(", ", neighbors));
        }
    }

    private static void bfs(int start) {
        System.out.println("\n3. BFS ВІД A");
        boolean[] visited = new boolean[N];
        int[] distance = new int[N];
        Arrays.fill(distance, -1);
        Queue<Integer> queue = new ArrayDeque<>();
        List<String> order = new ArrayList<>();
        visited[start] = true;
        distance[start] = 0;
        queue.add(start);
        System.out.println("початок: черга=" + queueState(queue) + ", відвідані=" + vertices(visited));
        int step = 1;
        while (!queue.isEmpty()) {
            int current = queue.remove();
            order.add(String.valueOf(name(current)));
            for (int next = 0; next < N; next++) {
                if (graph[current][next] > 0 && !visited[next]) {
                    visited[next] = true;
                    distance[next] = distance[current] + 1;
                    queue.add(next);
                }
            }
            System.out.println("Крок " + step++ + ": взято " + name(current)
                    + ", черга=" + queueState(queue) + ", відвідані=" + vertices(visited));
        }
        System.out.println("Порядок BFS: " + String.join(" -> ", order));
        for (int i = 0; i < N; i++) {
            System.out.println("Відстань A -> " + name(i) + " (кількість ребер): " + distance[i]);
        }
    }

    private static void dfs(int start) {
        System.out.println("\n4. DFS ВІД A (СУСІДИ ЗА АЛФАВІТОМ)");
        boolean[] visited = new boolean[N];
        List<String> order = new ArrayList<>();
        dfsVisit(start, visited, order);
        System.out.println("Порядок DFS: " + String.join(" -> ", order));
    }

    private static void dfsVisit(int current, boolean[] visited, List<String> order) {
        visited[current] = true;
        order.add(String.valueOf(name(current)));
        System.out.println("Вхід у " + name(current) + ", відвідані=" + vertices(visited));
        for (int next = 0; next < N; next++) {
            if (graph[current][next] > 0 && !visited[next]) {
                dfsVisit(next, visited, order);
            }
        }
    }

    private static void dijkstra(int start, int target) {
        System.out.println("\n5. АЛГОРИТМ ДЕЙКСТРИ ВІД A ДО E");
        int[] distance = new int[N];
        int[] previous = new int[N];
        boolean[] used = new boolean[N];
        Arrays.fill(distance, INF);
        Arrays.fill(previous, -1);
        distance[start] = 0;
        int step = 1;
        for (int i = 0; i < N; i++) {
            int current = -1;
            for (int v = 0; v < N; v++) {
                if (!used[v] && (current == -1 || distance[v] < distance[current])) {
                    current = v;
                }
            }
            if (current == -1 || distance[current] == INF) {
                break;
            }
            used[current] = true;
            for (int next = 0; next < N; next++) {
                if (!used[next] && graph[current][next] > 0
                        && distance[current] + graph[current][next] < distance[next]) {
                    distance[next] = distance[current] + graph[current][next];
                    previous[next] = current;
                }
            }
            System.out.print("Крок " + step++ + ": обрано " + name(current) + "; відстані: ");
            for (int v = 0; v < N; v++) {
                System.out.print(name(v) + "=" + number(distance[v]) + (v == N - 1 ? "" : ", "));
            }
            System.out.println();
        }
        if (distance[target] == INF) {
            System.out.println("Шлях до " + name(target) + " не існує.");
            return;
        }
        List<String> path = new ArrayList<>();
        for (int v = target; v != -1; v = previous[v]) {
            path.add(String.valueOf(name(v)));
        }
        Collections.reverse(path);
        System.out.println("Найкоротший шлях: " + String.join(" -> ", path));
        System.out.println("Сумарна вага шляху: " + distance[target]);
    }
}
