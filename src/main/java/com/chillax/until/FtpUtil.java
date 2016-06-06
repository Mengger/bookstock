package com.chillax.until;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.chillax.until.ftp.FTPBean;

/**
 * ftp客户端
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: AI(NanJing)</p>
 * @author Yang Hua
 * @version 3.0
 */
public class FtpUtil {
  public static final String FILE_SPEARATROR =	"/";//add by lxm 08-10-27	文件分隔符,只适用于unix环境
  public static final int BIN = 0;
  public static final int ASC = 1;

  protected  FTPClient client = null;
  private String localPath = null;
  //add by lxm 08-10-27
  private String remotePathHis;//FTP文件历史目录,在cfg_ftp_path表remote_path_his字段配置


  private String _host = null;
  private int _port = 0;
  private String _username = null;
  private String _password = null;
  private String _remotePath = null;
  private String _localPath = null;
  private String _localPathTemp = null;
  private String _part = null;
  /**
   *
   * @param ftpPathCode String
   * @throws Exception
   */
  public FtpUtil(String ftpPathCode) throws Exception {
    ICfgFtpPathValue objICfgFtpPathValue = (ICfgFtpPathValue) CacheFactory.get(CfgFtpPathCacheImpl.class, ftpPathCode);
    ICfgFtpValue objICfgFtpValue = (ICfgFtpValue) CacheFactory.get(CfgFtpCacheImpl.class, objICfgFtpPathValue.getCfgFtpCode());
    client = new FTPClient();
    client.connect(objICfgFtpValue.getHostIp(),objICfgFtpValue.getPort());
    client.login(objICfgFtpValue.getUsername(), objICfgFtpValue.getPassword());
    client.changeWorkingDirectory(objICfgFtpPathValue.getRemotePath());
    localPath = objICfgFtpPathValue.getLocalPath();
    remotePathHis = objICfgFtpPathValue.getRemotePathHis();//add by lxm 08-10-27
    //add by huangyb
    _host = objICfgFtpValue.getHostIp();
    _port = objICfgFtpValue.getPort();
    _username = objICfgFtpValue.getUsername();
    _password = objICfgFtpValue.getPassword();
    _remotePath = objICfgFtpPathValue.getRemotePath();
    _localPath = objICfgFtpPathValue.getLocalPath();
    _localPathTemp = objICfgFtpPathValue.getLocalPathTemp();

  }
  
  public FtpUtil(String ftpPathCode,String part) throws Exception {
	    FTPBean objICfgFtpPathValue = (ICfgFtpPathValue) CacheFactory.get(CfgFtpPathCacheImpl.class, ftpPathCode);
	    if (StringUtils.isBlank(part)) {
	    	part = "IN_USE";
	    }
	    IBOCfgFtpPartValue partCfgFtp = (IBOCfgFtpPartValue) CacheFactory.get(CfgFtpPartCacheImpl.class, objICfgFtpPathValue.getCfgFtpCode()+"_"+part);
	    client = new FTPClient();
	    client.connect(partCfgFtp.getHostIp(),partCfgFtp.getPort());
	    client.login(partCfgFtp.getUsername(), partCfgFtp.getPassword());
	    client.changeWorkingDirectory(objICfgFtpPathValue.getRemotePath());
	    localPath = objICfgFtpPathValue.getLocalPath();
	    remotePathHis = objICfgFtpPathValue.getRemotePathHis();//add by lxm 08-10-27
	    //add by huangyb
	    _host = partCfgFtp.getHostIp();
	    _port = partCfgFtp.getPort();
	    _username = partCfgFtp.getUsername();
	    _password = partCfgFtp.getPassword();
	    _remotePath = objICfgFtpPathValue.getRemotePath();
	    _localPath = objICfgFtpPathValue.getLocalPath();
	    _localPathTemp = objICfgFtpPathValue.getLocalPathTemp();
	    _part = partCfgFtp.getPart();
  }
  
  public String getPart(){
	  return _part;
  }

  /**
   * 二进制格式
   * @throws Exception
   */
  public void bin() throws Exception{
    client.setFileType(FTP.BINARY_FILE_TYPE);
  }

  /**
   * ascii格式
   * @throws Exception
   */
  public void asc() throws Exception{
    client.setFileType(FTP.ASCII_FILE_TYPE);
  }

