# Apache Kafka

Pelo que eu entendi com esse estudo o `kafka` funciona da seguinte forma. Um `producer` envia uma mensagem para um `topic` esse tópico pode ter várias partições e se houver uma `key` associada ao tópico ele consegue redirecionar para uma partição de forma não aleatória. Os `consumer` poderão consumir uma partição de um tópico, se os consumidores estiverem no mesmo `groupId` será divido as partições disponíveis entre eles e isso permite que o recebimento de uma mensagem se torne aleatório como se fosse um balanceamento de carga já que se não houver uma `key` associada ao tópico um algoritmo fará a distribuição pra um determinado partição que posteriormente será consumida por um consumidor membro do mesmo groupo. 


## Tecnologias

- Kafka
- Docker
- Spring Boot