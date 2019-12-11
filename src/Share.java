import java.util.*;

public class Share {
    Set<Island> islands = new HashSet<>();

    private Island getIsland(Subject s) {
        for (Island i : islands)
            if (i.contains(s))
                return i;
        throw new RuntimeException(s + " not in any island");
    }

    public boolean can_share(Right right, Object_ object_1, Object_ object_2, List<Edge> graph) {
        List<Edge> list_subj = new ArrayList<>();
        List<Edge> list_obj = new ArrayList<>();
        // создание списка рёбер состоящих из субъектов
        for (Edge e : graph) {
            if (e.getObject_1().getClassName().equals("Subject") && e.getObject_2().getClassName().equals("Subject")) {
                list_subj.add(e);
            }
        }
        //список всех субъектов
        List<Subject> listAllSubject = new ArrayList<>();
        for (Edge e : graph) {
            if (e.getObject_1().getClassName().equals("Subject")) {
                listAllSubject.add((Subject) e.getObject_1());
            }
            if (e.getObject_2().getClassName().equals("Subject")) {
                listAllSubject.add((Subject) e.getObject_2());
            }
        }
        //список рёбер состоящих из объектов
        for (Edge e : graph) {
            if (e.getObject_1().getClassName().equals("Object") && e.getObject_2().getClassName().equals("Object"))
                list_obj.add(e);
        }

        Set<Object_> handledInAnyIsland = new HashSet<>();
        Deque<Object_> stack = new ArrayDeque<>();
        Object_ current;

        //групирование по островам
        int i = 1;
        for (Edge e : graph) {
            if (e.getObject_1().getClassName().equals("Subject") && !handledInAnyIsland.contains(e.getObject_1())) {
                stack.push(e.getObject_1());
            }
            if (e.getObject_2().getClassName().equals("Subject") && !handledInAnyIsland.contains(e.getObject_2())) {
                stack.push(e.getObject_2());
            }
            if (!stack.isEmpty()) {
                Island island = new Island(i);
                while (!stack.isEmpty()) {
                    current = stack.pop();
                    island.add(current);

                    for (Edge k : list_subj) {
                        Object_ o = null;
                        if (k.getObject_1().getObjectNumber() == current.getObjectNumber()) {
                            o = k.getObject_2();
                        } else if (k.getObject_2().getObjectNumber() == current.getObjectNumber()) {
                            o = k.getObject_1();
                        }
                        if (!island.contains(o) && !stack.contains(o) && o != null) {
                            stack.push(o);
                        }
                    }
                }

                islands.add(island);
                handledInAnyIsland.addAll(island);
                i++;
            }


        }

        //установление связей между остравами
        Deque<Object_> stack_bridge = new ArrayDeque<>();
        Object_ current_for_obj;
        HashMap<Subject,Set<Object_>> connectedObjects = new HashMap<>();
        Set<Set<Island>> connectedIslands = new HashSet<>();
        Set<Set<Object_>> grantConnectedObject = new HashSet<>();
;        for (Subject e : listAllSubject) {
            stack_bridge.push(e);
            Set<Object_> set = new HashSet<>();
            while (!stack_bridge.isEmpty()) {
                current_for_obj = stack_bridge.pop();
                for (Edge k : graph) {

                    if ((k.getObject_1().getObjectNumber() == current_for_obj.getObjectNumber()) && k.getRight().getRight().equals("take") && k.getObject_2().getClassName().equals("Object")) {
                        stack_bridge.push(k.getObject_2());
                        set.add(k.getObject_2());
                    }
                    if ( k.getRight().getRight().equals("grant") && (k.getObject_1().getObjectNumber() == current_for_obj.getObjectNumber() || k.getObject_2().getObjectNumber() == current_for_obj.getObjectNumber()) ) {
                        grantConnectedObject.add(Set.of(k.getObject_1(), k.getObject_2()));
                    }
                    if ( k.getObject_1().getObjectNumber() == current_for_obj.getObjectNumber() && k.getObject_2().getClassName().equals("Subject") ) {
                        if (!(getIsland(e) == getIsland((Subject) k.getObject_2())))
                        connectedIslands.add(Set.of(getIsland(e), getIsland((Subject) k.getObject_2())));
                    }
                    if ( k.getObject_2().getObjectNumber() == current_for_obj.getObjectNumber() && k.getObject_1().getClassName().equals("Subject") &&  k.getRight().getRight().equals("grant") ) {
                        connectedIslands.add(Set.of(getIsland(e), getIsland((Subject) k.getObject_1())));
                    }


                }
            }
            connectedObjects.put(e,set);

        }
//        for()

        for (Island t : islands) {
            System.out.println(t.getIslandIndex() + " " + t);
        }
        System.out.println("Соединенные острава:        " + connectedIslands);
        System.out.println("object:      " + connectedObjects);
        System.out.println("grant:      " + grantConnectedObject);
        System.out.println("Все острава:    " + islands);
      //System.out.println(list_obj);

        return true;
    }
}
