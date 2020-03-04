/**
 * @author Climb.Xu
 * @date 2020/2/27 0:51
 */
public class test {
    public static void main(String[] args) {
        String s = "create_time";
        String s1 = s.replaceAll("(_)(.?)", "+");
        System.out.println(s+"\t"+s1);
    }
}