  /**
   * 列出服务器的目录下面的文件
   * @param encoding String
   * @throws Exception
   * @return String[]
   */
  public String[] list(String encoding) throws Exception {
    List list = new ArrayList();

    FTPFile[] ftpFiles = client.listFiles(client.printWorkingDirectory());

    for (int i = 0; i < ftpFiles.length; i++) {
      if (ftpFiles[i].isFile()) {
	list.add(new String(ftpFiles[i].getName().getBytes("ISO-8859-1"), encoding));
      }
    }

    return (String[]) list.toArray(new String[0]);
  }
  
  /**
	 * 根据pathName列出服务器的目录下的文件
	 * 
	 * @param pathName 
	 * 			文件名或文件路径
	 * @return
	 * 	返回FTPFile列表
	 * @throws Exception
	 */
	public FTPFile[] listFtpFiles (String pathName) throws Exception {
		return client.listFiles(pathName);
	}

  /**
   *
   * @throws Exception
   * @return String[]
   */
  public String[] list() throws Exception {
    return list("GBK");
  }

  /**
   *
   * @param remoteFileName String
   * @param input InputStream
   * @param mode int
   * @throws Exception
   */
  public void upload(String remoteFileName, InputStream input,int mode) throws Exception {
    if(mode == BIN){
      client.setFileType(FTP.BINARY_FILE_TYPE);
    }
    else if(mode == ASC){
      client.setFileType(FTP.ASCII_FILE_TYPE);
    }
    else{
      throw new Exception("不支持的传输模式:"+mode);
    }
    upload(remoteFileName,input);
  }

  /**
   *
   * @param remoteFileName String
   * @param input InputStream
   * @throws Exception
   */
  public void upload(String remoteFileName, InputStream input) throws Exception {
    client.storeFile(remoteFileName, input);
  }

  /**
   *
   * @param remoteFileName String
   * @param localFileName String
   * @throws Exception
   */
  public void upload(String remoteFileName, String localFileName) throws Exception {
    InputStream is = null;
    try {
      is = new BufferedInputStream(new FileInputStream(localPath + "/" + localFileName));
      client.storeFile(remoteFileName, is);
    }
    finally {
      if (is != null) {
	is.close();
      }
    }
  }

  /**
   *
   * @param remoteFileName String
   * @param localFileName String
   * @param mode int
   * @throws Exception
   */
  public void upload(String remoteFileName, String localFileName, int mode) throws Exception {
    if(mode == BIN){
      client.setFileType(FTP.BINARY_FILE_TYPE);
    }
    else if(mode == ASC){
      client.setFileType(FTP.ASCII_FILE_TYPE);
    }

    else {
      throw new Exception("不支持的传输模式:" + mode);
    }
    upload(remoteFileName, localFileName);
  }

  /**
   *
   * @param remoteFileName String
   * @param output OutputStream
   * @param mode int
   * @throws Exception
   */
  public void download(String remoteFileName, OutputStream output,int mode) throws Exception {
//    client.setControlEncoding("GBK");
    if(mode == BIN){
      client.setFileType(FTP.BINARY_FILE_TYPE);
    }
    else if(mode == ASC){
      client.setFileType(FTP.ASCII_FILE_TYPE);
    }

    else{
      throw new Exception("不支持的传输模式:"+mode);
    }
    download(remoteFileName,output);
  }


  /**
   *
   * @param remoteFileName String
   * @param output OutputStream
   * @throws Exception
   */
  public void download(String remoteFileName, OutputStream output) throws Exception {
      String filePath = null;
      String downFile = remoteFileName.trim();
    if(downFile.indexOf("\\")>=0 || downFile.indexOf("/")>=0){
        if(downFile.charAt(0)!='.'){
            downFile = "."+downFile;
        }
        if(downFile.indexOf("\\")>downFile.indexOf("/")){
            filePath = downFile.substring(0,downFile.lastIndexOf("\\"));
            downFile = downFile.substring(downFile.lastIndexOf("\\")+1,downFile.length());
        }else{
            filePath = downFile.substring(0,downFile.lastIndexOf("/"));
            downFile = downFile.substring(downFile.lastIndexOf("/")+1,downFile.length());
        }
    }
      //转码
      downFile =  new String(downFile.getBytes("GBK"),"iso-8859-1");

    if(null != filePath){
        //转码
        filePath =  new String(filePath.getBytes("GBK"),"iso-8859-1");

        this.changeWorkingDirectory(filePath);
    }

    boolean rtn = client.retrieveFile(downFile, output);
    if (rtn == false) {
      throw new Exception("下载远程文件:"+remoteFileName+",不成功");
    }
  }

