package socialnetwork.comunity;

import java.util.*;

public class Graph<T> {

    private Map<T, List<T> > map = new HashMap<>();
    private Map<T,Boolean> connectedCompMap=new HashMap<>();;

    /**
     * adauga un vf nou in graf
     * @param s nodul adaugat
     */
    public void addVertex(T s)
    {

        map.put(s, new LinkedList<T>());
    }

    /**
     * adauga o muchie in graf
     * @param source vf de pornire
     * @param destination vf destinatie
     * @param bidirectional indica daca e graf orientat sau nu
     */
    public void addEdge(T source,
                        T destination,
                        boolean bidirectional)
    {

        if (!map.containsKey(source))
            addVertex(source);

        if (!map.containsKey(destination))
            addVertex(destination);

        map.get(source).add(destination);  //de la sursa la dest

        if (bidirectional == true)
        {

            map.get(destination).add(source);  //trb adaugat in ambele sensuri
        }
    }

    /**
     * determina nr de muchii
     * @return nr de muchii din graf
     */

    //de la fiecare vf cheie, se iau toti vecinii posibili => toate muchiile

    public int getVertexCount()
    {
       /* System.out.println("The graph has "
                + map.keySet().size()
                + " vertex");

        */
        return map.keySet().size();
    }

    /**
     * aplica alg DFS pe un graf(parcurge in adancime)
     * @param start-el de la care se incepe parcurgerea
     * @param visited-vectorul de vizitati
     */
    void DFS(T start,Map<T,Boolean>visited){

        Stack<T> stack=new Stack<>();
        stack.push(start);

        while(!stack.empty()){
            start=stack.peek();
            stack.pop();
            connectedCompMap.put(start,true);
            if(visited.get(start)==false)
                visited.put(start,true);

            for(T t:map.get(start)){
                if(visited.get(t)==false) stack.push(t);
            }
        }
    }

    /**
     * @return Nr decomunitati=nr de componente conexe
     */
    public int numarComunitati(){
        int n=getVertexCount();
        Map<T,Boolean> visited=new HashMap<>(n);
        for(T t:map.keySet())
            visited.put(t,false);

        int count=0;
        for(T t:map.keySet()){
            if(visited.get(t)==false){
                //connectedCompMap=new HashMap<>();
                DFS(t,visited);
                count++;
            }
        }
        return count;
    }




}
