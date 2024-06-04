# ms-reserva

Este repositório contém a implementação de uma arquitetura de microsserviços para um sistema de reserva de propriedades. A arquitetura inclui vários serviços independentes que se comunicam entre si.

## Serviços

1. **Eureka Server**: Serviço de descoberta que permite que os microsserviços se registrem e descubram uns aos outros.
2. **Gateway Service**: Gateway API que encaminha solicitações para os microsserviços correspondentes.
3. **ms-property-catalog**: Serviço responsável pelo gerenciamento do catálogo de propriedades.
4. **ms-reservation**: Serviço responsável por gerenciar reservas de propriedades.
5. **ms-user**: Serviço para gerenciar informações de usuários.
6. **notification-service**: Serviço que gerencia notificações aos usuários.

## Tecnologias utilizadas

- **Java 17**
- **Spring Boot 3**
- **API REST**
- **RabbitMq**
- **MySQL**
- **Docker**

## Ferramentas utilizadas

- Intellij IDEA Community Edition
- Docker
- Maven

## Execução do projeto

- Antes de iniciar os serviços, você precisa construir os artefatos Maven para seus microserviços. Execute o seguinte comando na pasta raiz para cada microserviço:
  `mvn install -DskipTests`
- Agora basta  executar o comando no diretório raiz do repositório:
  `docker-compose up --build -d`

