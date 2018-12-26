package Houtai;

import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import Houtai.Service.Mythread;



public class Service {
	int port;
    List<Socket> clients;
    List<Mythread> tmhread=new ArrayList<Mythread>();
    ServerSocket server;
    //Map<Integer, Mythread> map = new HashMap<Integer, Mythread>(); 
    Map<String, Mythread> map = new HashMap<String, Mythread>(); 
    String [] msg_at= {"01","02","03","04"};
    Mythread mythread = null;
    private String Socke_at=null;
    public String  get_msg() {
    	String msgdd = null;
    	String  hu;
    	for(int i=0;i<tmhread.size();i++) {
    		hu=tmhread.get(i).get_msg();
    		if(hu==null) {}
    		else {
    			 msgdd=hu;
    			 break;
    		}
    	}
    	
    	return msgdd;
    }
    public void send_at(String at,int num) {
//    	 for (int i = tmhread.size() - 1; i >= 0; i--) {
//    		 Mythread ss=tmhread.get(i);
//           if(ss!=null) {
//        	    ss.sendsocket_at(at);
//           }
//         }
    	if(at.equals("input")) {
    		System.out.println("发送input"+map.get("z02"));
    		if(map.get("z02")!=null) {
    		map.get("z02").send_input_Msg();
    		//map.get("z02").sendsocket_at(at);
    		}
    	}else if(num==100){
    		try {map.get("z01").closesocket_at();
    		} catch (Exception ex) {}
    		try {map.get("z02").closesocket_at();
    		} catch (Exception ex) {}
    		try {map.get("z03").closesocket_at();
    		} catch (Exception ex) {}
    	}else {
    		System.out.println("发送at");
	    	//System.out.println(num); 
	    	if(map.get("z01")!=null||map.get("z02")!=null) {
	    		
	    		try {map.get("z02").sendsocket_3at(0);
	    		} catch (Exception ex) {}
	    		
	    		try {map.get("z03").sendsocket_3at(1);
	    		} catch (Exception ex) {}
	    		//zheng
	    		try {map.get("z01").sendsocket_at(0);
	    		} catch (Exception ex) {}
	    		
	    		try {map.get("z02").sendsocket_2at(0);
	    		}catch (Exception ex) {}
	    		//fang	    		
	    		try {map.get("z02").sendsocket_2at(100);
	    		}catch (Exception ex) {}
	    		
	    		try {map.get("z01").sendsocket_at(100);
	    		} catch (Exception ex) {}
	    		//zheng
	    		try {map.get("z01").sendsocket_at(0);
	    		} catch (Exception ex) {}
	    		
	    		try {map.get("z02").sendsocket_2at(0);
	    		} catch (Exception ex) {}
	    		
	    		//fang
	    		try {map.get("z02").sendsocket_2at(100);
	    		} catch (Exception ex) {}
	    		
	    		try {map.get("z01").sendsocket_at(100);
	    		} catch (Exception ex) {}
	    		
	    		try {map.get("z01").closesocket_at();
	    		} catch (Exception ex) {}
	    		//map.get("z02").closesocket_at();
	    		
	    		if(num>0&&num<=8) {
	    			System.out.println("01"); 
	    			try {map.get("z01").sendsocket_at(num);
	    			} catch (Exception ex) {}
	    			}
	    		if(num>8&&num<=12) {
	    			System.out.println("02"); 
	    			try {map.get("z01").sendsocket_at(8);
	    			} catch (Exception ex) {}
	    			
	    			try {map.get("z01").closesocket_at();
	    			} catch (Exception ex) {}
	    			
	    			try {map.get("z02").sendsocket_2at(num);
	    			} catch (Exception ex) {}
	    		}
	    	}

	    	
    	}
    }
//    public void map_ss(Mythread map_socket,int port,Mythread my) {
//    	if(map_socket==null) {
//           	 map.put(port, my);
//           	 for (Entry<Integer, Mythread> entry : map.entrySet()) { 
//           		 System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
//           	 					}
//       			                     
//   	 }else {
//   	
//   		// System.out.println((map_socket==ssocket)+"....kong"); 
////   		 if(map_socket.equals(ssocket)) {
//   		 if(map_socket.equals(my)) {
//  			 System.out.println("相等空"); 
//   			// num();
//   		 }else {
//   			 System.out.println("不相等空"); 
//   			map.put(port, my);
//           	 for (Entry<Integer, Mythread> entry : map.entrySet()) { 
//           		 System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
//           			                       }
//           	 //map_socket.close();
//   		 }
//   	 }
//    }

