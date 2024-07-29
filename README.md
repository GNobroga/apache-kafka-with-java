# Apache Kafka

`producer` - É quem publica uma mensagem em um tópico

`topic` - É como se fosse um grupo onde será postada a mensagem

`partitions` - São áreas dentro de um topic onde a mensagem será gravada.

`consumer` - É quem recebe a mensagem

`groupId` - Consumers em um mesmo groupId dividem as partições existentes no tópico.

`key` - A key é um atributo que serve de critério para que o kafka decida qual partição irá receber a mensagem. Ela serve para que todas as mensagens com a mesma key sejam direcionadas para a mesma participação. Essa é a forma de garantir consumo me mensagens na ordem correta para a mesma key. Se a key for o código de um produto vc pode garantir que a reserva de estoque será para os pedidos que chegarem na ordem, por exemplo.


Resumidamente, o `kafka` funciona da seguinte forma. Um `producer` envia uma mensagem para um `topic` esse tópico pode ter várias partições e se houver uma `key` associada ao tópico ele consegue redirecionar para uma partição de forma não aleatória. Os `consumer` poderão consumir uma partição de um tópico, se os consumidores estiverem no mesmo `groupId` será divido as partições disponíveis entre eles e isso permite que o recebimento de uma mensagem se torne aleatório como se fosse um balanceamento de carga já que se não houver uma `key` associada ao tópico um algoritmo fará a distribuição pra um determinado partição que posteriormente será consumida por um consumidor membro do grupo. 

Obs: Se um `consumer` for associado a um grupo diferente será feito um broadcasting.

## Tecnologias

- Kafka
- Docker
- Spring Boot