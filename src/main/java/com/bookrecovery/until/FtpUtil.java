package com.bookrecovery.until;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.bookrecovery.until.ftp.FTPBean;
  
public class FtpUtil {  
	
    private FTPClient ftpClient;  
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;  
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;  
      
    /**
     * 利用FtpConfig进行服务器连接
     * @param ftpConfig 参数配置Bean类
     * @throws SocketException
     * @throws IOException
     */
    public void connectServer(FTPBean ftpConfig) throws SocketException,  
            IOException {  
        String server = ftpConfig.getHostIp();  
        int port = ftpConfig.getPort();  
        String user = ftpConfig.getUserName();  
        String password = ftpConfig.getPassword();  
        String location = ftpConfig.getRemotePath();  
        connectServer(server, port, user, password, location);  
    }  
      
    /**
     * 使用详细信息进行服务器连接
     * @param server：服务器地址名称
     * @param port：端口号
     * @param user：用户名
     * @param password：用户密码
     * @param path：转移到FTP服务器目录 
     * @throws SocketException
     * @throws IOException
     */
    public void connectServer(String server, int port, String user,  
            String password, String path) throws SocketException, IOException {  
        ftpClient = new FTPClient();  
        ftpClient.connect(server, port);  
        ftpClient.login(user, password);  
        if (path!=null&&path.length() != 0) {  
            ftpClient.changeWorkingDirectory(path);  
        }  
    	ftpClient.setBufferSize(1024);//设置上传缓存大小
    	ftpClient.setControlEncoding("UTF-8");//设置编码
    	ftpClient.setFileType(BINARY_FILE_TYPE);//设置文件类型
    }  
    
    /**
     * 设置传输文件类型:FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE  
     * 二进制文件或文本文件
     * @param fileType
     * @throws IOException
     */
    public void setFileType(int fileType) throws IOException {  
        ftpClient.setFileType(fileType);  
    }  
  
    /**
     * 关闭连接
     * @throws IOException
     */
    public void closeServer() throws IOException {  
        if (ftpClient!=null&&ftpClient.isConnected()) {  
        	ftpClient.logout();//退出FTP服务器 
            ftpClient.disconnect();//关闭FTP连接 
        }  
    }
    
    /**
     * 将ftp切换到对应的路径,如果该路径不存在,自动创建
     * @param path
     * @return
     * @throws IOException 
     */
    public boolean createPath(String path) throws IOException{
    	if(!StringUtils.isBlank(path)){
    		String[] folders=path.split("/");
    		boolean condition=true;
    		begin:
    			for(String folder:folders){
    				if(!StringUtils.isBlank(folder)){
    					createDirectory(folder);
    					condition=changeDirectory(folder);
    					if(!condition)break begin;
    				}
    			}
    		return condition;
    	}
    	return false;
    }
    
    /**
     * 上传文件到FTP
     * @param in 文件流
     * @param fileName ftp的文件名
     * @param path 路径
     * @return
     * @throws IOException
     */
    public boolean uploadFile(InputStream in,String fileName,String path) throws IOException{
    	boolean condition=false;
    	condition=createPath(path);
    	fileName=new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
    	if(condition) condition=uploadFile(in, fileName);
    	return condition;
    }
    
    /**
     * 转移到FTP服务器工作目录
     * @param path
     * @return
     * @throws IOException
     */
    public boolean changeDirectory(String path) throws IOException {  
        return ftpClient.changeWorkingDirectory(path);  
    }  
    
