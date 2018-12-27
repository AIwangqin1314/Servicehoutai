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
    Map<String, Mythread> map = new HashMap<String, Mythread>(); 
    String [] msg_at= {"01","02","03","04"};
    Mythread mythread = null;
    private boolean isshan=true;
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
    	if(at.equals("input")) {
    		System.out.println("发送input"+map.get("z02"));
    		if(map.get("z02")!=null) {
    		map.get("z02").send_input_Msg();
    		//map.get("z02").sendsocket_at(at);
    		}
    	}else if(num==100){
    		isshan=false;
    		try {map.get("z01").closesocket_at(1);
    		} catch (Exception ex) {}
    		try {map.get("z02").closesocket_at(1);//全关
    		} catch (Exception ex) {}
    		try {map.get("z03").closesocket_at(1);
    		} catch (Exception ex) {}
    	}else {
    		System.out.println("发送at");
	    	//System.out.println(num); 
	    	if(map.get("z01")!=null||map.get("z02")!=null) {
	    		try {map.get("z02").sendsocket_3at(0);
	    		} catch (Exception ex) {}
	    	//闪烁
	    	 Thread shan = new Thread(new Runnable(){  
		             public void run(){  
		            	 System.out.println(num); 
		            	
			 	    		
		            	 for(int i=0;i<8;i++) {
		            		 System.out.println(i);
		            		 try {map.get("z01").sendall();
				 	    		} catch (Exception ex) {}
		            		 try {map.get("z02").sendall();
				 	    		} catch (Exception ex) {}

		            		 
				 	    		
				 	    		try {map.get("z01").sendsocket_at(0,20);
				 	    		} catch (Exception ex) {}
				 	    		
				 	    		try {map.get("z02").sendsocket_2at(0,20);
				 	    		}catch (Exception ex) {}
		            	 }
		            	
//		 	    
		            // run方法具体重写
		            	 while(isshan) {
		            	 if(num>0&&num<=8) {
		 	    			System.out.println("01"+num); 
		 	    			try {map.get("z01").sendsocket_at(100,num);//shan
		 		    		} catch (Exception ex) {}
		 	    			
		 	    			}
		 	    		if(num>8&&num<=12) {
		 	    			System.out.println("02"+num); 
		 	    			try {map.get("z02").sendsocket_2at(100,num);//shan
		 		    		} catch (Exception ex) {}
		 	    			
		 	    		}
		 	    		try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		            	 }
		            	 
		            	 isshan=true;
		             }
		             }
	    	 );  
	    	 shan.start(); 

	    		
	    		try {map.get("z03").sendsocket_3at(1);
	    		} catch (Exception ex) {}
	    	}
    	}
    }


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
        private boolean turn_on=true;
       
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
            } catch (Exception ex) {
            	isRun=false;
            }
        }
       
        public void sendsocket_at(int at,int num) {
        	String []msgattt= {
        			"all01111111","all00111111","all00011111","all00001111","all00000111","all00000011","all00000001","all00000000"
        	};
        	String []fang= {
        			"all11111110","all11111101","all11111011","all11110111","all11101111","all11011111","all10111111","all01111111"
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
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        	}else if(at==100){
        		if(turn_on) {
					turn_on=false;
					try {
						os=ssocket.getOutputStream();
						byte d[] = fang[num].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
						os.write(d);               
			             os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
				}else {
					turn_on=true;
					try {
						os=ssocket.getOutputStream();
						byte d[] = "all11111111".getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
						os.write(d);               
			             os.flush();
					} catch (IOException e) {
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
        public void sendall() {
        	try {
				os=ssocket.getOutputStream();
				byte d[] = "all11111111".getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
				os.write(d);               
	             os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
        public void closesocket_at(int num) {
        	if(num==1) {
	        	try {
					os=ssocket.getOutputStream();
					byte d[] = "all00000000".getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
					os.write(d);               
		             os.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}else
        	{
        		try {
					os=ssocket.getOutputStream();
					byte d[] = "all11110000".getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
					os.write(d);               
		             os.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        public void sendsocket_2at(int at,int num) {
        	String []msgattt= {
        			"all11110111","all11110011","all11110001","all11110000"
        	};
        	String []fang= {
        			"all11111110","all11111101","all11111011","all11110111"
        	};
        	String []fanxingzuo= {
        			"all11111110","all11111101","all11111011","all11110111"
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
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}else if(at==100){
					if(turn_on) {
						turn_on=false;
						try {
							os=ssocket.getOutputStream();
							byte d[] = fanxingzuo[num-9].getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
							os.write(d);               
				             os.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}			
					}else {
						turn_on=true;
						try {
							os=ssocket.getOutputStream();
							byte d[] = "all11111111".getBytes();            // 只能输出byte数组，所以将字符串变为byte数组
							os.write(d);               
				             os.flush();
						} catch (IOException e) {
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

    }
   
		
      
}

 