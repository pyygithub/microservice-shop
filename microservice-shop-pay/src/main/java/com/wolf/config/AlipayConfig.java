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
    public static String merchant_private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC+epLtPqiqYnlKIbO6qjqPOQZbUK7IRaV5cxohdwrMBPg9D0weXqOW/omfHGhfLHmm7pGoNUTjTh6fQRvLwZ177mcmv/Yff+rVs2AjXxjFYl5uyLHdwpDY4KyF4Xkvg9YqumxnJZUSJcB+XbGdeUUD2GhVW6EFaHWa+SifRfO4agxrvcC5J1bdfGR1rtMoeTDWvpz+p8Za+syKPKgMYQZtkjjhOYfBo5TMLwN0rK3KEoTlBF7aMoxzsyUMF/AQ33VOoMxUGuWf07QnIEru0vkcHSeiwxO9dl+PJdyEZctwoA7VYjNWeqrxHjjvyZeUpwYO6EeQq2YYUkWwrqB2BjFfAgMBAAECggEAZ0kt/Kl6ahHFtTX7MF/4Kwt/7+obLIvfg7CvXbAgUarsNKfj3Z/x0wGo/Hc5/UmvmZ5Jq01fUmZc4nw6p00QqVAH7RIwUrpZS5HdsrBxTS7fB/GZySckxgVmZ55wyRwS+M5n07KkV9fSvnLtyfclPkWNpaNu9/DBvMwYg+05kCg3IJKvf8tVrApUiKz3XNnfQgTLcLJyaWUo3EFMNzNWQFQvN7Mn7bRn5qMZO9CnWiidAZCetsNb+xJPTbMGsLqlz2tJbPLBiWSxPz4APDPUkTglgX7XHgUL4E1dQZ0K702LN/yVtkwNBqbMRUD4mEZZkiPYnw+pO6VA1ODfwP62AQKBgQD/K/MXSqgqNQG/Bb/jApff2jLBGwSlcgjSEFjljFd0/FzC1Ra7s025o3vIbPmAb3x9TDZqZoQIqaW95SXI/0eYpoQF2aGL1tbitf8A/6n/7aDvSQ8g6hrOygFrXhIiA0c46n8NxMXqYU1I8xpHpIdWsBFj3plNoHfnTgGW/0tWXwKBgQC/GN0nQ3DsC36nk6OHYpAbiugZiuqh3ZaGXOo/OPJat7kp3HzRZR3dNjUJOyoUylaVwS1UpeuRicOPy7GKDq4yaOYSMggeWIctFq9fUEJRXqFJEqoGSLLTTS8BgYnlHXCA35JUd2vXMrkmtNjhCMoy+siMaIaFR1xqK3ubf9UFAQKBgQCQviZjROcFIhI+gA1rwNHTmdsGbMQC8R+NwNYcR7JoWqpnniYKDNh+//w8IYyZzSOpk2yeiE5G96xbfjsju6KqNXJYOy4Gu7Qi36epslyFHw8WDLQ3MHRKbo3FkJPqlT92SpYNglvd6pnDUprKnspiYvdKVj9RqD+7hiUiuOfMMwKBgQClI9yCAMMGCUzzjh+kcKWuV5UJ9cWcCfz5sjPwXMr7YIfQSC1JW0O6lgyxJILSIqU+lwkQfK4dQFtS2alccCFTiNlB4iCn8MMoUNFO4fHdlZR1rH5oHc0D01CK0aIT8CP4CrzO58C24KumamR92/W4poZG6FFPvgJ78YXJNGgnAQKBgQDa7v8MqScu9q/WPYb7uTp3+cHODHu7QAMBiw6fcSKNxSibpkLNmrAPd+CZfQXiA+P3GOAOit9wi4Fy2C0bDVjwWSmDeoG8Xbq3VJmuZCT4Ub3Ojb/j956BJelxz2J6DcDlPcLNrkhUizuIyt+hygmKjAxHN8aRBtpf5w5dgNuGoQ==";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvnqS7T6oqmJ5SiGzuqo6jzkGW1CuyEWleXMaIXcKzAT4PQ9MHl6jlv6JnxxoXyx5pu6RqDVE404en0Eby8Gde+5nJr/2H3/q1bNgI18YxWJebsix3cKQ2OCsheF5L4PWKrpsZyWVEiXAfl2xnXlFA9hoVVuhBWh1mvkon0XzuGoMa73AuSdW3Xxkda7TKHkw1r6c/qfGWvrMijyoDGEGbZI44TmHwaOUzC8DdKytyhKE5QRe2jKMc7MlDBfwEN91TqDMVBrln9O0JyBK7tL5HB0nosMTvXZfjyXchGXLcKAO1WIzVnqq8R4478mXlKcGDuhHkKtmGFJFsK6gdgYxXwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://panyangyangoray.imwork.net/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://panyangyangoray.imwork.net/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "/Users/wolf/Desktop";


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