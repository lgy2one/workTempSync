package com.qg.exclusiveplug.web;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ClientTest {
    public static void main(String[] args) throws IOException {
        // 1、创建客户端的 Socket 服务
        Socket socket = new Socket("127.0.0.1", 8090);

        InputStream inputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\test.txt"));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        // 2、获取 Socket 流中输入流
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        while (null != (line = bufferedReader.readLine())) {
            log.info(line);
            // 3、使用输出流将指定的数据写出去
            bufferedWriter.write(line);
        }

        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(bufferedReader1.readLine());

        // 4、关闭 Socket 服务
        socket.close();
    }
}
