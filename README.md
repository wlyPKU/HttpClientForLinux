#北京大学网络连接程序
---
可用于非图形界面的Linux系统上,项目提供了两种选择:
##程序说明
###命令行形式
命令行形式的程序(见connect.sh),可以通过替换connect.sh中账号和密码来进行连接、断开本机连接和断开所有连接操作.配合cron可进行自动重连.
###Java形式
命令行形式的不足之处在于保存了明文的账号和密码,但如果我们将账号和密码写入java程序中,在服务器上以jar包形式执行,同样可以配合cron实现自动重连的功能.

执行方式:
args[0]: IAAA账号
args[1]: IAAA密码
args[2]: operation: connect | disconnect | disconnectall
args[3]: range: 1(收费网) | 2(免费网)
args[4]: verbose(是否详细打印response): True | False
##API说明
###its.pku.edu.cn:5428/ipgatewayofpku
格式https://its.pku.edu.cn:5428/ipgatewayofpku?uid=xxxxxxxx&password=xxxxxx&range=2&operation=&timeout=1
Operation:
* connect: range=2表示免费网，=1表示收费网
* disconnect: 断开本机连接
* disconnectall: 断开所有连接

返回值:
* 整个返回值
```html
<html>
<head>
<title>
IP控制网关
</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<style type="text/css">
body{ font-size: 9pt; line-height :13pt }
input { font-family: "宋体"; font-size: 9pt}
td { font-size: 9pt; background-color:#deebfb;color: #222222}
</style>
</head>
<body bgColor="#ffffff" text="#0000bb" onLoad="flash()">
<br><br><center>
<!--IPGWCLIENT_START SUCCESS=YES STATE=connected USERNAME=1501214408 FIXRATE=YES FR_DESC_CN=不限时间 FR_DESC_EN=unlimited FR_TIME=不限时间 SCOPE=international DEFICIT=NO CONNECTIONS=3 BALANCE=40.001 IP=162.105.146.215 MESSAGE=  IPGWCLIENT_END-->
</body>
</html>
```

* 代码截取的返回值:
```
SUCCESS=YES STATE=connected USERNAME=1501214408 FIXRATE=YES FR_DESC_CN=不限时间 FR_DESC_EN=unlimited FR_TIME=不限时间 SCOPE=international DEFICIT=NO CONNECTIONS=3 BALANCE=40.001 IP=162.105.146.215 MESSAGE=
```

###its.pku.edu.cn/cas/ITSClient(已废弃)
* Endpoint: POST https://its.pku.edu.cn/cas/ITSClient
* Content-Type: application/x-www-form-urlencoded
* Accept: */*
* Response: Contect-Type: text/html;charset=utf-8，但是实际上是 JSON。

####Connect(连接网关)
Request
* app: IPGWiOS1.2
* cmd: open
* ip: 空
* iprange: fee/free
* lang: 空
* password: IAAA 密码
* username: 学号

Response(注意，所有值都是 string)
* IP: 当前的 IP
* SCOPE: international/Free
* FR_TYPE: 空字符串
* FR_DESC_EN: Unlimited（我没有测试其他包月方案）
* FR_TIME_EN: Unlimited
* BALANCE_CN: 余额，一位小数，字符串类型（如 "100.0"）
* CONNECTIONS: 当前活跃连接数，整数，字符串类型（如 "2"）
* DEFICIT : 是否欠费
* ver : 1.1，可能是 API 版本号
* FR_DESC_CN: 90元不限时包月
* FR_TIME_CN: 不限时
* FIXRATE: YES
* succ : 空字符串
* BALANCE_EN: 余额，一位小数，字符串类型（如 "100.0"）

####Disconnect(断开网关)
Request
* cmd: close
* lang: 空

Response
* succ: close_OK

####Get Connection(获取当前在线连接)

Request
* cmd: getconnections
* lang: 空
* password: IAAA 密码
* username: 学号

Response
* succ: 当前连接描述，格式为：<ip>;收费地址;<地点>;<连接时间YYYY-MM-DD HH:mm:ss>;，若有多条记录就直接接下去。

Response Example
```
{
  "succ" : "1.2.3.4;收费地址;理科5;2016-09-21 20:32:00;5.6.7.8;收费地址;45甲;2016-09-16 04:03:14;9.10.11.12;收费地址;理科5;2016-09-21 20:34:09"
}
```

####Disconnect Certain Session(断开指定 IP 的连接)
Request
* cmd: disconnect
* ip: 要断开连接的 IP
* lang: 空
* password: IAAA 密码
* username: 学号

Response
* succ: 断开连接成功
