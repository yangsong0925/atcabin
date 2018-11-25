import org.apache.commons.lang3.RandomUtils;

public class Tests {

    public static void main(String[] args){
        String name = "杨松";
        String code = "11231";
        System.out.println("{\"name\":"+"\""+name+"\",\"code\":" + "\"" + code + "\"}");
        System.out.println(RandomUtils.nextInt(100000,999999));
    }

}
