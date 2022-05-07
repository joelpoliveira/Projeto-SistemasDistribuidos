Para correr e testar a aplicação,  ́e necessário executar os ficheiros ucDrive.jar e terminal.jar. 
O ucDrive.jar inicializa um servidor como primário. Se for inicializado outro, será considerado secundário. 
O terminal.jar inicializa uma instância de cliente que se conecta ao servidor primário.

Como o projeto foi realizado num grupo de dois alunos, não há um consola de administração,
mas sim ficheiros com a configuração estática do servidor e do cliente. 

Usar os seguintes comandos para correr:
	$ java -jar ucDrive.jar
	$ java -jar terminal.jar

Para o funcionamento de ambas as aplicações,  ́e necessário um ficheiro denominado "config.yml" na pasta
"server", para os servidores, e outro na pasta "client" para os clientes. Ambas as pastas server e client precisam
de ter uma pasta users, com os vários utilizadores da aplicação. 

A pasta entregue já contém as pastas e os ficheiros necessários para o funcionamento das aplicações cliente e servidor.

Aquando da inserção de paths, ter em atenção que a aplicação funciona com uso de paths absolutos (a partir do /home)

O projeto foi desenvolvido e testado com uso do Java 17. Podem existir conflitos com versões anteriores, nomeadamente de sintaxe.