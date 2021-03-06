package com.lvb.baseApi.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdWorker {

    private final static Logger logger = LoggerFactory.getLogger(IdWorker.class);
    private final long datacenterId;
    private final long workerId;
    //private final long epoch = 1403854494756L;   // 时间起始标记点，作为基准，一般取系统的最近时间
    private final long epoch = 1288834974657L;   // 时间起始标记点，作为基准，一般取系统的最近时间
    private final long datacenterIdBits = 5L;    //数据中心标识位数
    private final long workerIdBits = 5L;      // 机器标识位数
    private final long maxDatacenterId = -1L ^ -1L << this.datacenterIdBits;// 数据中心ID最大值: 32
    private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;// 机器ID最大值: 32
    private long sequence = 0L;                   // 0，并发控制
    private final long sequenceBits = 12L;      //毫秒内自增位

    private final long workerIdShift = this.sequenceBits;                             // 12
    private final long datacenterIdShift = this.sequenceBits + this.workerIdBits;//17
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits + this.datacenterIdBits;// 22
    private final long sequenceMask = -1L ^ -1L << this.sequenceBits;                 // 4095,111111111111,12位
    private long lastTimestamp = -1L;

    private IdWorker(long workerId, long datacenterId) {
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        if (datacenterId > this.maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", this.maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId(){
        long timestamp = IdWorker.timeGen();
        if (this.lastTimestamp == timestamp) { // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环); 对新的timestamp，sequence从0开始
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);// 重新生成timestamp
            }
        } else {
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            logger.error(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
//            throw new Exception(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
        }

        this.lastTimestamp = timestamp;
        long cId = timestamp - this.epoch << this.timestampLeftShift | this.datacenterId << this.datacenterIdShift | this.workerId << this.workerIdShift | this.sequence;

        return Long.parseLong(String.valueOf(cId).substring(String.valueOf(cId).length()-16,String.valueOf(cId).length()));
    }

    private static IdWorker flowIdWorker = new IdWorker(1, 5);

    public static IdWorker getFlowIdWorkerInstance() {
        return flowIdWorker;
    }


    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = IdWorker.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = IdWorker.timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

//    public static void main(String[] args) throws Exception {
//        System.out.println(timeGen());
//
//        IdWorker idWorker = IdWorker.getFlowIdWorkerInstance();
//        // System.out.println(Long.toBinaryString(idWorker.nextId()));
//        System.out.println(idWorker.nextId());
//        System.out.println(idWorker.nextId());
//    }
}
