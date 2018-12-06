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

public class WukongLockTest implements  Runnable{

    WkLock wkLock=new WkLock("192.168.0.31:2181,192.168.0.32:2181,192.168.0.33:2181","/wklock");
     static int i=0;

    public static void main(String[] args) throws InterruptedException {

        WukongLockTest lockTest2=new WukongLockTest();
        Thread t1= new Thread(lockTest2);
        Thread t2=  new Thread(lockTest2);
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println(i);

    }



        @Override
        public void run() {
            try {
                for(int j=0;j<300;j++){
                    wkLock.lock();
                    i++;
                    wkLock.unlock();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


    }
}
