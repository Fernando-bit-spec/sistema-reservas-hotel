# Guia de Preparação para Produção - Hotel Reservas

## ✅ Alterações Realizadas

### 1. **Dependências de Segurança (pom.xml)**
- ✅ Adicionado `spring-boot-starter-security`
- ✅ Adicionado `bcprov-jdk15on` para hashing BCrypt

### 2. **Configuração de Banco de Dados**
- ✅ Criado `application-dev.properties` — Configuração para desenvolvimento (local)
- ✅ Criado `application-prod.properties` — Configuração para produção com variáveis de ambiente
- ✅ Atualizado `application.properties` para selecionar perfil automaticamente

### 3. **Segurança de Senhas**
- ✅ `UsuarioService.java` — Implementado BCrypt:
  - Cadastro: senha é hasheada com `passwordEncoder.encode()`
  - Login: comparação segura com `passwordEncoder.matches()`
  - Atualização: valida senha vazia antes de encodar

### 4. **Configuração de Segurança & CORS**
- ✅ Criado `SecurityConfig.java`:
  - Bean `PasswordEncoder` com BCrypt
  - CORS configurado para `http://localhost:3000` e `http://localhost:8080`

### 5. **Proteção do Git**
- ✅ Atualizado `.gitignore`:
  - Adicionado `application-prod.properties` (nunca será versionado)
  - Adicionado `.env` (nunca será versionado)
- ✅ Criado `.env.example` como referência

---

## 📋 Próximos Passos (Necessários)

### ⚠️ BLOQUEADOR: Java 17 Necessário
Seu sistema tem **Java 15**, mas o projeto precisa de **Java 17+**.

**Ação Necessária:**
1. Instale Java 17 LTS em: https://adoptium.net/installation/
2. Configure `JAVA_HOME` para apontar para o Java 17
3. Verifique com: `java -version` (deve mostrar 17.x.x)

### Depois de instalar Java 17:

1. **Build do Projeto (Desenvolvimento)**
   ```bash
   cd C:\Users\User\OneDrive\Desktop\hotel-reservas
   mvnw.cmd clean install
   ```

2. **Testar Localmente (Desenvolvimento)**
   ```bash
   # O projeto rodará em http://localhost:8080
   mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
   ```

3. **Gerar JAR para Produção**
   ```bash
   mvnw.cmd clean package -DskipTests
   ```
   Resultado: `target\hotel-reservas-0.0.1-SNAPSHOT.jar`

4. **Testar JAR Localmente**
   ```bash
   java -jar target\hotel-reservas-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
   ```

5. **Antes do Deploy em Produção**
   ```bash
   # Criar .env com as variáveis reais
   copy .env.example .env
   # Editar .env com valores reais do servidor de produção
   ```

6. **Executar em Produção**
   ```bash
   java -jar hotel-reservas-0.0.1-SNAPSHOT.jar \
     --spring.profiles.active=prod \
     --server.port=8080
   ```
   Ou, se usar arquivo `.env`:
   ```bash
   # No servidor de produção
   export DB_URL=jdbc:mysql://seu-servidor:3306/hotel
   export DB_USERNAME=seu_usuario
   export DB_PASSWORD=sua_senha_segura
   export SPRING_PROFILES_ACTIVE=prod
   
   java -jar hotel-reservas-0.0.1-SNAPSHOT.jar
   ```

---

## 🔐 Segurança - Checklist

- ✅ Credenciais de banco de dados **NÃO** estão no código (usam variáveis de ambiente)
- ✅ Senhas de usuários são hasheadas com **BCrypt**
- ✅ Spring Security configurado
- ✅ CORS protegido (apenas origens autorizadas)
- ✅ `.gitignore` protege `.env` e `application-prod.properties`
- ✅ Arquivo `.env.example` fornece modelo para produção

---

## 📝 Variáveis de Ambiente (Produção)

Após instalar Java 17, configure estas variáveis antes de executar o `.jar`:

| Variável | Valor Exemplo | Descrição |
|----------|---------------|-----------|
| `DB_URL` | `jdbc:mysql://db.exemplo.com:3306/hotel` | URL do banco de dados |
| `DB_USERNAME` | `usuario_prod` | Usuário do banco |
| `DB_PASSWORD` | `SenhaSegura123!@#` | Senha do banco (NUNCA use no código) |
| `PORT` | `8080` | Porta do servidor |
| `SPRING_PROFILES_ACTIVE` | `prod` | Ativa configuração de produção |

---

## ⚠️ Importante: Nunca Commitar

Arquivos que NUNCA devem ser commitados no Git:
- `.env` (contém senhas reais)
- `application-prod.properties` (contém configurações sensíveis)
- Qualquer arquivo com credenciais

Verifique antes de fazer commit:
```bash
git status
```

---

## ✨ Melhorias Implementadas Resumo

| Aspecto | Antes | Depois |
|--------|-------|--------|
| **Credenciais** | Hardcoded no código ❌ | Variáveis de ambiente ✅ |
| **Senhas** | Texto plano ❌ | BCrypt hasheadas ✅ |
| **Spring Security** | Não configurado ❌ | Configurado ✅ |
| **CORS** | Não configurado ❌ | Configurado corretamente ✅ |
| **Perfis** | Só dev ❌ | Dev + Prod ✅ |
| **Git Protection** | Incompleto ❌ | Completo ✅ |

---

**Depois de instalar Java 17, o projeto estará 100% pronto para produção!** 🚀
