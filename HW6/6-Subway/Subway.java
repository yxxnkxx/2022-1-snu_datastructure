
import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.*;


public class Subway {


    public static final long INF = 500000000;

    public static void main(String[] args) throws IOException {
        HashMap<Integer, Vertex> stations = new HashMap<>(); //index, Vertex (index는 path 추적을 위해)
        HashMap<String, Integer> indexMatch = new HashMap<>(); //code, index
        HashMap<String, LinkedList<String>> nameList = new HashMap<>(); //name, code list (환승역)
        HashMap<String, ArrayList<Edge>> edges = new HashMap<>(); //code, edges


        FileInputStream fileStream = new FileInputStream(args[0]);
        BufferedReader br = new BufferedReader((new InputStreamReader(fileStream, StandardCharsets.UTF_8)));
        int index=0;
        while (true) {
            String str = br.readLine();
            if (str==null || str.equals("\n") || str.length()==0) {
                break;
            }
            String[] input = str.split("\\s");
            String code = input[0];
            String name = input[1];
            String line = input[2];

            indexMatch.put(code, index);
            stations.put(index, new Vertex(code, name, line, index++));


            if (nameList.containsKey(name)) {
                //이미 name이 있는데 추가하면 환승역
                nameList.get(name).add(code);
            } else {
                LinkedList<String> codes = new LinkedList<>();
                codes.add(code);
                nameList.put(name,codes);

            }
        }


        while (true) {
            //add edges
            String str = br.readLine();
            if (str==null || str.equals(" ")) {
                break;
            }

            String[] input = str.split("\\s");
            String start = input[0];
            String end = input[1];
            int edge = Integer.parseInt(input[2]);
            if (edges.containsKey(start)) {
                edges.get(start).add(new Edge(start, end, edge));
            } else {
                ArrayList<Edge> newEdge = new ArrayList<>();
                newEdge.add(new Edge(start, end, edge));
                edges.put(start, newEdge);
            }

        }

        //환승역 edge 양방향으로 5 추가
        for (String key : nameList.keySet()) {
            LinkedList<String> values = nameList.get(key);
            if (values.size() != 0 && values.size() != 1) {
                for (int i=0; i<values.size()-1; i++) {
                    for (int j=i+1; j<values.size(); j++) {
                        String start = values.get(i);
                        String end = values.get(j);
                        if (edges.containsKey(start)) {
                            edges.get(start).add(new Edge(start, end, 5));
                        } else {
                            ArrayList<Edge> newEdge = new ArrayList<>();
                            newEdge.add(new Edge(start, end, 5));
                            edges.put(start, newEdge);
                        }
                        if (edges.containsKey(end)) {
                            edges.get(end).add(new Edge(end, start, 5));
                        } else {
                            ArrayList<Edge> newEdge = new ArrayList<>();
                            newEdge.add(new Edge(end, start, 5));
                            edges.put(end, newEdge);
                        }
                    }
                }
            }
        }


        //////////////////////////////////////////
        //input start - end
        InputStream inputStream = System.in;
        BufferedReader br2 = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

        while (true)
        {
            try
            {
                String input = br2.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input, edges, stations, nameList, indexMatch);
                reset(stations); //reset distance and check

            }
            catch (IOException e)
            {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
     }

    private static void command(String input, HashMap<String, ArrayList<Edge>> edges, HashMap<Integer, Vertex> stations, HashMap<String, LinkedList<String>> nameList, HashMap<String, Integer> indexMatch) throws IOException {
        int[] path=new int[100000]; //역 최대 수 만개

        String[] splitInput = input.split("\\s");
        LinkedList<String> namelist = nameList.get(splitInput[0]);
        String start = namelist.get(0);
        LinkedList<String> namelist2 = nameList.get(splitInput[1]);
        String end = namelist2.get(0);

        PriorityQueue<Vertex> que = new PriorityQueue<>(); //for dijkstra

        Vertex startV = stations.get(indexMatch.get(start));
        Vertex endV = stations.get(indexMatch.get(end));
        startV.distance = 0;
        que.offer(startV);
        path[startV.index]=0;


        //dijkstra
        while (!que.isEmpty()) {
            Vertex vertex = que.poll();
            vertex.check = true;

            ArrayList<Edge> edgesList = edges.get(vertex.code);
            for (Edge edge : edgesList) {
                Vertex vertex1 = stations.get(indexMatch.get(edge.end));
                if (!vertex1.check) {
                    if (vertex1.name.equals(startV.name)) { //시작역이 환승역
                        vertex1.distance=0;
                        que.offer(vertex1);
                        path[vertex1.index]=vertex.index;
                    } else if (vertex.name.equals(vertex1.name) && vertex1.name.equals(endV.name)) {
                        //끝나는 역이 환승역일 경우
                        vertex1.distance = vertex.distance;
                        que.offer(vertex1);
                        path[vertex1.index]=vertex.index;
                    }
                    else if (vertex1.distance > edge.edge + vertex.distance) {
                        vertex1.distance = edge.edge + vertex.distance;
                        que.offer(vertex1);
                        path[vertex1.index]=vertex.index;
                    }
                }
            }

        }

        //find path

        Stack<Integer> stack = new Stack<>();
        int curr = endV.index;
        while (curr!=startV.index) {
            stack.push(curr);
            curr = path[curr];
        }
        stack.push(curr);


        ArrayList<String> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            Vertex v = stations.get(stack.pop());
            if (!result.isEmpty() && result.get(result.size()-1).equals(v.name)) {
                if (result.size()!=1 && !result.get(result.size()-1).equals(endV.name)) {
                    result.set(result.size()-1, "["+v.name+"]"); //환승역 처리, 시작역이나 끝역은 따로 처리x 중복인 경우 추가x
                }
            } else {
                result.add(v.name);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String str : result) {
            sb.append(str);
            sb.append(" ");
        }


        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        System.setOut(out);
        System.out.println(sb.toString().trim());
        System.out.println(endV.distance);


    }


    private static void reset(HashMap<Integer, Vertex> stations) {
        for (Vertex vertex : stations.values()) {
            vertex.check=false;
            vertex.distance=INF;
        }
    }

}
