# 🏨 Hotel Reservas

Sistema de gestão hoteleira desenvolvido com **Java 17 + Spring Boot 3.3.5**, com interface web em **Thymeleaf** e banco de dados **MySQL**. O projeto está em produção no Railway e pode ser acessado em:

🔗 **[sistema-reservas-hotel-production.up.railway.app](https://sistema-reservas-hotel-production.up.railway.app)**

**Acesso direto às páginas:**
- [/quartos](https://sistema-reservas-hotel-production.up.railway.app/quartos) — gerenciar quartos
- [/usuarios](https://sistema-reservas-hotel-production.up.railway.app/usuarios) — gerenciar usuários
- [/reservas](https://sistema-reservas-hotel-production.up.railway.app/reservas) — listar reservas
- [/reservas?abrirModal=true](https://sistema-reservas-hotel-production.up.railway.app/reservas?abrirModal=true) — nova reserva diretamente

---

## ✨ Funcionalidades

O sistema oferece quatro módulos principais. O **Dashboard** exibe uma visão geral em tempo real com os totais de quartos, usuários e reservas cadastrados. O módulo de **Quartos** permite cadastrar, editar, excluir e controlar o status de cada quarto (Disponível, Ocupado ou em Manutenção). O módulo de **Usuários** gerencia clientes e administradores do sistema. Por fim, o módulo de **Reservas** permite criar reservas com validação automática de conflito de datas, cancelar reservas existentes e listar todo o histórico.

A interface utiliza um tema escuro (dark theme) com modais, badges coloridos e animações CSS, tudo renderizado pelo Thymeleaf no servidor.

---

## 🛠️ Stack

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.3.5 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | MySQL 8 |
| Frontend | Thymeleaf + HTML/CSS/JS |
| Build | Maven 3.9 |
| Utilitários | Lombok |
| Deploy | Railway |

---

## 📁 Estrutura do Projeto

```
hotel-reservas/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/fernando/hotelreservas/
    │   │   ├── HotelReservasApplication.java
    │   │   ├── controller/
    │   │   │   ├── HomeController.java
    │   │   │   ├── QuartoController.java
    │   │   │   ├── ReservaController.java
    │   │   │   └── UsuarioController.java
    │   │   ├── service/
    │   │   │   ├── QuartoService.java
    │   │   │   ├── ReservaService.java
    │   │   │   └── UsuarioService.java
    │   │   ├── repository/
    │   │   │   ├── QuartoRepository.java
    │   │   │   ├── ReservaRepository.java
    │   │   │   └── UsuarioRepository.java
    │   │   ├── model/
    │   │   │   ├── Quarto.java
    │   │   │   ├── Reserva.java
    │   │   │   └── Usuario.java
    │   │   ├── dto/
    │   │   │   ├── QuartoDTO.java
    │   │   │   ├── ReservaDTO.java
    │   │   │   └── UsuarioDTO.java
    │   │   └── exception/
    │   │       ├── BusinessException.java
    │   │       ├── ResourceNotFoundException.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       ├── application.properties
    │       └── templates/
    │           ├── fragments.html   ← layout e nav compartilhados
    │           ├── index.html       ← dashboard
    │           ├── quartos.html
    │           ├── reservas.html
    │           └── usuarios.html
    └── test/
        └── java/com/fernando/hotelreservas/
            └── HotelReservasApplicationTests.java
```

> ⚠️ **Atenção:** os nomes dos templates devem estar em **letras minúsculas**. O servidor Linux do Railway diferencia maiúsculas de minúsculas — `Index.html` e `index.html` são arquivos diferentes para ele, então usar maiúscula causa erro 500.

---

## 🏗️ Arquitetura

O projeto segue o padrão em camadas **Controller → Service → Repository → Model**, que é a forma mais comum de organizar aplicações Spring Boot.

O **Controller** é a porta de entrada — ele recebe a requisição HTTP do navegador e delega o trabalho para o Service. O **Service** contém as regras de negócio, como validar se as datas fazem sentido ou se um quarto está disponível. O **Repository** é responsável por conversar com o banco de dados usando Spring Data JPA. O **Model** representa as tabelas do banco como classes Java. Os **DTOs** são objetos usados para transferir dados entre as camadas sem expor diretamente as entidades do banco. Por fim, o **GlobalExceptionHandler** captura erros em qualquer ponto da aplicação e retorna respostas padronizadas.

---

## 🗄️ Modelos de Dados

### Quarto
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | Chave primária |
| numero | String | Número único do quarto |
| tipo | Enum | `SOLTEIRO` `CASAL` `SUITE` `DELUXE` `PRESIDENCIAL` |
| preco | BigDecimal | Preço por diária |
| status | Enum | `DISPONIVEL` `OCUPADO` `MANUTENCAO` |
| hotelId | Long | Referência ao hotel |

### Usuario
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | Chave primária |
| nome | String | Nome completo |
| email | String | Email único |
| senha | String | Senha do usuário |
| tipo | Enum | `ADMIN` `CLIENTE` |

### Reserva
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | Chave primária |
| dataEntrada | LocalDate | Data de check-in |
| dataSaida | LocalDate | Data de check-out |
| status | Enum | `PENDENTE` `CONFIRMADA` `CANCELADA` `CONCLUIDA` |
| usuario | Usuario | Hóspede — `@ManyToOne` |
| quarto | Quarto | Quarto reservado — `@ManyToOne` |

---

## 🌐 Rotas

### Dashboard
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/` | Página inicial com totais |

### Quartos
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/quartos` | Listagem de quartos |
| POST | `/quartos` | Cadastrar quarto |
| POST | `/quartos/{id}/atualizar` | Atualizar quarto |
| POST | `/quartos/{id}/excluir` | Excluir quarto |

### Usuários
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/usuarios` | Listagem de usuários |
| POST | `/usuarios` | Cadastrar usuário |
| POST | `/usuarios/{id}/atualizar` | Atualizar usuário |
| POST | `/usuarios/{id}/excluir` | Excluir usuário |

### Reservas
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/reservas` | Listagem de reservas |
| GET | `/reservas?abrirModal=true` | Abre a página já com o modal de nova reserva ativo |
| POST | `/reservas` | Criar reserva |
| POST | `/reservas/{id}/cancelar` | Cancelar reserva |

---

## ⚙️ Configuração e Execução

### Pré-requisitos

Para rodar o projeto localmente você vai precisar de Java 17+, Maven 3.9+ e MySQL 8+.

### 1. Criar o banco de dados

```sql
CREATE DATABASE hotel;
```

### 2. Configurar `application.properties`

```properties
spring.application.name=hotel-reservas

# URL local para desenvolvimento
spring.datasource.url=jdbc:mysql://localhost:3306/hotel
spring.datasource.username=root
spring.datasource.password=SUA_SENHA

# Porta dinâmica (necessário para o Railway)
server.port=${PORT:8080}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

> As tabelas são criadas automaticamente pelo Hibernate na primeira execução graças ao `ddl-auto=update`.

### 3. Compilar e executar

```bash
mvn clean compile
mvn spring-boot:run
```

Acesse localmente em: **[http://localhost:8080](http://localhost:8080)**

---

## ☁️ Deploy no Railway

O projeto está configurado para deploy automático via GitHub. A cada `git push`, o Railway detecta a mudança e faz o redeploy automaticamente.

Para o banco de dados, o Railway fornece um plugin MySQL com as variáveis de conexão. A URL de produção fica no formato `jdbc:mysql://HOST_RAILWAY:PORTA/railway` e deve ser configurada diretamente no `application.properties` ou via variáveis de ambiente no painel do Railway.

---

## 🔒 Validações de Negócio

O sistema aplica diversas regras antes de confirmar uma reserva. A data de saída deve ser posterior à data de entrada. Quartos com status `MANUTENCAO` não podem ser reservados. O sistema verifica conflito de datas via JPQL antes de confirmar:

```sql
SELECT COUNT(r) > 0 FROM Reserva r
WHERE r.quarto.id = :quartoId
  AND r.status NOT IN ('CANCELADA')
  AND r.dataEntrada < :dataSaida
  AND r.dataSaida > :dataEntrada
```

Além disso, o email de cada usuário deve ser único, o número de cada quarto deve ser único, e reservas com status `CONCLUIDA` não podem ser canceladas.

---

## 🔮 Melhorias Futuras

O projeto tem uma base sólida e pode evoluir em várias direções. A adição de **Spring Security** permitiria uma tela de login real com controle de acesso diferenciado entre ADMIN e CLIENTE. O uso de **BCrypt** criptografaria as senhas antes de salvar no banco, o que é essencial para produção. A integração com **Swagger UI** geraria documentação interativa da API em `/swagger-ui.html`. Paginação nas listagens melhoraria a performance com grandes volumes de dados. Por fim, relatórios de ocupação por período dariam uma visão gerencial mais completa ao sistema.

---

## 👨‍💻 Autor

**Fernando Jesus dos Santos** — desenvolvido com Spring Boot 3.3.5 + Thymeleaf + MySQL.