    /**
     * 在服务器上创建目录
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean createDirectory(String pathName) throws IOException {  
        return ftpClient.makeDirectory(pathName);  
    }  
    
    /**
     * 在服务器上删除目录
     * @param path
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path) throws IOException {  
        return ftpClient.removeDirectory(path);  
    }  
      
    /**
     * 删除所有文件和目录
     * @param path
     * @param isAll true:删除所有文件和目录
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path, boolean isAll)  
            throws IOException {  
          
        if (!isAll) {  
            return removeDirectory(path);  
        }  
  
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        if (ftpFileArr == null || ftpFileArr.length == 0) {  
            return removeDirectory(path);  
        }  
        //   
        for (FTPFile ftpFile : ftpFileArr) {  
            String name = ftpFile.getName();  
            if (ftpFile.isDirectory()) {  
            	System.out.println("* [sD]Delete subPath ["+path + "/" + name+"]");               
                removeDirectory(path + "/" + name, true);  
            } else if (ftpFile.isFile()) {  
            	System.out.println("* [sF]Delete file ["+path + "/" + name+"]");                          
                deleteFile(path + "/" + name);  
            } else if (ftpFile.isSymbolicLink()) {  
  
            } else if (ftpFile.isUnknown()) {  
  
            }  
        }  
        return ftpClient.removeDirectory(path);  
    }  
    
    /**
     * 检查目录在服务器上是否存在 true：存在  false：不存在
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existDirectory(String path) throws IOException {  
        boolean flag = false;  
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        for (FTPFile ftpFile : ftpFileArr) {  
            if (ftpFile.isDirectory()  
                    && ftpFile.getName().equalsIgnoreCase(path)) {  
                flag = true;  
                break;  
            }  
        }  
        return flag;  
    }  
  
    /**
     * 得到文件列表,listFiles返回包含目录和文件，它返回的是一个FTPFile数组
     * listNames()：只包含目录的字符串数组
     * String[] fileNameArr = ftpClient.listNames(path); 
     * @param path:服务器上的文件目录:/DF4
     */
    public List<String> getFileList(String path) throws IOException {  
        FTPFile[] ftpFiles= ftpClient.listFiles(path);  
        //通过FTPFileFilter遍历只获得文件
/*      FTPFile[] ftpFiles2= ftpClient.listFiles(path,new FTPFileFilter() {
      @Override
      public boolean accept(FTPFile ftpFile) {
        return ftpFile.isFile();
      }
    });  */
        List<String> retList = new ArrayList<String>();  
        if (ftpFiles == null || ftpFiles.length == 0) {  
            return retList;  
        }  
        for (FTPFile ftpFile : ftpFiles) {  
            if (ftpFile.isFile()) {  
                retList.add(ftpFile.getName());  
            }  
        }  
        return retList;  
    }  
  
    /**
     * 删除服务器上的文件
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String pathName) throws IOException {  
        return ftpClient.deleteFile(pathName);  
    }  
  
    /**
     * 上传文件到ftp服务器
     * 在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * localFilePath:本地文件路径和名称
     * remoteFileName:服务器文件名称
     */
    public boolean uploadFile(String localFilePath, String remoteFileName)  
            throws IOException {  
        boolean flag = false;  
        InputStream iStream = null;  
        try {  
            iStream = new FileInputStream(localFilePath);  
            //我们可以使用BufferedInputStream进行封装
            //BufferedInputStream bis=new BufferedInputStream(iStream);
            //flag = ftpClient.storeFile(remoteFileName, bis); 
            flag = ftpClient.storeFile(remoteFileName, iStream);  
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            if (iStream != null) {  
                iStream.close();  
            }  
        }  
        return flag;  
    }  
  
    /**
     * 上传文件到ftp服务器，上传新的文件名称和原名称一样
     * @param fileName：文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String fileName) throws IOException {  
        return uploadFile(fileName, fileName);  
    }  
  
    /**
     * 上传文件到ftp服务器
     * @param iStream 输入流
     * @param newName 新文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(InputStream iStream, String newName)  
            throws IOException {  
        boolean flag = false;  
        try {  
            flag = ftpClient.storeFile(newName, iStream);  
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            if (iStream != null) {  
                iStream.close();  
            }  
        }  
        return flag;  
    }  
  
    /**
     * 从ftp服务器上下载文件到本地
     * @param remoteFileName：ftp服务器上文件名称
     * @param localFileName：本地文件名称
     * @return
     * @throws IOException
     */
    public boolean download(String remoteFileName, String localFileName)  
            throws IOException {  
        boolean flag = false;  
        File outfile = new File(localFileName);  
        OutputStream oStream = null;  
        try {  
            oStream = new FileOutputStream(outfile);  
            //我们可以使用BufferedOutputStream进行封装
         	//BufferedOutputStream bos=new BufferedOutputStream(oStream);
         	//flag = ftpClient.retrieveFile(remoteFileName, bos); 
            flag = ftpClient.retrieveFile(remoteFileName, oStream);  
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            oStream.close();  
        }  
        return flag;  
    }  
      
    /**
     * 从ftp服务器上下载文件到本地
     * @param sourceFileName：服务器资源文件名称
     * @return InputStream 输入流
     * @throws IOException
     */
    public InputStream downFile(String sourceFileName) throws IOException {  
        return ftpClient.retrieveFileStream(sourceFileName);  
    }  
    
    /**
     * 从FTP上复制文件流到os中
     * @param remoteFileName
     * @param os
     * @return
     */
    public boolean copyStreamFromFTP(String remoteFileName,OutputStream os){
    	boolean flag=false;
    	int index=remoteFileName.lastIndexOf("/");
    	String path=remoteFileName.substring(0,index);
    	boolean condition=false;
    	remoteFileName=remoteFileName.substring(index+1);
    	 try {  
    		 condition=createPath(path);
    		 if(condition){
    			 flag = ftpClient.retrieveFile(remoteFileName, os);  
    		 }
         } catch (IOException e) {  
             flag = false;  
             return flag;  
         } finally {  
             try {
            	os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
         }  
         return flag; 
    }
}