  public void downloadSimple(String remoteFileName, OutputStream output) throws Exception {
      boolean rtn = client.retrieveFile(remoteFileName, output);
    if (rtn == false) {
      throw new Exception("下载远程文件:"+remoteFileName+",不成功");
    }
  }

  /**
   *
   * @param remoteFileName String
   * @param localFileName String
   * @param mode int
   * @throws Exception
   */
  public void download(String remoteFileName, String localFileName,int mode) throws Exception {
    if(mode == BIN){
      client.setFileType(FTP.BINARY_FILE_TYPE);
    }
    else if(mode == ASC){
      client.setFileType(FTP.ASCII_FILE_TYPE);
    }
    else{
      throw new Exception("不支持的传输模式:"+mode);
    }
    download(remoteFileName,localFileName);
  }

  /**
   *
   * @param remoteFileName String
   * @param localFileName String
   * @throws Exception
   */
  public void download(String remoteFileName, String localFileName) throws Exception {
    OutputStream os = null;
    try {
      os = new BufferedOutputStream(new FileOutputStream(localPath + "/" + localFileName));
      boolean rtn = client.retrieveFile(remoteFileName, os);
      if (rtn == false) {
	throw new Exception("下载远程文件:" + remoteFileName + ",不成功");
      }
    }
    finally {
      if (os != null) {
	os.close();
      }
    }
  }

  /**
   *
   * @param remoteFileName String
   * @throws Exception
   * @return InputStream
   */
  public InputStream readRemote(String remoteFileName) throws Exception {
    return client.retrieveFileStream(remoteFileName);
  }

  /**
   *
   * @param remoteFileName String
   * @param mode int
   * @throws Exception
   * @return InputStream
   */
  public InputStream readRemote(String remoteFileName, int mode) throws Exception {
    if(mode == BIN){
      client.setFileType(FTP.BINARY_FILE_TYPE);
    }
    else if(mode == ASC){
      client.setFileType(FTP.ASCII_FILE_TYPE);
    }
    else {
      throw new Exception("不支持的传输模式:" + mode);
    }
    return readRemote(remoteFileName);
  }

  /**
   *
   * @param oldRemoteFileName String
   * @param newRemoteFileName String
   * @throws Exception
   */
  public void rename(String oldRemoteFileName, String newRemoteFileName) throws Exception {
    client.rename(oldRemoteFileName, newRemoteFileName);
  }

  /**
   *
   * @param remoteFileName String
   * @throws Exception
   */
  public void delete(String remoteFileName) throws Exception {
    boolean rtn = client.deleteFile(remoteFileName);
    if (rtn == false) {
      throw new Exception("删除远程文件:" + remoteFileName + ",不成功");
    }
  }

  /**
   *
   * @throws Exception
   */
  public void completePendingCommand() throws Exception {
    client.completePendingCommand();
  }

  /**
   *
   * @throws Exception
   */
  public void close() throws Exception {
    if(client.isConnected()){
      client.disconnect();
    }
  }

  /**
   *
   * @throws Exception
   */
  public void reconnect() throws Exception {
    if (!client.isConnected()) {
      client = new FTPClient();
      client.connect(_host, _port);
      client.login(_username, _password);
      client.changeWorkingDirectory(_remotePath);

      localPath = _localPath;
    }
  }
  
  public boolean isConnected() throws Exception {
	  
	  return client.isConnected();
  }

  /**
   *
   * @throws Exception
   */
  public void forceReconnect() throws Exception {
    if (!client.isConnected()) {
      client.disconnect();
    }

    client = new FTPClient();
    client.connect(_host, _port);
    client.login(_username, _password);
    client.changeWorkingDirectory(_remotePath);

    localPath = _localPath;
  }

  /**
   * 获得FTP主机配置
   * @param cfgFtpCode String
   * @throws Exception
   * @return ICfgFtpValue
   */
  public static ICfgFtpValue getCfgFtp(String cfgFtpCode) throws Exception {
    return (ICfgFtpValue) (ICfgFtpValue) CacheFactory.get(CfgFtpCacheImpl.class, cfgFtpCode);
  }

