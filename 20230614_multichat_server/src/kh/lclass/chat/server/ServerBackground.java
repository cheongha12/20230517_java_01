package kh.lclass.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ServerBackground {
	private ServerSocket serverSocket;
	private ServerGUI gui;// 서버소켓 //socket창 여러개 만들때 ArrayList로 만들어줌

	// client 여러명을 관리 : key:nickname, value:OutputStream
	private Map<String, BufferedWriter> mapClients = new HashMap<String, BufferedWriter>();
	// map.put("ej",socket);
	// map.put("hj",socket);
	private Socket socket;

	private int count; // 현재 접속자 수

	// 서버 생성 및 설정
	public void setting() {
		//참고 : 동시접속자로 map에 정보가 동기화 되어 들어가도록 설정함.
		Collections.synchronizedMap(mapClients);
		try {
			serverSocket = new ServerSocket(7777);
			// 방문자 접속을 계속 받아들임. 무한반복함 . GUI 프로그램경우 창 닫힐때 까지 계속 반복됨. break 없음
			while (true) {
				// 접속자 대기 중
				socket = serverSocket.accept();
				new Client(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 클라이언트 접속하면 그 정보를 나타내 주는 메소드
	public void addClient(String nickname) {
		gui.appendMsg(nickname + "님이 접속했습니다.");

	}

	// private ClientGUI gui;
	public void setGui(ServerGUI gui) {
		this.gui = gui;
	}

	public void sendMessage(String msg) {
		// client들 모두에게 msg전달
		Set<String> keys = mapClients.keySet();
		for (String key : keys) {
			BufferedWriter wr = mapClients.get(key);
			try {
				wr.write(key+":"+msg + "\n");
				wr.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

////////////////Inner Class//////////////////////	
	class Client extends Thread {
		private BufferedReader br;
		private BufferedWriter bw;
		private String nickname;

		public Client(Socket socket) {
			// 초기값 설정
			// client와 입/출력 통로 생성
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				// client nickname이 바로 이어서 들어옴.
				// 접속되면 바로 nickname이 전달될 것이므로 읽음
				String nickname = br.readLine();
				// server 화면에 표현
				addClient(nickname);
				// client outputStream 관리 map에 추가
				mapClients.put(nickname, bw);
				// client map 모두에게 접속 정보 전달
				sendMessage(nickname + "님 접속했습니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			// 클라이언트마다 각각에서 전달되어오는 메시지 확인하고 화면에 출력
			// client와 입력통로가 끊어지지 않았다면 계속 반복확인함.
			// client에서 수신받은 msg
			while (br != null) {
				try {
					String msg = br.readLine();
					gui.appendMsg(msg);
					sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}
}