package kl.springboot.demo.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {


    // 不带秘钥加密p
    public static String md5(String text) throws Exception {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text);
        System.out.println("MD52加密后的字符串为:" + md5str + "\t长度：" + md5str.length());
        return md5str;
    }

    /**
     * MD5验证方法
     *
     * @param text明文
     * @param key密钥
     * @param md5密文
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text,  String md5) throws Exception {
        String md5str = md5(text);
        if (md5str.equalsIgnoreCase(md5)) {
            System.out.println("MD5验证通过");
            return true;
        }
        return false;
    }

    // 测试
    public static void main(String[] args) throws Exception {
        // String str =
        // "181115.041650.10.88.168.148.2665.2419425653_1";181115.040908.10.88.181.118.3013.1655327821_1
        String str = "181115.040908.10.88.181.118.3013.1655327821_1";
        System.out.println("加密的字符串：" + str + "\t长度：" + str.length());
        MD5.md5(str);
        verify("123456","e10adc3949ba59abbe56e057f20f883e");
    }
}
