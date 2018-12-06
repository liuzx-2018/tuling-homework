package com.jiagouedu.lock;/*
 * ━━━━━━如来保佑━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━永无BUG━━━━━━
 * 图灵学院-悟空老师
 * www.jiagouedu.com
 * 悟空老师QQ：245553999
 */

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/***
 * 唯一特性 重复获取
 */
public class WkLock {

    private ZooKeeper zookeeper;
    private String path = "/tl";
    private CountDownLatch latch=null;
    public WkLock(String host, String path) {
        try {
            this.zookeeper =new ZooKeeper(host, 3000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }

    public void lock() {
        try {
            //Stat stat=zookeeper.exists(path,false);
            //System.out.println(stat);
                zookeeper.create(path, path.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {

           this.latch = new CountDownLatch(1);
            try {
                this.latch.await(1000, TimeUnit.MILLISECONDS);//等待，这里应该一直等待其他线程释放锁
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } this.latch = null;
            lock();
        }


    }

    public void unlock() {
        try {
            zookeeper.delete(path, -1);
        } catch (Exception e) {
        }
    }
}
