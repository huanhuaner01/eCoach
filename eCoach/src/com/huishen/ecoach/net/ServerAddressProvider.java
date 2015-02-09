package com.huishen.ecoach.net;

/**
 * 该类提供系统服务器的地址。
 * 
 * @author Muyangmin
 * @create 2015-2-7
 */
@SuppressWarnings("unused")
final class ServerAddressProvider {
	
	private static final IServer CURRENT_SERVER = new InternalTestServer();
	
	protected static final String getServerAddress(){
		return CURRENT_SERVER.getServerAddress();
	}
	
	//公开服务器
	private static final class PublicServer implements IServer{

		@Override
		public String getServerAddress() {
			return "http://www.huishen.net";
		}
		
	}
	
	//内部测试服务器
	private static class InternalTestServer implements IServer{

		@Override
		public String getServerAddress() {
			return "http://192.168.0.1:8080";
		}
		
	}
	
	private static interface IServer {
		/**
		 * 获得服务器的地址。
		 * 
		 * @return 返回服务器的地址（通常是域名，有时还包含端口号）。一个典型值是这样：
		 *         <code>http://www.huishen.com</code>
		 */
		String getServerAddress();
	}
}