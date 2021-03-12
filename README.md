# Este aplicativo foi desenvolvido com propósito de criar uma api de microsserviços para gerenciar clientes e seus endereços.
## Pré-requisitos
#### Docker instalado.
#### Java e JDK. >= 1.8
#
## Start da aplicação
### 1 - Clone este repositório em um diretório do seu computador.
### 2 - Dentro da pasta execute o comando `docker-compose up`. Este comando irá baixar o banco de dados Postgres e ficar online na porta padrão 5432.
### 3 - É recomendado utilizar uma IDE Java, como Eclipse ou Intellij.
### 4 - Na sua IDE de preferência selecione a opção de importar um projeto Maven e selecione o diretório do projeto baixado.
### 5 - A importação deve demorar um pouco pois é baixado vários arquivos do projeto.
### 6 - Após o download dos arquivos você pode abrir a classe SpringBootApiClientsApplication.java e executá-lá como Java Application.
### 7 - Após este passo será exposto na porta 8080 a API. Para saber mais sobre os recursos criados você pode acessar este [link](https://documenter.getpostman.com/view/1354700/Tz5p6yNY#c66510cf-849a-49c7-ba89-40fbd737378e) com uma documentação.
#
## Gerar build para deploy
### 1 - Dentro da pasta execute o comando `.\mvnw clean package`
### 2 - Será gerado uma pasta /target que se encontra o arquivo .jar.
#
