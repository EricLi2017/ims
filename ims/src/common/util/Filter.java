package common.util;


public final class Filter {
    public static String nullFilter(String src) {
        if (src == null) {
            return "";
        } else {
            return src.trim();
        }
    }

    public static void main(String[] args) {
        String src1 = null;
        String src2 = "123456";
        String src3 = "  123456   ";

        System.out.println(Filter.nullFilter(src1));
        System.out.println(Filter.nullFilter(src2));
        System.out.println(Filter.nullFilter(src3));
    }

}