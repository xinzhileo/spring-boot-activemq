package org.tongwoo.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.sql.SQLException;
import java.util.concurrent.*;

import javax.jms.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.tongwoo.util.ConcurrentQueue;
import org.tongwoo.util.Configuration;
import org.tongwoo.util.MyJsonObject;


@Component
public class ComsumeDataRunning implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ComsumeDataRunning.class);
	private ExecutorService staticThreadPool = Executors.newFixedThreadPool(8);
	private ExecutorService PositionThreadPool = Executors.newFixedThreadPool(8);
    public static CountDownLatch LATCH ;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
	@Autowired
    private ConnectionFactory connectionFactory;

    @Override
    public void run(String... args) throws JMSException {
    	//init staticConsumer process date of thread
		ConcurrentQueue staticQueue = new ConcurrentQueue();
		StaticConsumer.setQueue(staticQueue);
		ProcessStaticRunning processStatic = new ProcessStaticRunning();
		processStatic.setJdbcTemplate(jdbcTemplate);
		processStatic.setQueue(staticQueue);
		// create thread run processStatic class
		for(int i = 0; i< Configuration.getIntegerValue("static.queue.Concurrency"); i++) staticThreadPool.execute(processStatic);

    	//init positionConsumer process date of thread
		ConcurrentQueue positionQueue = new ConcurrentQueue();
		PositionConsumer.setQueue(positionQueue);
		for(int i=0;i<Configuration.getIntegerValue("position.queue.Concurrency");i++) {
			ProcessPositionRunable processPosition = new ProcessPositionRunable();
			processPosition.setQueue(positionQueue);
			processPosition.setVehicOperat(mongoTemplate.bulkOps(BulkMode.UNORDERED, "tb_positionVehicle"));
			processPosition.setDriverOperat(mongoTemplate.bulkOps(BulkMode.UNORDERED, "tb_positionDriver"));
			PositionThreadPool.execute(processPosition);
		}
    	System.out.println("app is running");
		LATCH.countDown();

    	Runtime run = Runtime.getRuntime();
    	//application life cycle will end,so we should close mongo bulk and mysql batch
//    	run.addShutdownHook(new Thread(){
//    		public void run(){
////    			StaticConsumer.commit();
//    			processPosition.vehicExecute(0);
//				processPosition.driverExecute(0);
//    		}
//    	});


    }


    static class StaticConsumer implements MessageListener{
        public static CountDownLatch LATCH ;
		private static ConcurrentQueue queue;

    	//@JmsListener(destination="DiDi_Static_Data")
    	public void receiveQueue(String text) throws SQLException{
    	}
		@Override
		public void onMessage(Message message) {
			if(LATCH==null) try {LATCH.await();} catch (InterruptedException e) {e.printStackTrace();}
//			TextMessage text = (TextMessage) message;
//			MyJsonObject obj = null;
//			try{ obj = new MyJsonObject(JSON.parseObject(text.getText())); }catch (JMSException e) {LOG.warn("position data parse exception!");}
//			queue.offer(obj);
		}

		public static void setQueue(ConcurrentQueue queue){
			StaticConsumer.queue = queue;
		}
	}
    
    static class PositionConsumer implements MessageListener{
		private ExecutorService threadPool = Executors.newFixedThreadPool(8);
        public static CountDownLatch LATCH ;
		private static ConcurrentQueue queue;

    	public PositionConsumer(){
		}
		@Override
		public void onMessage(Message message) {
			if(LATCH==null) try {LATCH.await();} catch (InterruptedException e) {e.printStackTrace();}
//			threadPool.execute(new Runnable() {
//				@Override
//				public void run() {
//					TextMessage text = (TextMessage) message;
//					JSONObject obj = null;
//					try{ obj = JSON.parseObject(text.getText()); }catch (JMSException e) {LOG.warn("position data parse exception!");}
//					queue.offer(obj);
//				}
//			});
		}

		public static void setQueue(ConcurrentQueue queue){
			PositionConsumer.queue = queue;
		}
	}
}
