package practic.utils;

import java.util.List;

public class GlobalVariable {
    private static List<Long> list = null;

    public static List<Long> getList() {
        return list;
    }

    public static void setList(List<Long> list) {
        GlobalVariable.list = list;
    }
}