  /**
   * 获得FTP文件路径配置
   * @param cfgFtpPathCode String
   * @throws Exception
   * @return ICfgFtpPathValue
   */
  public static ICfgFtpPathValue getCfgFtpPath(String cfgFtpPathCode) throws Exception {
    return (ICfgFtpPathValue) CacheFactory.get(CfgFtpPathCacheImpl.class, cfgFtpPathCode);
  }

  /**
   * 获得输入流
   * @param 
   * @throws Exception
   * @author 栾其进
   */
  public OutputStream getOutputStream(String fileName) throws Exception{
	  return client.storeFileStream(fileName);
  }

  /**将远程文件移动至远程历史目录
   *
   * @author 陆晓明(Mail:luxm@asiainfo.com MSN:lxm_js@hotmail.com)
   * @param fileName
   * @throws Exception
   */
  public void moveFileToRemoteHisDir(String fileName) throws Exception{
	  if(client.listFiles(fileName).length == 0){
		  throw new AIException("在远程服务器上未找到文件"+fileName+",文件移动至历史目录失败");
	  }
	  StringBuffer newFileName = new StringBuffer();
	  newFileName.append(getRemotePathHis());
	  newFileName.append(FILE_SPEARATROR);
	  newFileName.append(fileName);
	  rename(fileName, newFileName.toString());
  }

  /**获取远程文件历史目录
   *
   * @author 
   * @return
   * @throws com.ai.appframe2.common.AIException
   */
  public String getRemotePathHis () throws AIException{
	  if(remotePathHis == null || remotePathHis.length()<1){
		  throw new AIException("未配置远程文件历史目录");
	  }
	  return remotePathHis;
  }

  //add by 黄游槟 2008-11-19
  public String getWorkingDirectory() throws Exception{
	  if(this.client != null){
		  return this.client.printWorkingDirectory();
	  }
	  return null;
  }

  public void changeWorkingDirectory(String pathName) throws Exception{
	  if(this.client != null){
		  this.client.changeWorkingDirectory(pathName);
	  }
  }

  //add by 黄游槟 2009-2-4
  public String getLocalPath(){
	 return this._localPath;
  }

  public String getLocalPathTemp(){
	  return this._localPathTemp;
  }

  public String getRemotePath(){
	  return _remotePath;
  }

  public static void main(String[] args)  throws Exception {
    FtpUtil f = new FtpUtil("CS_AFFICHE");
    String[] str = f.list();
    for (int i = 0; i < str.length; i++) {
      System.out.println(str[i]);
    }
/*
    FileInputStream in = new FileInputStream(new File("d:\\audio.txt"));
    f.upload("audio.txt",in,ASC);
*/
/*
      String str2 = new String("电视剧");
      f.client.makeDirectory(new String(str2.getBytes("GBK"),"iso-8859-1"));
*/

      FileOutputStream out = new FileOutputStream("d:\\高人.txt");
      f.download("电视/move/的/高人.txt",out,ASC);


//    f.download("c.txt",out,BIN);

//    f.delete("ba.txt");
//    f.upload("b","b");
//    f.completePendingCommand();

/*
    for (int i = 0; i < 1; i++) {
      InputStream is = f.readRemote("b");
      int b = 0;
      while ( (b = is.read()) != -1) {
	//System.out.println(b);
      }
      f.completePendingCommand();
    }
*/
//    f.download("b", "b");
//    f.delete("b");

//    ByteArrayOutputStream a = new ByteArrayOutputStream();
//    f.download("weblogic.jar",a);
//    System.out.println(a.toByteArray().length);
//    byte[] bytes = new byte[1024];
//    int c;
//    while ( (c = is.read(bytes)) != -1) {
//      a.write(bytes, 0, c);
//    }
//
//    a.flush();
//
//    System.out.println(new String(a.toByteArray()));
//
    f.close();
//    System.out.println("耗时:"+(System.currentTimeMillis()-start)+":ms");
  }
  /**
   * add by xingnn
   * 向已有文件里面追加数据
   * @param remoteName
   * @param in
   * @return
   * @throws Exception
   */
  public boolean appendFile(String remoteName,InputStream in)  throws Exception{
	   return client.appendFile(remoteName, in);
  }

