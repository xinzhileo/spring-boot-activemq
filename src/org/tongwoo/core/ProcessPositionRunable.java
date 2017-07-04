package org.tongwoo.core;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.tongwoo.util.ConcurrentQueue;

import java.util.concurrent.ConcurrentHashMap;

public class ProcessPositionRunable implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessPositionRunable.class);

//    private static MongoTemplate mongoTemplate;
    private ConcurrentHashMap<String, BulkOperations> bulkMap = new ConcurrentHashMap();
//    private BulkOperations vehicOperat ;
//    private BulkOperations driverOperat ;
    private ConcurrentQueue queue;
    private static volatile Integer vehiCount = 0,driverCount = 0;

    public void run() {
        while(true){
            //receive date from queue
            JSONObject obj = (JSONObject) queue.take();
            if(obj==null) continue;
            if(!obj.containsKey("InfoFlag"))
                LOG.info("无法判断是什么数据：" + obj.toJSONString());
            else {
//                LOG.info("receive position data: "+ obj.toJSONString());
                String strInfoFlag = obj.getString("InfoFlag");
                //add data to bulk if count already 10000 do execute
                if(strInfoFlag.equals("positionVehicle")) { this.bulkMap.get("vehicOperat").insert(obj); vehiCount++; }
                else if(strInfoFlag.equals("positionDriver")) { this.bulkMap.get("driverOperat").insert(obj); driverCount++; }
                vehicExecute(10000);
                driverExecute(10000);
            }
        }
    }

    //write to volatile modifier variable operation at before next read
    public void vehicExecute(int  i){
        if(vehiCount<i)
            return;
        this.bulkMap.get("vehicOperat").execute();
        vehiCount = 0;
        LOG.info("vehicle position data count have arrived 10000 so commit");
    }
    public void driverExecute(int i) {
        if(driverCount<i)
            return;
        this.bulkMap.get("driverOperat").execute();
        driverCount = 0;
        LOG.info("driver position data count have arrived 10000 so commit");
    }

    public void setQueue(ConcurrentQueue queue){
        this.queue = queue;
    }

    public BulkOperations getVehicOperat() {
        return this.bulkMap.get("vehicOperat");
    }

    public void setVehicOperat(BulkOperations vehicOperat) {
        this.bulkMap.put("vehicOperat", vehicOperat);
    }

    public BulkOperations getDriverOperat() {
        return this.bulkMap.get("driverOperat");
    }

    public void setDriverOperat(BulkOperations driverOperat) {
        this.bulkMap.put("driverOperat",driverOperat);
    }
}
