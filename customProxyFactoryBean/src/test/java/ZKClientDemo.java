import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ym on 2018/5/25.
 */
public class ZKClientDemo {

    public static void main(String[] args) throws InterruptedException {
        String zkServers = "127.0.0.1:2181";
        int timeout = 300;
        ZkClient zkClient = new ZkClient(zkServers, timeout);
        String path = "/zk-data1/node1/node11";

        zkClient.createPersistent(path, true);


    }
}
