package com.wolf.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016082000290530";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCT6q8MYIda7T0pLW7ANGNF/PDlhDmZUGWt/NKAWayeALXd7SHMxmw0Ur5fNPXSUfpC8Ko4Atg03dW0twcOrPF5D/KtZkfmKvUpJlot7KzTGXTHraYrAYCWkEXsUmYPNSdkkYrmt1//icKdCNyAVlfTmA/t4wabEi/Q5e2XOPLewx27fDVNxC1lJRtHELyeOYVifqSyWQrwVWOAh3yIer6RZiHmy248FDULb+b5uHakQRIKW3LaBeU3wsB7+OzYqunfJM2ah4lGj+ubsmOIeGrWaDWJWcq19WHgpI/kG2nHmJg/TYWlzIjhDE95LnlTj5N/A5McyiRtlr9+9xZ+piRrAgMBAAECggEAbzRWLFD6EiMgAzAz/m6kX/7dQUCaaSgneZoEzBgfDZ7tHA7mhWR7/w5iL4PKeDfHlcIHxxpkX8KTo0yjEqZ02JZj1Xd7AJE1RZ2e2L4B5KdTw7dKoTR87zVCTHxaXH6SwiolqnUcyCpS1OrPk/DWEK9W8nrnAXi32C605CcdeNk7V+y47N0exlKOWNfklzqhjzh6YI8U+EDyPrzPQHNvkX6uRHc35Am8PiyfUds3BrVUoI7eoizUaymlOyjvSYfaoBpooIDlLA+cuvt3MAIMyuZKHg9pJvhKxwVMV2MX8m24L3pK4AP9TCPjRdzRYh5OF9O9GwjG4JziuMX71TBHMQKBgQD9O5vTaJ4ueX7pGWoH2MKpzwxd+kkmS9D31cYea9VWiO11jCakp6gtR9KT0DB4vyHxRrNy//nKED82hWHRejzBHZsj9FUvEXJYRFvcue8lUCc087JfjFnXJO/bAYR5jD1wA/joFi9ZI06d8Ui0seD/tegFbcNk6sIGsH0ZqxZSswKBgQCViHb4wtyE8VOR1IBOqJcf2/1DxytAUJXi1/qZUkSWrlGM60PWhrkFAXHZcv8UPgo1SGu3oBYULoMds0iaGJlAgYqdazo0cJUBmOBP2UzzLVv9K032UtljZ6oHBW9tm56a8U3b8d8icyOvGbyXKJ4HGCF3cR1G/6upzuTJ0vFjaQKBgCf1EOhx2IvSxfC3/uAQFt8+eWAWzxYI3odMpv9D2pzV7Eog/DP89G94psSGFC/pUMzGcChUNbDpkV/j+gRYul9qcYFh21Ma+KTu3GPevtpvZFBZp6ql9cMZ4/L1sVaQh102cz8DySLSjGQln2hLrQoMRPkeUbeJx2XQ0VyMf3A7AoGAYJy9+XW1ErP1QahhifDFS5FQLkV1vWUTom/W+s67f/t6tyieOPHsgr5vN4ZkJkj7QXfcGNT2zu1KS5s1UlKIWbQYwiKwhYcjztdePljwrcFlyMn84iSfTemBYm3xk4jTTdqQaoX4nAJ2UMC5J5TXnU/K96f+uYug9uK00W1YBtkCgYEAmQ9Hrsni6LIeM36AKIcdrghpYspLSzqIhxcvmpZXK9p2JNluId5nI3nMAOxFK1yrQfkC8E8jUqVHLTiwH0E7ahbBzqBGl0XSzx6i5d3KKpVnz/GPHq1KLDc/szC8NIkcvlFXZQFE/hBh9XwhxRezF3Kk26eg2mWycsgpJhHW+N4=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArWQ03214I6kGAbEo1stqSp522+0JomL5YndJFyiTXYDGyF1OU8HWcq0hoUSX1Yjf+Pd8MNsOugpW9qYJVExbqovRwzRx+6pQMMRJFeL+SaK0UL8lxfSJyNMJdxqv95cSTTAAZguhOWMk4Rpj/WNl4lWQ8pSOWgfZfboLas3rWPVTAAvBzLaCrCYdBlhfNsUUKJHPFfwUD8CeU/iPNJkHHbypaB5M4HXZ3vpUsS36nPTn9SY98x5L8Vywr+jKLOHiLZJDOFFcNlykaKTQRMAIFJjMpiwH2Wmvrjqax/0TIHqrzhAYEMfh6VRsjfY5Fi2PYzdfgKn55XeNAOolFP6QSQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://itmayiedu.tunnel.qydev.com/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://itmayiedu.tunnel.qydev.com/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}