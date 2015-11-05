package service;

/**
 * Created by Saeed on 11/5/2015.
 */
public interface ProxyService {
    public void setNoProxy();
    public void setUseSystemProxy();
    public void setHttpProxy(String urlText, int port, String userName, String password);
    public void setHttpsProxy(String urlText, int port, String userName, String password);
    public void setFtpProxy(String urlText, int port, String userName, String password);
    public void setSocksProxy(String urlText, int port, String userName, String password);
}
