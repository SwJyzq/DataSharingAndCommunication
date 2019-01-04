package z.android.communication.datasharingandcommunication.transport;

public enum TranObjectType {
    REGISTER, // 注册
    LOGIN, // 用户登录
    LOGOUT, // 用户退出登录
    FRIEND_LOGIN, // 好友上线
    FRIEND_LOGOUT, // 好友下线
    MESSAGE, // 用户发送消息
    UNCONNECTED, // 无法连接
    FILE, // 传输文件
    REFRESH, // 刷新
    DATA_DETAIL, //获取资源列表
}
