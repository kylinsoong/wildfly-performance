### Connector 初始化开始过程

如下 Sequence 图

![JBoss Web/Tomcat Connetor init/start process](http-connector-JIoEndpoint.png)

* WebConnectorService 指的是 `org.jboss.as.web.WebConnectorService`

* Connector 指的是 `org.apache.catalina.connector.Connector`

* Http11Protocol 指的是 `org.apache.coyote.http11.Http11Protocol`

* JIoEndpoint 指的是 `org.apache.tomcat.util.net.JIoEndpoint`

#### Connector init() 

Connector 可以是 HTTP Connector，也可以是 AJP Connector，Connector 中有 ProtocolHandler 和 Adapter 属性，Connector 初始化主要包括：初始化 Adapter，且将初始化的 Adapter 的 设定给 ProtocolHandler，然后调运 ProtocolHandler 的初始化方法，如下面代码段所示：

~~~
        // Initializa adapter
        adapter = new CoyoteAdapter(this);
        protocolHandler.setAdapter(adapter);

        IntrospectionUtils.setProperty(protocolHandler, "jkHome", System.getProperty("catalina.base"));

        try {
            protocolHandler.init();
        } catch (Exception e) {
            throw new LifecycleException(MESSAGES.protocolHandlerInitFailed(e));
        }
~~~ 

#### Http11Protocol init()

Http11Protocol 它有一个 Http11ConnectionHandler Handler，该 Handler 实现 `org.apache.tomcat.util.net.JIoEndpoint.Handler` 接口，Http11Protocol 同样有一个 JIoEndpoint 属性，该属性用来处理 incoming TCP connections，如下代码段所示：

~~~
    protected Http11ConnectionHandler cHandler = new Http11ConnectionHandler(this);
    protected JIoEndpoint endpoint = new JIoEndpoint();
~~~

Http11Protocol 初始化主要包括： 

* 给 JIoEndpoint 设定名字，默认设定的名字为 http-/127.0.0.1:8080

* 给 JIoEndpoint 设定 socket handler，设定的 handler 为 Http11ConnectionHandler，该 handler 的作用是 Handling of accepted sockets

* 调运 JIoEndpoint 的初始化方法


#### JIoEndpoint init()

JIoEndpoint，关于此类的作用之前我们有说，对该类最直接的总结如下：

~~~
/**
 * Handle incoming TCP connections.
 *
 * This class implement a simple server model: one listener thread accepts on a socket and
 * creates a new worker thread for each incoming connection.
 *
 * More advanced Endpoints will reuse the threads, use queues, etc.
 *
 * @author James Duncan Davidson
 * @author Jason Hunter
 * @author James Todd
 * @author Costin Manolache
 * @author Gal Shachor
 * @author Yoav Shapira
 * @author Remy Maucherat
 */
public class JIoEndpoint {
~~~

JIoEndpoint 初始化包括：

* 初始化 Acceptor thread count，默认初始设定的 Acceptor thread count 为 1

* 初始化 ServerSocketFactory，并通过初始化的 ServerSocketFactory 创建 ServerSocket


#### Connector start() 

Connector 开始方法验证更新当前的状态，并调运 Http11Protocol 的开始方法

#### Http11Protocol start()

Http11Protocol 的开始方法中直接调运 JIoEndpoint 的开始方法。

#### JIoEndpoint start() 

JIoEndpoint 的开始方法主要包括：

如果外部基于 Executor 的线程池为空，则初始化内部的 workers 栈，该栈保存Worker，初始化的栈大小定义如下：

~~~
 protected int maxThreads = (org.apache.tomcat.util.Constants.LOW_MEMORY) ? 64 : ((Constants.MAX_THREADS == -1) ? 512 * Runtime.getRuntime().availableProcessors() : Constants.MAX_THREADS);
~~~

如上：

* 如果通过系统参数 -Dorg.apache.tomcat.util.LOW_MEMORY=true，则初始化的栈大小为 64 

* 如果通过系统参数 -Dorg.apache.tomcat.util.net.MAX_THREADS=XXX 指定最大值，则初始化的栈大小为系统参数指定的最大值 

* 如果没有通过系统参数指定 MAX_THREADS，则初始化的栈大小为Runtime.getRuntime().availableProcessors()

启动 Poller 线程，默认线程的名字为 http-/127.0.0.1:8080-Poller

启动 Acceptor 线程，默认线程的名字为 http-/127.0.0.1:8080-Acceptor-0

如下代码段显示如上逻辑

~~~
public void start()
        throws Exception {
        // Initialize socket if not done before
        if (!initialized) {
            init();
        }
        if (!running) {
            running = true;
            paused = false;

            // Create worker collection
            if (executor == null) {
                workers = new WorkerStack(maxThreads);
            }

            // Start event poller thread
            eventPoller = new Poller();
            eventPoller.init();
            Thread pollerThread = new Thread(eventPoller, getName() + "-Poller");
            pollerThread.setPriority(threadPriority);
            pollerThread.setDaemon(true);
            pollerThread.start();

            // Start acceptor threads
            for (int i = 0; i < acceptorThreadCount; i++) {
                Thread acceptorThread = new Thread(new Acceptor(), getName() + "-Acceptor-" + i);
                acceptorThread.setPriority(threadPriority);
                acceptorThread.setDaemon(daemon);
                acceptorThread.start();
            }
        }
    }
~~~ 

### max-connections

JBoss Web 中关于max-connections 的定义如下

~~~
<subsystem xmlns="urn:jboss:domain:web:1.4" default-virtual-server="default-host" native="false">
            <connector name="http" protocol="HTTP/1.1" scheme="http" socket-binding="http" max-connections="200" />
~~~

如上，如果我们定义了 max-connections，WebConnectorService 开始方法中会有如下逻辑：

* 设定 JIoEndpoint 中 pollerSize，如下代码:

~~~
    protected int pollerSize = (org.apache.tomcat.util.Constants.LOW_MEMORY) ? 128 : (32 * 1024);
    public void setPollerSize(int pollerSize) { this.pollerSize = pollerSize; }
    public int getPollerSize() { return pollerSize; }
~~~

如上，默认的 pollerSize 如果没有 -Dorg.apache.tomcat.util.LOW_MEMORY=true 设定，它的值为 32 * 1024。

* 设定 JIoEndpoint 中 maxThreads，如下代码段：

~~~
    protected int maxThreads = (org.apache.tomcat.util.Constants.LOW_MEMORY) ? 64 : ((Constants.MAX_THREADS == -1) ? 512 * Runtime.getRuntime().availableProcessors() : Constants.MAX_THREADS);
    public void setMaxThreads(int maxThreads) { this.maxThreads = maxThreads; }
    public int getMaxThreads() { return maxThreads; }
~~~

注意，该 maxThreads 用来初始化内部的 workers 栈的大小。