    /**
     * 切换到指定目录(不存在就创建目录同时切换到新建的目录)
     * @param directoryId
     * @throws Exception
     */
    public void changeToRightDir(String directoryId) throws Exception {
        if (client != null) {
            String ftpDir = client.printWorkingDirectory();
            if(ftpDir==null)
            	throw new Exception("FTP当前工作目录为NULL");
            String curDir = ftpDir + FILE_SPEARATROR + directoryId;
            boolean flag = client.changeWorkingDirectory(curDir);
            if (flag == false) {
                //不存在就创建目录同时切换到新建的目录
                client.makeDirectory(directoryId);
                client.changeWorkingDirectory(curDir);
            }
        }
    }
    
    /**
     * 切换到remote目录下
     * @throws Exception
     */
    public void changeToRemoteDir() throws Exception {
        if (client != null) {
            String ftpDir = client.printWorkingDirectory();//获取ftp当前工作目录
            if(ftpDir==null)
            	throw new Exception("FTP当前工作目录为NULL");
            client.changeWorkingDirectory(_remotePath);//切换到指定目录,为null时切换到ftp根目录
            
        }
    }
    
	 /**
     * 根据路径创建目录,并会把工作目录自动转换到最后子目录
     * @param ftpClient
     * @param path 参数类型 ：aaa/bbb/ccc
     * @return
     * @throws IOException
     *
     * @author 郭书培
     * 2014.10.24
     */
    public void changeDir(String path) throws Exception{
    	if (client != null) {
	        System.out.println("changeDir: "+path);
	        if(path == null|| path == "" )
	        {
	        	throw new Exception("FTP的要转换的工作目录为NULL");
	        }
	        
	        String[] paths = path.split("/");
	        for (int i = 0; i < paths.length; i++)
	        {
	        	String ftpDir = client.printWorkingDirectory();//获取ftp当前工作目录
	        	//System.out.println("workDir:"+ftpDir);
	        	String curDir = ftpDir + FILE_SPEARATROR + paths[i];
	        	System.out.println("crateDir: "+curDir);
	        	client.makeDirectory(new String(paths[i].getBytes("UTF-8"),"iso-8859-1"));//创建指定目录
	        	client.changeWorkingDirectory(curDir);//切换到指定目录
	        }
    	}
    }
    
    /**
     * 根据路径创建目录
     * @param ftpClient
     * @param path 参数类型 ：aaa/bbb/ccc
     * @return
     * @throws IOException
     *
     */
    public void makeDir(String path) throws Exception{
    	
    	if (client != null) {
	       
	        String[] paths = path.split("/");
	        for (int i = 0; i < paths.length; i++)
	        {
	        	String ftpDir = client.printWorkingDirectory();//获取ftp当前工作目录
	        	String curDir = ftpDir + FILE_SPEARATROR + paths[i];
	        	client.makeDirectory(new String(paths[i].getBytes("UTF-8"),"iso-8859-1"));//创建指定目录
	        }
    	}
    }
    
    
    
    public static String getPicStrFromPath(String ftpPathCode,String url) throws Exception{
		InputStream is = null;
		FtpUtil ftpUtil = null;
		String isStr = "";
		try {
			if (StringUtils.contains(url, "FTP")) {
				ftpUtil = new FtpUtil(ftpPathCode,StringUtils.substring(url,0, StringUtils.indexOf(url, "FTP")));
				url = StringUtils.substring(url, StringUtils.indexOf(url, "FTP")+3);
			}else{
				ftpUtil = new FtpUtil(ftpPathCode);
			}
			is = ftpUtil.readRemote(url);
			if(null==is){
				//如果得到的is为空，则去历史转移的ftp节点去获取
				if(ftpUtil!=null) ftpUtil.close();
				ftpUtil = new FtpUtil(ftpPathCode,"1");
				is = ftpUtil.readRemote(url);
			}
			if (null!=is) {
				isStr = IOUtils.toString(is, "ISO8859-1");
			}
			if (null!=is) {
				is.close();
				if (null!=ftpUtil) {
					ftpUtil.completePendingCommand();
				}
			}
			if (null!=ftpUtil) {
				ftpUtil.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if (null!=is) {
				is.close();
				if (null!=ftpUtil) {
					ftpUtil.completePendingCommand();
				}
			}
			if (null!=ftpUtil) {
				ftpUtil.close();
			}
		}
		return isStr;
	}
    
}
