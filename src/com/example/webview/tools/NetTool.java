package com.example.webview.tools;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class NetTool {
	 /** 
     *  
     * @param urlPath ����·�� 
     * @param params Map��keyΪ���������valueΪ���������ֵ 
     * @param encoding  ���뷽ʽ 
     * @return 
     * @throws Exception 
     */  
      
    //ͨ��post��������˷������ݣ�����÷������������  
    public static InputStream getInputStreamByPost(String urlPath,Map<String,String> params,String encoding) {  
        StringBuffer sb = new StringBuffer();  
        for(Map.Entry<String,String> entry:params.entrySet()){  
            try {
				sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encoding));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            sb.append("&");  
        }  
        String data = sb.deleteCharAt(sb.length()-1).toString();  
        URL url = null;
		try {
			url = new URL(urlPath);
        //������  
        HttpURLConnection conn = null;
			conn = (HttpURLConnection)url.openConnection();
        //�����ύ��ʽ  
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
			conn.setRequestMethod("POST");
        //post��ʽ����ʹ�û���  
        conn.setUseCaches(false);  
        conn.setInstanceFollowRedirects(true);  
        //�������ӳ�ʱʱ��  
        conn.setConnectTimeout(2*1000);  
        //���ñ������ӵ�Content-Type������Ϊapplication/x-www-form-urlencoded  
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
        //ά�ֳ�����  
        conn.setRequestProperty("Connection", "Keep-Alive");  
        //�������������  
        conn.setRequestProperty("Charset", "UTF-8");  
        DataOutputStream dos = null;
			dos = new DataOutputStream(conn.getOutputStream());
        //���������������������˷���  
			dos.writeBytes(data);
       
			dos.flush();
	
			dos.close();
		
			if(conn.getResponseCode() == 200){  
			    //��÷������������  
			    return conn.getInputStream();  
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;  
    }  
      
    //ͨ������������ֽ�����  
    public static byte[] readStream(InputStream is) throws Exception {  
        byte[] buffer = new byte[1024];  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        int len = 0;  
        while((len=is.read(buffer)) != -1){  
            bos.write(buffer, 0, len);  
        }   
        is.close();  
        return bos.toByteArray();  
    }  
}
