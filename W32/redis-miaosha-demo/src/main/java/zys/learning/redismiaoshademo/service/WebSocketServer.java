package zys.learning.redismiaoshademo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint(value = "/websocket/{uid}")
@Component
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    private Session session;

    private String uid;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketServers =
            new CopyOnWriteArraySet<>();

    /***
     * 向客户端发消息
     * @param message
     * @throws IOException
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /***
     * 连接建立时调用
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        this.uid = uid;
        webSocketServers.add(this);
        addOnlineCount();
        LOGGER.info("websocket已建立");
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            LOGGER.error("websocket IO异常", e);
        }
    }

    /***
     * 收到客户端消息调用
     * @param message
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        LOGGER.info("收到消息: {}", message);
        sendMessage(message);
    }

    /***
     * 关闭连接时调用
     */
    @OnClose
    public void onClose() {
        webSocketServers.remove(this);
        subOnlineCount();
        LOGGER.info("websocket已关闭");
    }

    @OnError
    public void onError(Throwable e) {
        LOGGER.error("连接发生错误", e);
    }

    public static void sendInfo(String message,@PathParam("uid") String userId){
        LOGGER.info("推送消息到窗口"+userId+"，推送内容:"+message);
        for (WebSocketServer item : webSocketServers) {
            try {
                //这里可以设定只推送给这个userId的，为null则全部推送
                if(userId == null) {
                    item.sendMessage(message);
                }else if(item.uid.equals(userId)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
