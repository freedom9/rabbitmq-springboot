#### 15672是web管理端口，5672是客户端TCP端口。

#### topic类型的exchange注意点
>* 交换机的routing_key不能随便写，**必须是一个单词列表，用点号分开**。单词可以是任意的单词，例如："user.name"。
>* "*"可以代替一个单词。
>* "#"可以代替零个和多个单词。
>* 当队列绑定键是"#"，那么这个队列将接收所有的数据，相当于fanout类型。
>* 当队列绑定键没有"#"或"*"，相当于direct类型。

#### 死信队列中的消息来源
>* 消息ttl过期。
>* 队列达到最大长度（队列满了，无法再添加数据到mq中）。
>* 消息被拒绝（basic.reject或basic.nack）并且requeue=false。

#### 安装rabbitmq_delayed_message_exchange 插件rabbitmq_delayed_message_exchange 插件可实现一个实时延迟队列