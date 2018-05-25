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
        String path = "/zk-data";

        if (zkClient.exists(path)) {
            zkClient.delete(path);
        }

        zkClient.createPersistent(path);

        zkClient.writeData(path, "test_data_1");

        System.out.println(zkClient.readData(path).toString());

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.printf("-----------------handleDataChange path:%s data:%s \n", s, JSON.toJSON(o));
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.printf("=================handleDataDeleted path:%s \n", s);
            }
        });

        zkClient.writeData(path, "data2");
        zkClient.writeData(path, "data3");
        zkClient.writeData(path, "data4");
        zkClient.writeData(path, "data5");
        TimeUnit.SECONDS.sleep(1);
        zkClient.delete(path);

        TimeUnit.SECONDS.sleep(5);
    }
}
