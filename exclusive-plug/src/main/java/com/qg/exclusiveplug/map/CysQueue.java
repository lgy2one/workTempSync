package com.qg.exclusiveplug.map;

public class CysQueue {
    private Integer maxSize = 0; //循环队列最大长度为7  0~6
    private Double[] arr;
    private Integer front = 0;//头指针，若队列不为空，指向队头元素
    private Integer rear = 0; //尾指针，若队列不为空，指向队列尾元素的下一个位置

    public CysQueue(int maxSize) {
        this.maxSize = maxSize;
        arr = new Double[maxSize];
    }

    public Boolean isEmpty() {
        if (front.equals(rear)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Integer queueLength() {
        return (rear - front + maxSize) % maxSize; //求环形队列的元素个数
    }

    public Double getHead() {
        return arr[front];
    }

    //入队前判满
    public Double enQueue(Double e) {
        Double result = null;
        //队列头指针在队尾指针的下一位位置上  说明满了
        if ((rear + 1) % maxSize == front) {
            result = arr[front % maxSize];
            front = (front + 1) % maxSize;
        }
        rear = (rear + 1) % maxSize;
        arr[rear] = e;
        return result;
    }

    public static void main(String[] args) {
        CysQueue queue = new CysQueue(10);
        for (int i = 0; i < 100; i++) {
            queue.enQueue(i * 1.0);
            System.out.println(queue.getMinValue());
        }
    }

    @Deprecated
    //出队前判空
    public Double DeQueue() {
        if (rear.equals(front)) {
            return null;
        }
        Double e = (Double) arr[front];
        front = (front + 1) % maxSize;
        return e;
    }

    public Double getAvgValue() {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < maxSize; i++) {
            Double num = arr[i];
            if (null != num) {
                sum += arr[i];
                count ++;
            }
        }
        return sum / count;
    }

    public Double getMaxValue() {
        double max = 0;
        for (int i = 0; i < maxSize; i++) {
            if (null != arr[i] && arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public Double getMinValue() {
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < maxSize; i++) {
            if (null != arr[i] && arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }
}
