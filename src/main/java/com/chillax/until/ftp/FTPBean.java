package com.chillax.until.ftp;

public class FTPBean {

	/**
	 * ftp连接ip
	 */
	private String hostIp;
	
	/**
	 * ftp端口
	 */
	private int port;
	
	/**
	 * 账号
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 正式 相对路径
	 */
	private String localPath;
	
	/**
	 * 正式 临时路径
	 */
	private String localPathTemp;
	
	/**
	 * 远程路径，上传文件的默认路径
	 */
	private String remotePath;
	
	/**
	 * FTP文件历史目录,在remote_path_his字段配置
	 */
	private String remotePathHis;

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getLocalPathTemp() {
		return localPathTemp;
	}

	public void setLocalPathTemp(String localPathTemp) {
		this.localPathTemp = localPathTemp;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getRemotePathHis() {
		return remotePathHis;
	}

	public void setRemotePathHis(String remotePathHis) {
		this.remotePathHis = remotePathHis;
	}

	public FTPBean() {
		super();
	}

	public FTPBean(String hostIp, int port, String userName, String password, String localPath, String localPathTemp,
			String remotePath, String remotePathHis) {
		super();
		this.hostIp = hostIp;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.localPath = localPath;
		this.localPathTemp = localPathTemp;
		this.remotePath = remotePath;
		this.remotePathHis = remotePathHis;
	}
	
	
}
