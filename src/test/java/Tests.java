import com.aliyuncs.exceptions.ClientException;
import com.atc.common.utils.AliyunSmsUtils;
import org.apache.commons.lang3.RandomUtils;

public class Tests {

    public static void main(String[] args) throws ClientException {
        String code = String.valueOf(RandomUtils.nextInt(100000,1000000));
        AliyunSmsUtils.sendMsg("13980152803",code);
    }

}
