import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.atc.common.utils.AliyunSmsUtils;
import com.atc.common.utils.ResultUtil;
import org.apache.commons.lang3.RandomUtils;

public class Tests {

    public static void main(String[] args) throws ClientException {
        String code = String.valueOf(RandomUtils.nextInt(100000,1000000));
        System.out.println(code);
        SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendMsg("13980152803", code);
        String successCode = "OK";
        if(sendSmsResponse.getCode() == null && !successCode.equals(sendSmsResponse.getCode())) {
            System.out.println(ResultUtil.error("短信发送失败!"));
        }
        System.out.println(sendSmsResponse.getCode());
    }

}
