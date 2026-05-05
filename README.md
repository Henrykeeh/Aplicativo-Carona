# 🚗 App Carona - Sistema de Compartilhamento de Viagens

Projeto desenvolvido para a conclusão do **Desafio 3** no Projeto Integrador pelo **Grupo 4**.

## 📘 Introdução

O App Carona veio pra dar aquele gás no transporte compartilhado! A ideia é conectar motoristas e passageiros, otimizando os rolês diários com economia, praticidade e um toque de sustentabilidade. Tudo isso com um sistema CRUD parrudo e bem estruturado no banco de dados.

## 🎯 Objetivo

Permitir que os usuários se cadastrem como motoristas ou passageiros, criem ou encontrem caronas, tudo dentro das regras da boa convivência e com permissão de acesso direitinho, sem bagunça!

## 💻 Tecnologias Utilizadas

* Java
* Spring Tools Suite (STS)
* Spring Boot
* MySQL Workbench
* SQL
* Insomnia
* Trello
* Discord

## 🗃️ DER (Diagrama Entidade-Relacionamento)

![imagem DER](https://uploaddeimagens.com.br/images/004/896/705/original/DER_carona.png?1746626744)

## ▶️ Como rodar o projeto (passo a passo raiz)

1. **Clona o repositório**:

   ```bash
   git clone https://github.com/seu-usuario/app-carona.git
   ```

2. **Abre o projeto no STS (ou na IDE de sua preferência)**
   Se estiver usando o Spring Tools Suite, é só importar como projeto Maven existente.

3. **Configura o banco de dados no MySQL**
   Cria o banco no MySQL com o nome que tá lá no `application.properties`. Algo assim:

   ```sql
   CREATE DATABASE app_carona;
   ```

4. **Ajusta o `application.properties`** com suas configs:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/app_carona
   spring.datasource.username=seuUsuario
   spring.datasource.password=suaSenha
   spring.jpa.hibernate.ddl-auto=update
   ```

5. **Roda a aplicação**
   Dá o play na classe principal (aquela com `@SpringBootApplication`) e voilá!

6. **Testa as rotas no Insomnia/Postman**
   Usa os endpoints disponíveis pra testar o CRUD de usuários, caronas, etc.

## 👥 Integrantes

**Desenvolvedores:**

* [Guilherme Dino](https://github.com/meDinoo)
* [Camille Tarine](https://github.com/CahTarine)
* [Carlos Henrique da Silva Barbosa](https://github.com/Henrykeeh)
* [Henrique Machado](https://github.com/scottineo)
* [Beatriz Kailane](https://github.com/BeaKaylanee)

**Product Owner:**
* [Carlos Henrique da Silva Barbosa](https://github.com/Henrykeeh)

**Tester:**
* [Camille Tarine](https://github.com/CahTarine)


## 📄 Documentação

### 📄 Escopo do Projeto
[👉Clique aqui para baixar o PDF do escopo do projeto](https://drive.google.com/uc?export=download&id=16LRie7NVny_rfNj46r5s-CV2IiII50Ju)

* Projeto em deploy:* [Clique aqui pra conferir o swagger](https://aplicativo-carona-2.onrender.com)
---

