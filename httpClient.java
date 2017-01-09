/**
 * Created by WLY on 2016/9/6.
 */
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

public class httpClient {
    public static CloseableHttpClient httpClient = null;
    public static HttpClientContext context = null;
    public static CookieStore cookieStore = null;
    public static RequestConfig requestConfig = null;

    public static void main(String[] args){
        System.out.println("Usage: httpClient uid passwd operation range [verbose]");
        System.out.println("\t\toperation: connect | disconnectall");
        System.out.println("\t\tGlobal: 1 | Free: 2");
        System.out.println("\t\tVerbose: true | false");
        String range = "2";
        boolean verbose = true;
        if(args.length > 3){
            range = args[3];
        }
        if(args.length > 4){
            verbose =Boolean.parseBoolean(args[4]);
        }
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
        requestConfig = RequestConfig.custom().setConnectTimeout(500000).setSocketTimeout(500000)
                .setConnectionRequestTimeout(500000).build();
        // 设置默认跳转以及存储cookie
        httpClient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = new HttpGet("https://its.pku.edu.cn:5428/ipgatewayofpku?uid="+args[0]+
                "&password="+args[1]+"&timeout=1&operation="+args[2]+"&range="+range);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet, context);
            HttpEntity myEntity = response.getEntity();
            String resString = EntityUtils.toString(myEntity);
            if(verbose){
                System.out.println(resString);
            }
            String usefulString[] = resString.split("<!--IPGWCLIENT_START ");
            if(usefulString.length >= 2){
                String info = usefulString[1];
                usefulString = info.split(" IPGWCLIENT_END-->");
                System.out.println(usefulString[0]);
            }else {
                System.out.println(resString);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
