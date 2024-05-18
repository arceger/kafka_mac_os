###  Instalacao e configuracao kafka macos 

### producer e consumer com java

- Exemplo simples de classe pra postar loop de mensagens e consumer com sleep
- testado e validado localmente usando java 17

### install kafka local using homebrew

- brew install kafka
- start zookeeper (/usr/local/bin/zookeeper-server-start /usr/local/etc/zookeeper/zoo.cfg)
- start kafka (/usr/local/bin/kafka-server-start /usr/local/etc/kafka/server.properties)

### crie seu topico

- kafka-topics --bootstrap-server localhost:9092 --topic topic1 --create --partitions 3 --replication-factor 1
- kafka-topics.sh --bootstrap-server localhost:9092 --list

### Listar o topico criado

- kafka-topics --list --bootstrap-server localhost:9092



### validacao da instalacao -- testes de console

### Criar um producer que ira enviar mensagens para o topico criado

kafka-console-producer --broker-list localhost:9092 --topic topic1

### E em outro terminar criaremos o consumer(pra visualizar as mensagens)

kafka-console-consumer --bootstrap-server localhost:9092 --topic topic1






