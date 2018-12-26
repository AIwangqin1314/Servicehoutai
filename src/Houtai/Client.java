package Houtai;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class Client
 */
//@WebServlet("/Client")
public class Client extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MyThread myThread=new MyThread();
	String at="all00000000";
	private boolean send_flg=false;
	private int time_num=0;
	 Timer t=new Timer();
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("init");	
		myThread.start();//�����������߳�	
		
	        
	}

	class MyTask extends TimerTask{

	    @Override
	    public void run() {
	    	time_num++;
	    	if(time_num>=6*3+2) {
	    		time_num=0;
	    		System.out.println("�뿪");
				send_flg=false;
		        myThread.send_at("all",100);
		        this.cancel();
	    	}else {
	    	myThread.send_at("input",0);
	        System.out.println("��������!!!!"+":"+time_num);
	        String msg_ny=myThread.getmsg();
	        String msg_in=null;
	        if(msg_ny==null) {
	        	
	        }else {
		         msg_in=msg_ny.substring(0, 13);}
	        
	        
				System.out.println(msg_ny+":"+msg_in);
				if("input00000000".equals(msg_in)) {
					System.out.println("�뿪");
					send_flg=false;
					time_num=0;
			        myThread.send_at("all",100);
			        this.cancel();
				}      
	    	}
				
	    }
	}
	public class MyThread extends Thread{//�̳�Thread��
		private Service service=new Service();
		private Service serviceat;
		public String getmsg() {
			return service.get_msg();
		}
		public void de_ser() {
			
			service.de_ser();
		}
		public void send_at(String at,int num) {
			if(service!=null)service.send_at(at,num);
			System.out.println(service); 
			
		}
		public void run(){
				// service=new Service();
				// serviceat=service;
			service.Service();
		}

		}
	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("des");
		myThread.de_ser();
		//�ͷ���Դ
		
	}
    public Client() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String name=request.getParameter("name");
		String flg=request.getParameter("flg");
		String id=request.getParameter("id");
		String port="";
		String input="";
		String jieguo="";
		int Stuat=0;
		String msg_at=null;
		System.out.println("�õ�����"+name+flg+id);
		
		if(id!=null&&!"".equals(id)&&"ok".equals(flg)&&flg!=null) {
			if(Integer.parseInt(id)>=0&&Integer.parseInt(id)<12) {
				if(send_flg==false) {
						send_flg=true;
						myThread.send_at("input",0);
						msg_at=myThread.getmsg();
						System.out.println(msg_at);
						if(msg_at!=null) {
							input=msg_at.substring(0, 13);
							if(input.equals("input00000100")) {//����
								//��3���ִ��MyTask���е�run����
						        t.schedule(new MyTask(),0,1000*10);//1000*60*3+15000);
								myThread.send_at("all",Integer.parseInt(id)+1);
								msg_at=myThread.getmsg();
								if(msg_at!=null) {
								port=msg_at.substring(0, 5);
								}
								jieguo="���";
								
							}else {//����
								System.out.println("����վ");
								jieguo="����վ";
								send_flg=false;
							}
						}else {
							System.out.println("����վ ���ؿ�");
							jieguo="����";
							send_flg=false;
						}
					}else {//��������
						jieguo="������";
					}
				port=myThread.getmsg();
				myThread.getmsg();
				Stuat=1;
			}else {//����������
				jieguo="����������";
			}
		}else {
			port="";
			jieguo="��Ч";
			msg_at="";
			input="";
		}
		
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("content-type", "text/json; charset=UTF-8");
		
		
		//java������json����
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("port",port);
		jsonObject.put("stuat", Stuat);
		jsonObject.put("jieguo",jieguo);
		jsonObject.put("input", input);
		
		PrintWriter out = response.getWriter();
		out.write(jsonObject.toString());
        out.flush();
        out.close();

		//response.getWriter().append("Served at: "+msg_at).append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
