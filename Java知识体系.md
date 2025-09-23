Nginx 的配置项数量庞大，按作用域大致可分为以下 6 大类（每类给出最常用的指令，按字母顺序排列，便于速查）。下面列出的 200+ 指令覆盖了 95 % 的日常场景；如需完整列表，可在源码 src/http/modules/ 目录下搜索 ngx_http_*_command 数组。

------------------------------------------------
一、main 全局块（与业务无关，影响守护进程本身）
user nginx nginx;              # 工作进程用户/组  
worker_processes auto;         # 进程数，auto=CPU核数  
worker_rlimit_nofile 65535;    # 单进程最大文件句柄  
pid /var/run/nginx.pid;        # pid 文件  
error_log /var/log/nginx/error.log warn;   # 全局错误日志  
daemon on|off;                 # 是否以守护进程方式运行  
master_process on|off;         # 是否启用 master-worker 模式  
load_module modules/ngx_http_geoip2_module.so; # 动态模块加载  

------------------------------------------------
二、events 块（连接调度模型）
use epoll;                     # 事件模型：epoll/kqueue/select  
worker_connections 65535;      # 每个 worker 最大连接数  
multi_accept on;               # 一次性尽量接收全部新连接  
accept_mutex on|off;           # 是否串行化 accept 锁  
accept_mutex_delay 100ms;      # 抢锁失败后再试间隔  

------------------------------------------------
三、http 块（HTTP 协议公共配置）
include mime.types;            # 文件扩展名→MIME 映射  
default_type application/octet-stream;  
server_tokens off;             # 隐藏版本号  
sendfile on;                   # 零拷贝发送文件  
tcp_nopush on;                 # 包合并延迟发送  
tcp_nodelay on;                # 禁用 Nagle，降低延迟  
keepalive_timeout 65s;         # 长连接超时  
keepalive_requests 1000;       # 单连接最大请求数  
client_max_body_size 1m;       # 上传包体上限  
client_body_buffer_size 128k;  # 包体缓存大小  
client_header_buffer_size 4k;  
large_client_header_buffers 8 16k;  
types_hash_max_size 2048;  
server_names_hash_bucket_size 64;  

------------------------------------------------
四、日志 / 压缩 / 缓存 / 限速
log_format main '$remote_addr - $remote_user [$time_local] "$request" '  
                '$status $body_bytes_sent "$http_referer" '  
                '"$http_user_agent" "$http_x_forwarded_for"';  
access_log /var/log/nginx/access.log main buffer=32k flush=5s;  

gzip on;                       # 内容压缩  
gzip_vary on;  
gzip_min_length 1k;  
gzip_comp_level 6;  
gzip_types text/plain text/css application/json …;  

# 浏览器缓存  
expires 30d;  
add_header Cache-Control "public, no-transform";  

# 限速  
limit_req_zone $binary_remote_addr zone=api:10m rate=10r/s;  
limit_req zone=api burst=20 nodelay;  
limit_conn_zone $binary_remote_addr zone=addr:10m;  
limit_conn addr 100;  

------------------------------------------------
五、upstream 块（负载均衡）
upstream backend {  
    server 192.168.1.10:8080 weight=5 max_fails=3 fail_timeout=30s;  
    server 192.168.1.11:8080 weight=1 backup;  
    keepalive 32;              # 与后端长连接池  
    keepalive_requests 1000;  
    keepalive_timeout 60s;  
}  

负载算法：  
ip_hash;                       # 会话保持  
least_conn;                    # 最少连接  
hash $remote_addr consistent;  # 一致性哈希  
random two least_conn;         # 随机两节点+最少连接  

------------------------------------------------
六、server / location 块（虚拟主机与 URI 映射）
server {  
    listen 80;                 # 监听端口  
    listen 443 ssl http2;      # 多协议复用同一虚拟主机  
    server_name example.com www.example.com;   # 域名  
    root /var/www/html;        # 站点根目录  
    index index.html index.php;  

    # 重定向  
    return 301 https://$host$request_uri;  

    # SSL  
    ssl_certificate     cert.pem;  
    ssl_certificate_key cert.key;  
    ssl_session_timeout 1d;  
    ssl_session_cache   shared:SSL:10m;  
    ssl_protocols       TLSv1.2 TLSv1.3;  
    ssl_ciphers         HIGH:!aNULL:!MD5;  
    ssl_prefer_server_ciphers on;  

    # 反向代理  
    location /api/ {  
        proxy_pass http://backend;      # 注意是否带斜杠  
        proxy_set_header Host $host;  
        proxy_set_header X-Real-IP $remote_addr;  
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;  
        proxy_connect_timeout 3s;  
        proxy_send_timeout    60s;  
        proxy_read_timeout    60s;  
        proxy_buffering on;  
        proxy_buffer_size 4k;  
        proxy_buffers 8 4k;  
    }  

    # 前端 history 路由  
    location / {  
        try_files $uri $uri/ /index.html;  
    }  

    # 静态资源缓存+CORS  
    location ~* \.(js|css|png|jpg|gif|ico|svg|woff2)$ {  
        expires 30d;  
        add_header Cache-Control "public";  
        add_header Access-Control-Allow-Origin *;  
    }  

    # 拒绝隐藏文件  
    location ~ /\. { deny all; }  

    # 错误页  
    error_page 404 /404.html;  
    error_page 500 502 503 504 /50x.html;  
}  

------------------------------------------------
七、常见第三方模块指令（安装后生效）
# http_geoip2  
geoip2 /usr/share/GeoIP/GeoLite2-City.mmdb { auto_reload 60s; }  
map $geoip2_data_country_code $allow_country { default no; CN yes; }  

# fastcgi  
location ~ \.php$ {  
    fastcgi_pass  127.0.0.1:9000;  
    fastcgi_index index.php;  
    fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;  
    include fastcgi_params;  
}  

# uwsgi / grpc / njs / lua 等模块均有自己独立指令集  

------------------------------------------------
速查小结
1. 先定“作用域”：main → events → http → upstream/server → location  
2. 再选“功能”：进程数、连接池、日志、压缩、缓存、代理、负载、SSL、限速、黑白名单…  
3. 同一指令在不同作用域可重复出现，内层覆盖外层。  
4. 修改后执行 nginx -t 检查语法，再 -s reload 平滑重载即可生效。
