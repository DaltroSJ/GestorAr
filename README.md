# Gestor de Autoridades de Registro (Gestorar)

O **Gestor de Autoridades de Registro (Gestorar)** é um aplicativo desktop desenvolvido para auxiliar no controle e auditoria de computadores que realizam a emissão de certificados digitais. 

Com regras estritas regidas pelo **ITI (Instituto Nacional de Tecnologia da Informação)** e pelas **ACs (Autoridades Certificadoras)**, é obrigatório manter um inventário atualizado das máquinas e de seus respectivos aplicativos instalados. Esta ferramenta foi desenvolvida com o intuito de tornar esse processo de conformidade mais simples, rápido e persistente.

Esta versão foi adaptada para compartilhamento público, contando com a remoção de poucas funcionalidades sensíveis ou específicas.

## 🚀 Funcionalidades

* **Inventário de Máquinas:** Registro e controle dos computadores e dos softwares instalados para auditoria.
* **Banco de Dados Local:** Persistência de dados utilizando o **H2 Database** com gerenciamento de sessão integrado.
* **Armazenamento Seguro:** Arquivos de configuração e pastas de gerenciamento criados automaticamente no diretório do usuário local (dentro da pasta `Gestorar`).
* **Relatórios em PDF:** Exportação rápida de arquivos PDF dos inventários selecionados diretamente na aba de relatórios.
* **Interface Gráfica (GUI):** Interface intuitiva desenvolvida em Java utilizando a biblioteca **Swing**.

## 🛠️ Tecnologias Utilizadas

* **Java** (Ambiente de desenvolvimento)
* **Swing** (Interface gráfica)
* **H2 Database** (Banco de dados relacional embutido)
* **Apache Maven** (Gerenciamento de dependências e build com Maven Shade)

## 📋 Pré-requisitos

Para executar a aplicação na máquina cliente, é necessário ter o ambiente de execução Java instalado:
* **JRE (Java Runtime Environment)** ou **JDK (Java Development Kit)** instalado no sistema.

## 🔧 Como Executar a Aplicação

Caso você faça o download do arquivo `.jar` compilado:

1. Abra o terminal ou prompt de comando.
2. Navegue até a pasta onde o arquivo se encontra.
3. Execute o comando:
   ```bash
   java -jar Gestorar.jar
   ```