    public void Service() {
        try {

            port = 8888;
            clients = new ArrayList<Socket>();
            server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                clients.add(socket);
                System.out.println("线程长度"+clients.size()); 
                for(int i=clients.size();i>0;i--) {
                	System.out.println("客户端连接"+clients.get(i-1)); 
                }
                mythread = new Mythread(socket);
                tmhread.add(mythread);
                for(int i=tmhread.size();i>0;i--) {
                	System.out.println("xianc连接"+tmhread.get(i-1)); 
                }        
                mythread.start();
            }


        } catch (Exception ex) {
        }
    }

    class Mythread extends Thread {
        Socket ssocket;
        private InputStream br;
        private OutputStream os = null;
        PrintWriter pw=null; 
        public String msg=null;
        public String get_msg=null;
        public String get_input_msg=null;
        private int at=0;
        private volatile boolean isRun = true;
       
        public boolean vall(InputStream br) {
        	
        	String msg_v = null;
        	byte buffv[]  = new byte[20];
        	 try {
				if((br.read(buffv))!=-1)
				 {            
					msg_v=new String(buffv,4,8);
					System.out.println(msg_v+"vall"); 
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 if(msg_v.equals(Socke_at)) {
        		 return  true;
        	 }else return false;
		
        }
        public String  get_msg() {
        	String msgs;
        	msgs=get_input_msg;
        	get_input_msg=null;
        	get_msg=null;
        	return msgs;
        }
        public  Mythread(Socket s) {
            ssocket = s;                               
        }

        public void run() {
        	  byte buff[]  = new byte[4096];
              String rcvMsg;
              int rcvLen;
              
              byte buffzero[]=new byte[1024];
	          try {
	  				br =ssocket.getInputStream();
	  				} 
	          catch (IOException e) {
	  				// TODO Auto-generated catch block
	  				e.printStackTrace();
	  				}     
              
    
            try {
               // br =ssocket.getInputStream(); //new BufferedReader(new InputStreamReader(ssocket.getInputStream()));
             
                
//                if(Socke_at!=null) {//防止未发送
//                	boolean flg=true;
//                	flg=vall(br);
//                	if(!flg) {
//                		sendsocket_at(Socke_at);
//                	}
//                }
                while (isRun&&!ssocket.isClosed() && !ssocket.isInputShutdown()){
	                if((rcvLen = br.read(buff))!=-1&&!ssocket.isClosed())
	                {            
	                	String head=null;
	                    String relay=null;
	                	 rcvMsg = new String(buff,0,rcvLen);
	                	 String heart=new String(buff,3,5);
	                	 String read=new String(buff,0,5);
	                	 head=new String(buff,0,3);
	                	 relay=new String(buff,0,5);	    	                
	                	 if(relay.equals("relay")) {
	                		 System.out.println("数据返回"+rcvMsg); 
	                		 get_msg=rcvMsg;
	                	 }
	                	 if(relay.equals("input")) {
	                		 System.out.println("输入放回"+rcvMsg); 
	                		 get_input_msg=rcvMsg+ssocket.getPort();	                	                	
	                	 }	                	
	                	 if(heart.equals("heart")) {
	                		 System.out.println("链接gugug"+rcvMsg);  
	                		 Mythread  map_socket=map.get(head);
	                		 if(map_socket==null) {
			                	 if(head.equals("z01")||head.equals("z02")||head.equals("z03")) {
					                	 map.put(head, this);
					                	 for (Entry<String, Mythread> entry : map.entrySet()) { 
					                		 System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
					                	 					}
				                			                       }
		                	 }else {		                		
		                		 if(map_socket==this) {
		                		 }else {
		                			 System.out.println("不相等"); 
		                			 map.put(head, this);
				                	 for (Entry<String, Mythread> entry : map.entrySet()) { 
				                		 System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
				                			                       }
				                	 
				                	 
				                	 
				                	 if(map_socket.os!=null) 
				                		 map_socket.os.close(); 
				     				if(map_socket.br!=null) 
				     					map_socket.br.close();
				     				clients.remove(map_socket.ssocket);
				     				tmhread.remove(map_socket);
				     				map_socket.ssocket.close();
				                	 System.out.println(map_socket.toString()+map_socket.ssocket.isClosed());
				                	 
				                	 
		                		 }
		                	 }
	                	 }	            	                	 
	                }else { 
	                	isRun=false;
	                	System.out.println("线程if结束");
	                	}
                } 
                System.out.println("线程结束");
//				if(this.os!=null) 
//					this.os.close(); 
//				if(br!=null) 
//					br.close();
//				clients.remove(ssocket);
//				tmhread.remove(this);
//				 System.out.println("线程长度"+clients.size()+tmhread.size()); 
//				if(ssocket!=null&&!ssocket.isClosed()) 
//					ssocket.close(); 
				
            } catch (Exception ex) {
            	isRun=false;
            }
        }
       
        public void sendsocket_at(int at) {
        	String []msgattt= {
        			"all00000001","all00000011","all00000111","all00001111","all00011111","all00111111","all01111111","all11111111"
        	};
        	String []fang= {
        			"all00000000","all00000001","all00000011","all00000111","all00001111","all00011111","all00111111","all01111111"
        	};
        	String []ledbuff= {
        			"all00000001","all00000010","all00000100","all00001000","all00010000","all00100000","all01000000","all10000000"
        	};
        	if(at==0) {//泡灯
        		
				for(int i=0;i<8;i++) {
					try {
						os=ssocket.getOutputStream();
						byte d[] = msgattt[i].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
						os.write(d);               
			             os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        	}else if(at==100){
        		for(int i=8;i>0;i--) {
					try {
						os=ssocket.getOutputStream();
						byte d[] = fang[i-1].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
						os.write(d);               
			             os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        	}
        	else {
        		for(int j=0;j<at;j++) {
        			try {				
    					os=ssocket.getOutputStream();
    					byte d[] = ledbuff[j].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
    					os.write(d);               
    		             os.flush();
    		             Thread.sleep(500);
    				} catch (IOException | InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}    
        		}   
        	}		
        }
        public void sendsocket_3at(int at) {
        	String []msga2= {
        			"all00110000","all01110000","all11110000"
        	};
        	String []fang3= {
        			"all10000001","all11111111"
        	};
    
        	if(at==0) {//02
        		
				for(int i=0;i<3;i++) {
					try {
						os=ssocket.getOutputStream();
						byte d[] = msga2[i].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
						os.write(d);               
			             os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        	}else if(at==1){//03
        		for(int i=0;i<2;i++) {
					try {
						os=ssocket.getOutputStream();
						byte d[] = fang3[i].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
						os.write(d);               
			             os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        	}
       		
        }
        public void closesocket_at() {
        	try {
				os=ssocket.getOutputStream();
				byte d[] = "all00000000".getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
				os.write(d);               
	             os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        public void sendsocket_2at(int at) {
        	String []msgattt= {
        			"all11110001","all11110011","all11110111","all11111111"
        	};
        	String []fang= {
        			"all11110000","all11110001","all11110011","all11110111"
        	};
        	String []ledbuff= {
        			"all11110001","all11110010","all11110100","all11111000"
        	};
        	if(at==0) {
				for(int i=0;i<4;i++) {
					try {
						os=ssocket.getOutputStream();
						byte d[] = msgattt[i].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
						os.write(d);               
			             os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}else if(at==100){
	        		for(int i=4;i>0;i--) {
						try {
							os=ssocket.getOutputStream();
							byte d[] = fang[i-1].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
							os.write(d);               
				             os.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
	        	}else {
	        		for(int j=0;j<at-8;j++) {
	        			try {				
	    					os=ssocket.getOutputStream();
	    					byte d[] = ledbuff[j].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
	    					os.write(d);               
	    		             os.flush();
	    		             Thread.sleep(500);
	    				} catch (IOException | InterruptedException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}    
	        		}   
				}
        	
			
        }
        public int num() {
        	ThreadGroup group = Thread.currentThread().getThreadGroup();  
        	ThreadGroup topGroup = group;  
        	// 遍历线程组树，获取根线程组  
        	while (group != null) {  
        	    topGroup = group;  
        	    group = group.getParent();  
        	}  
        	// 激活的线程数加倍  
        	int estimatedSize = topGroup.activeCount() * 2;  
        	Thread[] slackList = new Thread[estimatedSize];  
        	// 获取根线程组的所有线程  
        	int actualSize = topGroup.enumerate(slackList);  
        	// copy into a list that is the exact size  
        	Thread[] list = new Thread[actualSize];  
        	//System.arraycopy(slackList, 0, list, 0, actualSize);  
        	//System.out.println("Thread list size == " + list.length); 
        	
        	for (Thread thread : list) {  
        	   // System.out.println(thread.getName());  
        	} 
        	return list.length;
        }
	    public void send_input_Msg() {
	    	try {
				os=ssocket.getOutputStream();
				byte d[] = "input".getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
				os.write(d);               
	             os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("input"+"发送");  
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }
    public void de_ser() {
//   	 for (Entry<String, Socket> entry : map.entrySet()) { 
//                         System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
//                         try {
//							entry.getValue().close();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//                       }
    }
   
		
      
}

 