import com.aliyuncs.exceptions.ClientException;
import com.atc.common.utils.Md5Helper;

public class Tests {

    public static void main(String[] args) throws ClientException {
//        String code = String.valueOf(RandomUtils.nextInt(100000,1000000));
//        System.out.println(code);
//        SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendMsg("13980152803", code);
//        String successCode = "OK";
//        if(sendSmsResponse.getCode() == null && !successCode.equals(sendSmsResponse.getCode())) {
//            System.out.println(ResultUtil.error("短信发送失败!"));
//        }
//        System.out.println(sendSmsResponse.getCode());
//        String option = "(1234)xy=1.111111 2.222222";
//        String randomCode = option.substring(1, 5);
//        String substring = option.substring(option.indexOf('=')+1);
//        String x = substring.substring(0,substring.indexOf(' '));
//        String y = substring.substring(substring.indexOf(' ')+1);
        System.out.println(Md5Helper.MD5("admin"));
    }

}
